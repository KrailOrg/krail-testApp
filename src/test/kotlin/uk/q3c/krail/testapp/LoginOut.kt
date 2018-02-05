package uk.q3c.krail.testapp

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode

/**
 * Created by David Sowerby on 01 Feb 2018
 */
class LogInOutFunctionalTest : Spek({


    given("Browser selection") {
        executionMode = ExecutionMode.SELENIDE
        createBrowser()

        given("navigateTo login page") {
            browser.navigateTo("login")
            val view = DefaultLoginViewObject()
            val page = TestAppUIObject()

            on("page being opened") {
                it("displays correct captions") {
                    view.password.captionShouldBe("Password")
                    view.username.captionShouldBe("User Name")
                }

                it("shows users status as guest, and Log In on button") {
                    page.userStatus.usernameLabel.valueShouldBe("Guest")
                    page.userStatus.login_logout_Button.captionShouldBe("log in")
                }
            }

            on("entering valid credentials") {
                view.username.setValue("ds")
                view.password.setValue("password")
                view.submit.click()


                it("navigates to private home page") {
                    if (executionMode == ExecutionMode.SELENIDE) {
                        browser.fragmentShouldBe("private/home")
                    } else {
                        browser.fragmentShouldBe("home") // TODO https://github.com/davidsowerby/krail-functest/issues/2
                    }
                }

                it("shows users status as user name, and 'log out' on button") {
                    page.userStatus.usernameLabel.valueShouldBe("ds")
                    page.userStatus.login_logout_Button.captionShouldBe("log out")
                }

            }


        }
    }
})