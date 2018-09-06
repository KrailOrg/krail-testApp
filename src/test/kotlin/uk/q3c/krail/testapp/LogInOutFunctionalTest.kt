package uk.q3c.krail.testapp

import com.codeborne.selenide.Selenide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.app.home
import uk.q3c.krail.app.login
import uk.q3c.krail.app.logout
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode


/**
 * Created by David Sowerby on 01 Feb 2018
 */
object LogInOutFunctionalTest : Spek({
    given("we want to test logging in and out") {
        beforeGroup {
            executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED
            createBrowser()
        }
        afterGroup {
            if (executionMode == ExecutionMode.SELENIDE) Selenide.close()
        }

        on("navigating to login page") {
            val page = TestAppSimpleUIObject()
            page.topBar.login_logout_Button.click()


            it("should be at login page") {
                browser.fragmentShouldBe(login)
            }

            it("displays correct captions") {
                val view = DefaultLoginViewObject()
                view.password.captionShouldBe("Password")
                view.username.captionShouldBe("User Name")
            }
        }

        on("entering invalid credentials") {
            val view = DefaultLoginViewObject()
            view.username.setValue("ds")
            view.password.setValue("rubbish")
            view.submit.click()
            it("shows failure message") {
                view.statusMessage.valueShouldBe("That username or password was not recognised")
            }
        }
        on("entering valid credentials") {
            val view = DefaultLoginViewObject()
            val page = TestAppSimpleUIObject()
            view.username.setValue("ds")
            view.password.setValue("password")
            view.submit.click()


            it("navigates to home page") {
                browser.fragmentShouldBe(home)
            }

        }
        on("logout") {
            val page = TestAppSimpleUIObject()
            page.topBar.login_logout_Button.click()


            it("goes to logout page") {
                browser.fragmentShouldBe(logout)
            }


        }
    }
})
//on("entering invalid credentials") {
//    it("shows failure message") {
//        view.statusMsgLabel.valueShouldBe("That username or password was not recognised")
//    }
//}


