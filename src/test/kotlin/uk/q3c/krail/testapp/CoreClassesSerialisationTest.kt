package uk.q3c.krail.testapp

import com.google.inject.Guice
import org.apache.commons.lang3.SerializationUtils
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.reflections.Reflections
import uk.q3c.krail.config.config.DefaultApplicationConfiguration
import uk.q3c.krail.config.service.DefaultApplicationConfigurationService
import uk.q3c.krail.core.eventbus.DefaultSessionBusProvider
import uk.q3c.krail.core.eventbus.DefaultUIBusProvider
import uk.q3c.krail.core.form.EasyBinder
import uk.q3c.krail.core.guice.ServletEnvironmentModule
import uk.q3c.krail.core.i18n.VaadinCurrentLocale
import uk.q3c.krail.core.navigate.DefaultInvalidURIHandler
import uk.q3c.krail.core.navigate.DefaultLoginNavigationRule
import uk.q3c.krail.core.navigate.DefaultLogoutNavigationRule
import uk.q3c.krail.core.navigate.DefaultNavigator
import uk.q3c.krail.core.navigate.DefaultViewChangeRule
import uk.q3c.krail.core.navigate.StrictURIFragmentHandler
import uk.q3c.krail.core.navigate.sitemap.DefaultSitemapFinisher
import uk.q3c.krail.core.navigate.sitemap.DefaultSitemapService
import uk.q3c.krail.core.navigate.sitemap.DefaultUserSitemap
import uk.q3c.krail.core.navigate.sitemap.UserSitemapBuilder
import uk.q3c.krail.core.navigate.sitemap.UserSitemapNodeModifier
import uk.q3c.krail.core.navigate.sitemap.comparator.DefaultUserSitemapSorters
import uk.q3c.krail.core.navigate.sitemap.set.DefaultMasterSitemapQueue
import uk.q3c.krail.core.navigate.sitemap.set.MasterSitemapQueue
import uk.q3c.krail.core.option.DefaultOptionPopup
import uk.q3c.krail.core.option.KrailOptionPermissionVerifier
import uk.q3c.krail.core.push.DefaultBroadcaster
import uk.q3c.krail.core.shiro.DefaultJWTKeyProvider
import uk.q3c.krail.core.shiro.DefaultJWTProvider
import uk.q3c.krail.core.shiro.DefaultSubjectProvider
import uk.q3c.krail.core.shiro.PageAccessController
import uk.q3c.krail.core.shiro.SubjectProvider
import uk.q3c.krail.core.ui.BrowserProvider
import uk.q3c.krail.core.ui.ScopedUIProvider
import uk.q3c.krail.core.user.notify.DefaultUserNotifier
import uk.q3c.krail.core.vaadin.DefaultConverterFactory
import uk.q3c.krail.core.vaadin.DefaultOptionBinder
import uk.q3c.krail.core.view.DefaultViewFactory
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.core.view.component.DefaultComponentIdGenerator
import uk.q3c.krail.core.view.component.DefaultUserNavigationTreeBuilder
import uk.q3c.krail.functest.coded.CodedBrowser
import uk.q3c.krail.i18n.persist.source.DefaultPatternSource
import uk.q3c.krail.i18n.translate.DefaultTranslate
import uk.q3c.krail.option.option.DefaultOption
import uk.q3c.krail.option.option.OptionKeyLocator
import uk.q3c.krail.option.persist.cache.DefaultOptionCache
import uk.q3c.krail.option.persist.cache.DefaultOptionCacheProvider
import uk.q3c.krail.option.persist.dao.DefaultOptionDao
import uk.q3c.krail.option.persist.source.DefaultOptionSource
import uk.q3c.krail.persist.DefaultPersistenceInfo
import uk.q3c.krail.persist.inmemory.store.DefaultInMemoryOptionStore
import uk.q3c.krail.testapp.view.TestAppBindingsCollator
import uk.q3c.krail.util.DefaultResourceUtils
import uk.q3c.util.clazz.DefaultClassNameUtils
import uk.q3c.util.data.DefaultDataConverter
import uk.q3c.util.text.DefaultMessageFormat
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
                DefaultJWTKeyProvider::class.java,
                DefaultJWTProvider::class.java,
                BrowserProvider::class.java,
                SubjectProvider::class.java,
                VaadinCurrentLocale::class.java,
                DefaultPatternSource::class.java,
                DefaultMessageFormat::class.java,
                DefaultTranslate::class.java,
                DefaultUserNotifier::class.java,
                OptionKeyLocator::class.java,
                DefaultConverterFactory::class.java,
                DefaultDataConverter::class.java,
                DefaultInMemoryOptionStore::class.java,
                DefaultOptionDao::class.java,
                DefaultOptionCacheProvider::class.java,
                DefaultOptionCache::class.java,
                KrailOptionPermissionVerifier::class.java,
                DefaultOption::class.java,
                DefaultOptionBinder::class.java,
                DefaultOptionPopup::class.java,
                EasyBinder::class.java,

                DefaultOptionSource::class.java,
                DefaultPersistenceInfo::class.java,
                DefaultUserNotifier::class.java,
                DefaultBroadcaster::class.java,
                StrictURIFragmentHandler::class.java,
                MasterSitemapQueue::class.java,


                UserSitemapNodeModifier::class.java,
                DefaultUserSitemap::class.java,
                DefaultUserNavigationTreeBuilder::class.java,

                DefaultSubjectProvider::class.java,
                PageAccessController::class.java,
                ScopedUIProvider::class.java,
                DefaultViewFactory::class.java,
                UserSitemapBuilder::class.java,
                DefaultLoginNavigationRule::class.java,
                DefaultLogoutNavigationRule::class.java,
                DefaultUIBusProvider::class.java,
                DefaultSessionBusProvider::class.java,
                DefaultUserSitemapSorters::class.java,
                DefaultViewChangeRule::class.java,
                DefaultInvalidURIHandler::class.java,
                DefaultComponentIdGenerator::class.java,

                DefaultResourceUtils::class.java,
                DefaultClassNameUtils::class.java,
                DefaultApplicationConfiguration::class.java,
                DefaultMasterSitemapQueue::class.java,
                DefaultSitemapFinisher::class.java,
                DefaultApplicationConfigurationService::class.java,
                DefaultSitemapService::class.java,
                DefaultNavigator::class.java
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