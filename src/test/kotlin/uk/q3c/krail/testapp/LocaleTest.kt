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
            browser.fragmentShouldBe("home")
            val page = TestAppUIObject()

            it("should default locale to Locale.UK") {
                page.menu.select("Locale")
                browser.viewShouldBe(LocaleChanger::class.java)
                browser.fragmentShouldBe("locale")
                val view = LocaleChangerObject()
                view.currentLocale.valueShouldBe("en-GB")
                page.userStatus.usernameLabel.valueShouldBe("Guest")
            }
        }

        on("going to validated page") {
            val page = TestAppUIObject()
            page.menu.select("Form")
            browser.fragmentShouldBe("form")
            val view = AutoFormObject()

            view.validateButton.click()

            it("should show validation no error message") {
                view.validationMsg.valueShouldBe("No errors")
            }
        }


        on("entering invalid data") {
            val page = TestAppUIObject()
            page.menu.select("Form")
            browser.fragmentShouldBe("form")
            val view = AutoFormObject()
            view.ageField.sendBackspace(2)
            view.ageField.sendValue("13")
            view.validateButton.click()

            it("should show validation error message") {
                view.validationMsg.valueShouldBe("must be less than or equal to 12")
            }
        }

        on("changing language") {
            val page = TestAppUIObject()
            page.menu.select("Locale")
            browser.fragmentShouldBe("locale")
            val view = LocaleChangerObject()
            view.changeToGerman.click()

            it("should change translated elements to German") {
                view.currentLocale.valueShouldBe("de-DE")
                page.userStatus.usernameLabel.valueShouldBe("Gast")
            }
        }

        on("checking validation message") {
            val page = TestAppUIObject()
            page.menu.select("Form")
            browser.fragmentShouldBe("form")
            val view = AutoFormObject()
            view.ageField.sendBackspace(2)
            view.ageField.sendValue("13")
            view.validateButton.click()

            it("should show validation error message in German") {
                view.validationMsg.valueShouldBe("muss kleiner oder gleich 12 sein")
            }
        }


    }
})
