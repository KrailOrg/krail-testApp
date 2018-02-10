package uk.q3c.krail.testapp

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.core.view.DefaultLogoutView
import uk.q3c.krail.functest.*


/**
 * Created by David Sowerby on 01 Feb 2018
 */
class LogInOutFunctionalTest : Spek({


    given("Browser selection") {
        executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED

        createBrowser()

        given("navigateTo login page") {

            val page = TestAppUIObject()
            page.userStatus.login_logout_Button.click()
            browser.fragmentShouldBe("login")
            val view = DefaultLoginViewObject()

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

            on("entering invalid credentials") {
                view.username.setValue("ds")
                view.password.setValue("rubbish")
                view.submit.click()
                it("shows failure message") {
                    view.statusMsgLabel.valueShouldBe("That username or password was not recognised")
                }
            }

            on("entering valid credentials") {
                view.username.setValue("ds")
                view.password.setValue("password")
                view.submit.click()


                it("navigates to private home page") {
                    browser.fragmentShouldBe("home")
                }

                it("shows users status as user name, and 'log out' on button") {
                    page.userStatus.usernameLabel.valueShouldBe("ds")
                    page.userStatus.login_logout_Button.captionShouldBe("log out")
                }

            }

            on("logout") {

                page.userStatus.login_logout_Button.click()


                it("goes to logout page, and changes labels in user status") {
                    browser.fragmentShouldBe("logout")
                    page.userStatus.usernameLabel.valueShouldBe("Guest")
                    page.userStatus.login_logout_Button.captionShouldBe("log in")
                }
            }

        }
    }
})
//on("entering invalid credentials") {
//    it("shows failure message") {
//        view.statusMsgLabel.valueShouldBe("That username or password was not recognised")
//    }
//}


class LogoutBackSecurity : Spek({
    given("Browser selection") {
        executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED
        createBrowser()
        val page = TestAppUIObject()



        on("login, move to private page, logout and try to navigate back to private page") {
            login(page)
            page.menu.select("Private/Finance/Accounts")
            browser.fragmentShouldBe("private/finance/accounts")
            page.userStatus.login_logout_Button.click()
            browser.fragmentShouldBe("logout")
            browser.back()

            it("does not go to private page, notification is shown") {

                when (executionMode) {
                    ExecutionMode.SELENIDE -> browser.fragmentShouldBe("private/finance/accounts") // url stays the same in a real browser
                    ExecutionMode.CODED -> browser.fragmentShouldBe("logout") // but not in CodedBrowser
                }
                browser.viewShouldBe(DefaultLogoutView::class.java)
                notificationShouldBeVisible(NotificationLevel.INFORMATION, "private/finance/accounts is not a valid page")

            }
        }
    }
})

fun login(page: TestAppUIObject) {
    page.userStatus.login_logout_Button.click()
    browser.fragmentShouldBe("login")
    val view = DefaultLoginViewObject()
    view.username.setValue("ds")
    view.password.setValue("password")
    view.submit.click()
}