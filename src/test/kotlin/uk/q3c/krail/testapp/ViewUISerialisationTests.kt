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
import uk.q3c.krail.core.env.ServletEnvironmentModule
import uk.q3c.krail.core.ui.BootstrapUI
import uk.q3c.krail.core.ui.DefaultApplicationUI
import uk.q3c.krail.core.ui.ScopedUIProvider
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.functest.coded.CodedBrowser
import uk.q3c.krail.testapp.view.TestAppBindingsCollator
import uk.q3c.util.serial.tracer.SerializationTracer
import java.io.Serializable
import java.lang.reflect.Modifier

/**
 * Created by David Sowerby on 13 Mar 2018
 */


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
        val uiClasses: List<Class<out Serializable>> = listOf(
                DefaultApplicationUI::class.java,
                TestAppUI::class.java,
                BootstrapUI::class.java
        )

        uiClasses.forEach { classUnderTest ->
            given(classUnderTest.simpleName) {

                on("doing it") {
                    println(">>>>> ${classUnderTest.simpleName}")
                    val instance = injector.getInstance(classUnderTest)
                    val tracer = SerializationTracer()


                    it("does not throw exception") {
                        tracer.shouldNotHaveAnyFailures()
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