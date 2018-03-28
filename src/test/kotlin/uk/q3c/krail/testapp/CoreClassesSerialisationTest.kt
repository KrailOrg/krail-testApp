package uk.q3c.krail.testapp

import com.google.inject.Guice
import org.apache.commons.lang3.SerializationUtils
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.reflections.Reflections
import uk.q3c.krail.core.guice.ServletEnvironmentModule
import uk.q3c.krail.core.i18n.VaadinCurrentLocale
import uk.q3c.krail.core.option.DefaultOptionPopup
import uk.q3c.krail.core.shiro.DefaultJWTKeyProvider
import uk.q3c.krail.core.shiro.DefaultJWTProvider
import uk.q3c.krail.core.shiro.SubjectProvider
import uk.q3c.krail.core.ui.BrowserProvider
import uk.q3c.krail.core.ui.ScopedUIProvider
import uk.q3c.krail.core.user.notify.DefaultUserNotifier
import uk.q3c.krail.core.vaadin.DefaultConverterFactory
import uk.q3c.krail.core.vaadin.DefaultOptionBinder
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.functest.coded.CodedBrowser
import uk.q3c.krail.i18n.persist.source.DefaultPatternSource
import uk.q3c.krail.i18n.translate.DefaultTranslate
import uk.q3c.krail.option.option.DefaultOption
import uk.q3c.krail.option.option.OptionKeyLocator
import uk.q3c.krail.option.persist.cache.DefaultOptionCache
import uk.q3c.krail.option.persist.cache.DefaultOptionCacheProvider
import uk.q3c.krail.option.persist.dao.DefaultOptionDao
import uk.q3c.krail.testapp.view.TestAppBindingsCollator
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
                DefaultOptionDao::class.java,
                DefaultOptionCacheProvider::class.java,
                DefaultOptionCache::class.java,
                DefaultOption::class.java,
                DefaultOptionBinder::class.java,
                DefaultOptionPopup::class.java)

        coreClasses.forEach { test ->
            given(test.simpleName) {

                on("doing it") {
                    println(">>>>> ${test.simpleName}")
                    val instance = injector.getInstance(test)
                    val output = SerializationUtils.serialize(instance)
//                    val result: Serializable = SerializationUtils.deserialize(output)

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
//                    val result: Serializable = SerializationUtils.deserialize(output)

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