package uk.q3c.krail.testapp

import com.codeborne.selenide.Selenide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode
import uk.q3c.krail.testapp.view.LocaleChanger

/**
 * Created by David Sowerby on 10 Feb 2018
 */
class LocaleAndValidationTest : Spek({
    given("we want to test something") {
        beforeGroup {
            executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED
            createBrowser()
        }
        afterGroup {
            if (executionMode == ExecutionMode.SELENIDE) Selenide.close()
        }

        on("opening the page") {
            browser.clickOnNavigationButton("locale")

            it("should default locale to Locale.UK") {
                browser.viewShouldBe(LocaleChanger::class.java)
                browser.fragmentShouldBe("locale")
                val view = LocaleChangerObject()
                view.changeToUK.click()
                view.currentLocale.valueShouldBe("en-GB")
            }
        }

        on("going to validated page") {
            browser.back()
            browser.clickOnNavigationButton("form")
            browser.fragmentShouldBe("form")
            val view = ManualFormObject()

            view.validateButton.click()

            it("should show validation no error message") {
                view.validationMsg.valueShouldBe("No errors")
            }
        }


        on("entering invalid data") {
            browser.fragmentShouldBe("form")
            val view = ManualFormObject()
            view.ageField.sendBackspace(2)
            view.ageField.sendValue("13")
            view.validateButton.click()

            it("should show validation error message") {
                view.validationMsg.valueShouldBe("must be less than or equal to 12")
            }
        }

        on("changing language") {
            browser.navigateTo("locale")
            val view = LocaleChangerObject()
            view.changeToGerman.click()

            it("should change translated elements to German") {
                view.currentLocale.valueShouldBe("de-DE")
                val page = SimpleUIObject()
                page.titleLabel.valueShouldBe("unbenannt")

            }
        }


        on("checking validation message") {
            val page = SimpleUIObject()
            page.homeButton.click()
            browser.clickOnNavigationButton("form")
            val view = ManualFormObject()
            view.ageField.sendBackspace(2)
            view.ageField.sendValue("13")
            view.validateButton.click()

            it("should show validation error message in German") {
                view.validationMsg.valueShouldBe("muss kleiner oder gleich 12 sein")
            }
        }


    }
})
