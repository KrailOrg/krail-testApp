package uk.q3c.krail.testapp.serial

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.vaadin.server.VaadinSession
import com.vaadin.ui.MenuBar
import com.vaadin.ui.Tree
import com.vaadin.ui.UI
import org.amshove.kluent.shouldBeEmpty
import org.apache.bval.jsr303.ApacheValidatorFactory
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.reflections.Reflections
import uk.q3c.krail.core.guice.ServletEnvironmentModule
import uk.q3c.krail.core.guice.uiscope.UIKey
import uk.q3c.krail.core.navigate.DefaultNavigator
import uk.q3c.krail.core.navigate.Navigator
import uk.q3c.krail.core.navigate.sitemap.MasterSitemapNode
import uk.q3c.krail.core.navigate.sitemap.UserSitemap
import uk.q3c.krail.core.navigate.sitemap.UserSitemapNode
import uk.q3c.krail.core.vaadin.ConverterPair
import uk.q3c.krail.core.vaadin.TargetTreeWrapper_VaadinTree
import uk.q3c.krail.core.vaadin.UserSitemapNodeCaption
import uk.q3c.krail.core.validation.SimpleContext
import uk.q3c.krail.core.view.component.ComponentIDAssignmentFilter
import uk.q3c.krail.core.view.component.ComponentIDDrilldownFilter
import uk.q3c.krail.core.view.component.MenuBarNodeModifier
import uk.q3c.krail.core.view.component.UserNavigationTree
import uk.q3c.krail.core.view.component.UserNavigationTreeNodeModifier
import uk.q3c.krail.functest.coded.CodedBrowser
import uk.q3c.krail.service.ServiceKey
import uk.q3c.krail.testapp.i18n.LabelKey
import uk.q3c.krail.testapp.view.TestAppBindingsCollator
import uk.q3c.util.clazz.UnenhancedClassIdentifier
import uk.q3c.util.forest.BasicForest
import uk.q3c.util.forest.TargetTreeWrapper_BasicForest
import uk.q3c.util.guice.GuiceKeyProxy
import uk.q3c.util.serial.tracer.SerializationOutcome
import uk.q3c.util.serial.tracer.SerializationTracer
import java.io.Serializable
import java.lang.reflect.Modifier
import javax.validation.constraints.Max

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
        UIKey::class.java -> UIKey(5)
        ServiceKey::class.java -> ServiceKey(LabelKey.Accounts)
        GuiceKeyProxy::class.java -> GuiceKeyProxy(Key.get(DefaultNavigator::class.java))
        ConverterPair::class.java -> ConverterPair(UIKey::class.java, ServiceKey::class.java)
        MasterSitemapNode::class.java -> MasterSitemapNode(3, "any")
        UserSitemapNode::class.java -> {
            val masterNode = MasterSitemapNode(3, "any")
            UserSitemapNode(masterNode)
        }
        TargetTreeWrapper_BasicForest::class.java -> TargetTreeWrapper_BasicForest<String, String>(BasicForest<String>())
        UserNavigationTreeNodeModifier::class.java -> UserNavigationTreeNodeModifier(injector.getInstance(UserNavigationTree::class.java), injector.getInstance(UserSitemap::class.java))
        MenuBarNodeModifier::class.java -> MenuBarNodeModifier(MenuBar(), injector.getInstance(Navigator::class.java), UserSitemapNodeCaption(), injector.getInstance(UserSitemap::class.java))
        SimpleContext::class.java -> {
            val validator = ApacheValidatorFactory.getDefault().validator
            val descriptor = validator.getConstraintsForClass(ValidatedClass::class.java).getConstraintsForProperty("age").constraintDescriptors.iterator().next()
            SimpleContext("propertyName", "boo", descriptor)
        }
        TargetTreeWrapper_VaadinTree::class.java -> TargetTreeWrapper_VaadinTree<String, String>(Tree<String>())
        ComponentIDDrilldownFilter::class.java -> ComponentIDDrilldownFilter(injector.getInstance(UnenhancedClassIdentifier::class.java))
        ComponentIDAssignmentFilter::class.java -> ComponentIDAssignmentFilter(injector.getInstance(UnenhancedClassIdentifier::class.java))


        else -> throw TestConfigurationException("We have no method for constructing $clazz, add it to function 'constructNonGuiceClass'")
    }
}

class TestConfigurationException(msg: String) : RuntimeException(msg)


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

/**
 *  final Validator gvalidator = getValidator();

assertFalse(gvalidator.getConstraintsForClass(PreferredGuest.class)
.getConstraintsForProperty("guestCreditCardNumber").getConstraintDescriptors().isEmpty());

MessageInterpolator.Context ctx = new MessageInterpolator.Context() {

@Override
public ConstraintDescriptor<?> getConstraintDescriptor() {
return gvalidator.getConstraintsForClass(PreferredGuest.class)
.getConstraintsForProperty("guestCreditCardNumber").getConstraintDescriptors().iterator().next();
}

@Override
public Object getValidatedValue() {
return "12345678";
}

@Override
public <T> T unwrap(Class<T> type) {
return null;
}
};
 */

class ValidatedClass {
    @Max(5)
    var age: Int = 23
}