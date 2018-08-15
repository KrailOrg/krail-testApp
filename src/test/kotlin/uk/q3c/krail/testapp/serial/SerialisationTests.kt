package uk.q3c.krail.testapp.serial

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.vaadin.server.VaadinSession
import com.vaadin.ui.Grid
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.MenuBar
import com.vaadin.ui.Tree
import com.vaadin.ui.UI
import io.mockk.mockk
import org.amshove.kluent.shouldBeEmpty
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.reflections.Reflections
import uk.q3c.krail.config.ConfigFile
import uk.q3c.krail.core.env.ServletEnvironmentModule
import uk.q3c.krail.core.form.ConverterPair
import uk.q3c.krail.core.form.DefaultMultiSelectProperty
import uk.q3c.krail.core.form.DefaultSingleSelectProperty
import uk.q3c.krail.core.form.DetailPropertyInfo
import uk.q3c.krail.core.form.Form
import uk.q3c.krail.core.form.FormConfiguration
import uk.q3c.krail.core.form.FormDao
import uk.q3c.krail.core.form.FormDetailSection
import uk.q3c.krail.core.form.FormSectionConfiguration
import uk.q3c.krail.core.form.FormTableSection
import uk.q3c.krail.core.form.KrailBeanValidationBinder
import uk.q3c.krail.core.form.KrailBeanValidationBinderFactory
import uk.q3c.krail.core.form.MapDBBaseDao
import uk.q3c.krail.core.form.MultiSelectPropertyDelegate
import uk.q3c.krail.core.form.PropertyConfiguration
import uk.q3c.krail.core.form.SingleSelectPropertyDelegate
import uk.q3c.krail.core.guice.uiscope.UIKey
import uk.q3c.krail.core.i18n.VaadinCurrentLocale
import uk.q3c.krail.core.navigate.DefaultNavigator
import uk.q3c.krail.core.navigate.NavigationState
import uk.q3c.krail.core.navigate.Navigator
import uk.q3c.krail.core.navigate.sitemap.DirectSitemapEntry
import uk.q3c.krail.core.navigate.sitemap.EmptyView
import uk.q3c.krail.core.navigate.sitemap.MasterSitemapNode
import uk.q3c.krail.core.navigate.sitemap.RedirectEntry
import uk.q3c.krail.core.navigate.sitemap.UserSitemap
import uk.q3c.krail.core.navigate.sitemap.UserSitemapNode
import uk.q3c.krail.core.persist.MapDbFormDaoFactory
import uk.q3c.krail.core.shiro.PageAccessControl
import uk.q3c.krail.core.vaadin.TargetTreeWrapper_VaadinTree
import uk.q3c.krail.core.vaadin.UserSitemapNodeCaption
import uk.q3c.krail.core.view.NavigationStateExt
import uk.q3c.krail.core.view.component.ComponentIDAssignmentFilter
import uk.q3c.krail.core.view.component.ComponentIDDrilldownFilter
import uk.q3c.krail.core.view.component.MenuBarNodeModifier
import uk.q3c.krail.core.view.component.UserNavigationTree
import uk.q3c.krail.core.view.component.UserNavigationTreeNodeModifier
import uk.q3c.krail.functest.coded.CodedBrowser
import uk.q3c.krail.i18n.test.MockCurrentLocale
import uk.q3c.krail.option.OptionKey
import uk.q3c.krail.service.ServiceKey
import uk.q3c.krail.testapp.i18n.LabelKey
import uk.q3c.krail.testapp.view.AutoForm
import uk.q3c.krail.testapp.view.Person
import uk.q3c.krail.testapp.view.TestAppBindingsCollator
import uk.q3c.util.clazz.UnenhancedClassIdentifier
import uk.q3c.util.forest.BasicForest
import uk.q3c.util.forest.TargetTreeWrapper_BasicForest
import uk.q3c.util.guice.GuiceKeyProxy
import uk.q3c.util.serial.tracer.SerializationOutcome
import uk.q3c.util.serial.tracer.SerializationTracer
import java.io.Serializable
import java.lang.reflect.Modifier

/**
 * Created by David Sowerby on 13 Mar 2018
 */
object SerialisationTest : Spek({

    beforeGroup {
        resetVaadin()
        val codedBrowser = CodedBrowser()
        codedBrowser.setup()
    }

    given("we want to check all classes that implement Serializable, do actually serialise") {
        val injector = Guice.createInjector(TestAppBindingsCollator(ServletEnvironmentModule()).allModules())
        val coreClasses: List<Class<out Serializable>> = listSerializableClasses()
        val manualConstructs = mutableListOf<String>()
        val mc: MockCurrentLocale

        coreClasses.forEach { clazz ->
            given(clazz.simpleName) {

                on("constructing via Guice preferably, or manually in 'constructNonGuiceClass', and running trace") {
                    val tracer = SerializationTracer()
                    var instance: Any
                    try {
                        instance = injector.getInstance(clazz)
                    } catch (e: com.google.inject.ConfigurationException) {
                        try {
                            instance = constructNonGuiceClass(clazz, injector)
                        } catch (e: TestConfigurationException) {
                            manualConstructs.add(clazz.name)
                            throw AssertionError(e)
                        }

                    }
                    tracer.trace(instance)
                    it("has no serialisation failures") {
                        tracer.shouldNotHaveAny(SerializationOutcome.FAIL)
                    }

                }

            }
        }

        on("constructing AutoForm and invoking loadData()") {
            val tracer = SerializationTracer()
            val form = injector.getInstance(AutoForm::class.java)
            form.init()
            form.buildView(mockk())
            form.loadData(mockk())
            tracer.trace(form)

            it("should not have any trace failures") {
                tracer.shouldNotHaveAny(SerializationOutcome.FAIL)
            }
        }

        on("checking manual constructs") {
            println("Missing manual constructs: $manualConstructs")

            it("should have no manual constructs missing") {
                manualConstructs.shouldBeEmpty()
            }
        }
    }
})

fun constructNonGuiceClass(clazz: Class<out Serializable>, injector: Injector): Serializable {

    return when (clazz) {
        UIKey::class.java -> UIKey()
        ServiceKey::class.java -> ServiceKey(LabelKey.Accounts)
        GuiceKeyProxy::class.java -> GuiceKeyProxy(Key.get(DefaultNavigator::class.java))
        ConverterPair::class.java -> ConverterPair(UIKey::class.java, ServiceKey::class.java)
        MasterSitemapNode::class.java -> MasterSitemapNode(3, "any")
        UserSitemapNode::class.java -> userSitemapNode()

        TargetTreeWrapper_BasicForest::class.java -> TargetTreeWrapper_BasicForest<String, String>(BasicForest<String>())
        UserNavigationTreeNodeModifier::class.java -> UserNavigationTreeNodeModifier(injector.getInstance(UserNavigationTree::class.java), injector.getInstance(UserSitemap::class.java))
        MenuBarNodeModifier::class.java -> MenuBarNodeModifier(MenuBar(), injector.getInstance(Navigator::class.java), UserSitemapNodeCaption(), injector.getInstance(UserSitemap::class.java))
        TargetTreeWrapper_VaadinTree::class.java -> TargetTreeWrapper_VaadinTree<String, String>(Tree<String>())
        ComponentIDDrilldownFilter::class.java -> ComponentIDDrilldownFilter(injector.getInstance(UnenhancedClassIdentifier::class.java))
        ComponentIDAssignmentFilter::class.java -> ComponentIDAssignmentFilter(injector.getInstance(UnenhancedClassIdentifier::class.java))
        ConfigFile::class.java -> ConfigFile("anyfile")
        DirectSitemapEntry::class.java -> DirectSitemapEntry(moduleName = "a", viewClass = EmptyView::class.java, labelKey = LabelKey.Accounts, pageAccessControl = PageAccessControl.AUTHENTICATION, positionIndex = 5)
        RedirectEntry::class.java -> RedirectEntry(redirectTarget = "home")
//        KrailAutoBinder::class.java ->{
//            val easyBinder = injector.getInstance(EasyBinder::class.java)
//            return easyBinder.auto(TestObject::class.java)
//        }
        Person::class.java -> Person(name = "george", age = 23, title = "Mr")
        OptionKey::class.java -> VaadinCurrentLocale.optionPreferredLocale
        NavigationStateExt::class.java -> NavigationStateExt(NavigationState(), NavigationState(), userSitemapNode())
        PropertyConfiguration::class.java -> PropertyConfiguration("config", sectionConfiguration())
        FormSectionConfiguration::class.java -> sectionConfiguration()
        FormDao::class.java -> dao()
        MapDBBaseDao::class.java -> dao()
        SingleSelectPropertyDelegate::class.java -> SingleSelectPropertyDelegate<Person, String>(setOf("a", "B"))
        DefaultSingleSelectProperty::class.java -> DefaultSingleSelectProperty(setOf("a", "B"))
        DefaultMultiSelectProperty::class.java -> DefaultMultiSelectProperty(setOf("a", "B"))
        MultiSelectPropertyDelegate::class.java -> MultiSelectPropertyDelegate<Person, String>(setOf("a", "B"))
        FormDetailSection::class.java -> FormDetailSection(dao = dao(), rootComponent = HorizontalLayout(), propertyMap = propertyMap(), binder = binder(injector))
        DetailPropertyInfo::class.java -> detailPropertyInfo()
        FormTableSection::class.java -> FormTableSection(form = injector.getInstance(Form::class.java), rootComponent = Grid(), dao = dao())


        else -> throw TestConfigurationException("We have no method for constructing $clazz, add it to function 'constructNonGuiceClass'")
    }
}

class TestConfigurationException(msg: String) : RuntimeException(msg)


private fun propertyMap(): Map<String, DetailPropertyInfo> {
    val info1 = detailPropertyInfo()
    val info2 = detailPropertyInfo()
    val map: Map<String, DetailPropertyInfo> = mapOf(Pair("a", info1), Pair("b", info2))
    return map
}

private fun detailPropertyInfo(): DetailPropertyInfo {
    return DetailPropertyInfo(LabelKey.Accounts, LabelKey.Authenticated, Label(), true)
}

private fun userSitemapNode(): UserSitemapNode {
    val masterNode = MasterSitemapNode(3, "any")
    return UserSitemapNode(masterNode)
}

private fun binder(injector: Injector): KrailBeanValidationBinder<Person> {
    val factory = injector.getInstance(KrailBeanValidationBinderFactory::class.java)
    return factory.create(Person::class)
}

private fun dao(): FormDao<Person> {
    return MapDbFormDaoFactory().getDao(Person::class)
}

private fun sectionConfiguration(): FormSectionConfiguration {
    return FormSectionConfiguration(formConfiguration(), name = "x")
}

private fun formConfiguration(): FormConfiguration {
    return FormConfiguration1()
}

fun listSerializableClasses(): List<Class<out Serializable>> {
    Guice.createInjector(TestAppBindingsCollator(ServletEnvironmentModule()).allModules())
    val reflections = Reflections("uk.q3c")
    val classes = reflections.getSubTypesOf(Serializable::class.java).filter { clazz -> !Modifier.isAbstract(clazz.modifiers) }
    return classes
}

fun resetVaadin() {
    VaadinSession.setCurrent(null)
    UI.setCurrent(null)
}

class TestObject : Serializable {
    val age: Int = 12
    val name: String = "wiggly"
}

private class FormConfiguration1 : FormConfiguration() {
    override fun config() {

    }

}