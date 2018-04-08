package uk.q3c.krail.testapp

import com.google.inject.Guice
import org.apache.commons.lang3.SerializationUtils
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.reflections.Reflections
import uk.q3c.krail.TestServiceSerial
import uk.q3c.krail.core.guice.ServletEnvironmentModule
import uk.q3c.krail.core.ui.ScopedUIProvider
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.functest.coded.CodedBrowser
import uk.q3c.krail.testapp.view.TestAppBindingsCollator
import java.io.Serializable
import java.lang.reflect.Modifier

/**
 * Created by David Sowerby on 13 Mar 2018
 */
object CoreClassesSerialisationTest : Spek({

    beforeGroup {
        val codedBrowser = CodedBrowser()
        codedBrowser.setup()
    }

    given("we want core classes to be serializable") {
        val injector = Guice.createInjector(TestAppBindingsCollator(ServletEnvironmentModule()).allModules())
        val coreClasses: List<Class<out Serializable>> = listOf(
//                DefaultJWTKeyProvider::class.java,
//                DefaultJWTProvider::class.java,
//                BrowserProvider::class.java,
//                SubjectProvider::class.java,
//                VaadinCurrentLocale::class.java,
//                DefaultPatternSource::class.java,
//                DefaultMessageFormat::class.java,
//                DefaultTranslate::class.java,
//                DefaultUserNotifier::class.java,
//                OptionKeyLocator::class.java,
//                DefaultConverterFactory::class.java,
//                DefaultDataConverter::class.java,
//                DefaultInMemoryOptionStore::class.java,
//                DefaultOptionDao::class.java,
//                DefaultOptionCacheProvider::class.java,
//                DefaultOptionCache::class.java,
//                KrailOptionPermissionVerifier::class.java,
//                DefaultOption::class.java,
//                DefaultOptionBinder::class.java,
//                DefaultOptionPopup::class.java,
//                EasyBinder::class.java,
//
//                DefaultOptionSource::class.java,
//                DefaultPersistenceInfo::class.java,
//                DefaultUserNotifier::class.java,
//                DefaultBroadcaster::class.java,
//                StrictURIFragmentHandler::class.java,
//                MasterSitemapQueue::class.java,
//
//                DefaultServiceClassGraph::class.java,
//                DefaultServiceInstanceGraph::class.java,
//                DefaultServiceModel::class.java,
//
//                UserSitemapNodeModifier::class.java,
//                DefaultUserSitemap::class.java,
//                DefaultUserNavigationTreeBuilder::class.java,
//
//                DefaultSubjectProvider::class.java,
//                PageAccessController::class.java,
//                ScopedUIProvider::class.java,
//                DefaultViewFactory::class.java,
//                UserSitemapBuilder::class.java,
//                DefaultLoginNavigationRule::class.java,
//                DefaultLogoutNavigationRule::class.java,
//                DefaultUIBusProvider::class.java,
//                DefaultSessionBusProvider::class.java,
//                DefaultUserSitemapSorters::class.java,
//                DefaultViewChangeRule::class.java,
//                DefaultInvalidURIHandler::class.java,
//                DefaultComponentIdGenerator::class.java,

//                DefaultRelatedServiceExecutor::class.java,
//                DefaultResourceUtils::class.java,
//                DefaultClassNameUtils::class.java,
//                DefaultApplicationConfiguration::class.java,
//                DefaultMasterSitemapQueue::class.java,
//                DefaultSitemapFinisher::class.java,
//                DefaultApplicationConfigurationService::class.java
                TestServiceSerial::class.java
//                DefaultSitemapService::class.java
//                DefaultNavigator::class.java
        )

        coreClasses.forEach { test ->
            given(test.simpleName) {

                on("doing it") {
                    println(">>>>> ${test.simpleName}")
                    val instance = injector.getInstance(test)
                    val output = SerializationUtils.serialize(instance)
                    val result: Serializable = SerializationUtils.deserialize(output)

                    it("does not throw exception") {

                    }

                }


            }
        }
    }
})

object ViewSerialisationTest : Spek({

    beforeGroup {
        val codedBrowser = CodedBrowser()
        codedBrowser.setup()
    }

    given("we want all views to be serializable") {
        val injector = Guice.createInjector(TestAppBindingsCollator(ServletEnvironmentModule()).allModules())
        val coreClasses: List<Class<out KrailView>> = listViews()
        coreClasses.forEach { test ->
            given(test.simpleName) {

                on("doing it") {
                    println(">>>>> ${test.simpleName}")
                    val instance = injector.getInstance(test)
                    val output = SerializationUtils.serialize(instance)
                    val result: Serializable = SerializationUtils.deserialize(output)

                    it("does not throw exception") {

                    }

                }


            }
        }
    }
})

fun listViews(): List<Class<out KrailView>> {
    val injector = Guice.createInjector(TestAppBindingsCollator(ServletEnvironmentModule()).allModules())
    val scopedUIProvider = injector.getInstance(ScopedUIProvider::class.java)
    scopedUIProvider.get()
    val reflections = Reflections()
    val classes = reflections.getSubTypesOf(KrailView::class.java).filter { clazz -> !Modifier.isAbstract(clazz.modifiers) }
    return classes
}