package uk.q3c.krail.testapp

import com.codeborne.selenide.Selenide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.vaadin.spinkit.shared.SpinnerType
import uk.q3c.krail.app.widgetset
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode
import uk.q3c.krail.testapp.view.WidgetsetView

object WidgetsetTest : Spek({


    given("an app with a spinner component") {

        beforeGroup {
            executionMode = ExecutionMode.SELENIDE
            createBrowser()
        }
        afterGroup {
            if (executionMode == ExecutionMode.SELENIDE) Selenide.close()
        }

        on("entering page") {

            browser.navigateTo(widgetset)
            browser.viewShouldBe(WidgetsetView::class.java)

            val view = WidgetsetViewObject()

            it("should have visible spinner") {
                view.spinner.shouldBeVisible()
                view.spinner.shouldBeOfType(SpinnerType.FOLDING_CUBE)
            }
        }

        on("click on change spinner type button") {
            browser.navigateTo(widgetset)
            val view = WidgetsetViewObject()
            view.changeSpinnerType.click()

            it("should change the spinner type") {
                view.spinner.shouldBeVisible()
                view.spinner.shouldBeOfType(SpinnerType.WAVE)
            }

        }


    }
})
