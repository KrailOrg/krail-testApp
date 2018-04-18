package uk.q3c.krail.testapp

import com.google.inject.Guice
import com.vaadin.server.VaadinSession
import com.vaadin.ui.UI
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
import uk.q3c.krail.core.guice.uiscope.UIScope
import uk.q3c.krail.core.i18n.VaadinCurrentLocale
import uk.q3c.krail.core.navigate.DefaultInvalidURIHandler
import uk.q3c.krail.core.navigate.DefaultLoginNavigationRule
import uk.q3c.krail.core.navigate.DefaultLogoutNavigationRule
import uk.q3c.krail.core.navigate.DefaultNavigator
import uk.q3c.krail.core.navigate.DefaultViewChangeRule
import uk.q3c.krail.core.navigate.StrictURIFragmentHandler
import uk.q3c.krail.core.navigate.sitemap.DefaultMasterSitemap
import uk.q3c.krail.core.navigate.sitemap.DefaultSitemapFinisher
import uk.q3c.krail.core.navigate.sitemap.DefaultSitemapService
import uk.q3c.krail.core.navigate.sitemap.DefaultUserSitemap
import uk.q3c.krail.core.navigate.sitemap.UserSitemapBuilder
import uk.q3c.krail.core.navigate.sitemap.UserSitemapNodeModifier
import uk.q3c.krail.core.navigate.sitemap.comparator.DefaultUserSitemapSorters
import uk.q3c.krail.core.option.DefaultOptionPopup
import uk.q3c.krail.core.option.KrailOptionPermissionVerifier
import uk.q3c.krail.core.push.DefaultBroadcaster
import uk.q3c.krail.core.push.DefaultPushMessageRouter
import uk.q3c.krail.core.shiro.DefaultJWTKeyProvider
import uk.q3c.krail.core.shiro.DefaultJWTProvider
import uk.q3c.krail.core.shiro.DefaultSubjectProvider
import uk.q3c.krail.core.shiro.PageAccessController
import uk.q3c.krail.core.shiro.SubjectProvider
import uk.q3c.krail.core.ui.ApplicationTitle
import uk.q3c.krail.core.ui.BrowserProvider
import uk.q3c.krail.core.ui.DefaultApplicationUI
import uk.q3c.krail.core.ui.ScopedUIProvider
import uk.q3c.krail.core.user.notify.DefaultUserNotifier
import uk.q3c.krail.core.user.notify.DefaultVaadinNotification
import uk.q3c.krail.core.vaadin.DefaultConverterFactory
import uk.q3c.krail.core.vaadin.DefaultOptionBinder
import uk.q3c.krail.core.view.DefaultViewFactory
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.core.view.component.DefaultApplicationHeader
import uk.q3c.krail.core.view.component.DefaultApplicationLogo
import uk.q3c.krail.core.view.component.DefaultBreadcrumb
import uk.q3c.krail.core.view.component.DefaultComponentIdGenerator
import uk.q3c.krail.core.view.component.DefaultLocaleSelector
import uk.q3c.krail.core.view.component.DefaultMessageBar
import uk.q3c.krail.core.view.component.DefaultSubPagePanel
import uk.q3c.krail.core.view.component.DefaultUserNavigationMenu
import uk.q3c.krail.core.view.component.DefaultUserNavigationTree
import uk.q3c.krail.core.view.component.DefaultUserNavigationTreeBuilder
import uk.q3c.krail.core.view.component.DefaultUserStatusPanel
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
import uk.q3c.krail.testapp.ui.PointlessUI
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
        resetVaadin()
        val codedBrowser = CodedBrowser()
        codedBrowser.setup()
    }

    given("we want core classes to be serializable") {
        val injector = Guice.createInjector(TestAppBindingsCollator(ServletEnvironmentModule()).allModules())
        val coreClasses: List<Class<out Any>> = listOf(
                ApplicationTitle::class.java,
                BrowserProvider::class.java,
                DefaultApplicationConfiguration::class.java,
                DefaultApplicationConfigurationService::class.java,
                DefaultApplicationHeader::class.java,
                DefaultApplicationLogo::class.java,
                DefaultBreadcrumb::class.java,
                DefaultBroadcaster::class.java,
                DefaultClassNameUtils::class.java,
                DefaultComponentIdGenerator::class.java,
                DefaultConverterFactory::class.java,
                DefaultDataConverter::class.java,
                DefaultInMemoryOptionStore::class.java,
                DefaultInvalidURIHandler::class.java,
                DefaultJWTKeyProvider::class.java,
                DefaultJWTProvider::class.java,
                DefaultLocaleSelector::class.java,
                DefaultLoginNavigationRule::class.java,
                DefaultLogoutNavigationRule::class.java,
                DefaultMasterSitemap::class.java,
                DefaultMessageBar::class.java,
                DefaultMessageFormat::class.java,
                DefaultNavigator::class.java,
                DefaultOption::class.java,
                DefaultOptionBinder::class.java,
                DefaultOptionCache::class.java,
                DefaultOptionCacheProvider::class.java,
                DefaultOptionDao::class.java,
                DefaultOptionPopup::class.java,
                DefaultOptionSource::class.java,
                DefaultPatternSource::class.java,
                DefaultPersistenceInfo::class.java,
                DefaultPushMessageRouter::class.java,
                DefaultResourceUtils::class.java,
                DefaultSessionBusProvider::class.java,
                DefaultSitemapFinisher::class.java,
                DefaultSitemapService::class.java,
                DefaultSubjectProvider::class.java,
                DefaultSubPagePanel::class.java,
                DefaultTranslate::class.java,
                DefaultUIBusProvider::class.java,
                DefaultUserNavigationMenu::class.java,
                DefaultUserNavigationTree::class.java,
                DefaultUserNavigationTreeBuilder::class.java,
                DefaultUserNotifier::class.java,
                DefaultUserNotifier::class.java,
                DefaultUserSitemap::class.java,
                DefaultUserSitemapSorters::class.java,
                DefaultUserStatusPanel::class.java,
                DefaultVaadinNotification::class.java,
                DefaultViewChangeRule::class.java,
                DefaultViewFactory::class.java,
                EasyBinder::class.java,
                KrailOptionPermissionVerifier::class.java,
                OptionKeyLocator::class.java,
                PageAccessController::class.java,
                ScopedUIProvider::class.java,
                StrictURIFragmentHandler::class.java,
                SubjectProvider::class.java,
                UIScope::class.java,
                UserSitemapBuilder::class.java,
                UserSitemapNodeModifier::class.java,
                VaadinCurrentLocale::class.java
        )

        coreClasses.forEach { test ->
            given(test.simpleName) {

                on("doing it") {
                    println(">>>>> ${test.simpleName}")
                    val instance = injector.getInstance(test)
                    val output = SerializationUtils.serialize(instance as Serializable)
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
        resetVaadin()
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

object UIClassesSerialisationTest : Spek({

    beforeGroup {
        resetVaadin()
        val codedBrowser = CodedBrowser()
        codedBrowser.setup()
    }

    given("we want core classes to be serializable") {
        val injector = Guice.createInjector(TestAppBindingsCollator(ServletEnvironmentModule()).allModules())
        val coreClasses: List<Class<out Serializable>> = listOf(
                DefaultApplicationUI::class.java,
                TestAppUI::class.java,
                PointlessUI::class.java
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

fun listViews(): List<Class<out KrailView>> {
    val injector = Guice.createInjector(TestAppBindingsCollator(ServletEnvironmentModule()).allModules())
    val scopedUIProvider = injector.getInstance(ScopedUIProvider::class.java)
    scopedUIProvider.get()
    val reflections = Reflections("uk.q3c")
    val classes = reflections.getSubTypesOf(KrailView::class.java).filter { clazz -> !Modifier.isAbstract(clazz.modifiers) }
    return classes
}

fun resetVaadin() {
    VaadinSession.setCurrent(null)
    UI.setCurrent(null)
}