package uk.q3c.krail.testapp

import com.codeborne.selenide.Selenide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.core.view.DefaultLogoutView
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.NotificationLevel
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode
import uk.q3c.krail.functest.notificationShouldBeVisible


object LogoutBackSecurity : Spek({

    given("user may try to navigate back to a page after logging out") {
        beforeGroup {
            executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED
            createBrowser()

        }
        afterGroup {
            if (executionMode == ExecutionMode.SELENIDE) Selenide.close()
        }

        on("login, move to private page, logout and try to navigate back to private page") {
            val page = SimpleUIObject()
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

fun login(page: SimpleUIObject) {
    page.userStatus.login_logout_Button.click()
    browser.fragmentShouldBe("login")
    val view = DefaultLoginViewObject()
    view.username.setValue("ds")
    view.password.setValue("password")
    view.submit.click()
}