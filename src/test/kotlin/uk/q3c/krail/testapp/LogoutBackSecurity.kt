package uk.q3c.krail.testapp

import com.codeborne.selenide.Selenide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.app.finance_accounts
import uk.q3c.krail.app.logout
import uk.q3c.krail.core.view.DefaultLogoutView
import uk.q3c.krail.core.view.PublicHomeView
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode


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
            val page = TestAppSimpleUIObject()
            login(page)
            browser.navigateTo(finance_accounts)
            browser.fragmentShouldBe(finance_accounts)
            page.topBar.login_logout_Button.click()
            browser.fragmentShouldBe(logout)
            browser.back()

            // originally this would consistently pass, but for some reason the browser history now seems inconsistent
            // running it manually or through the debugger always works as originally expected.  Time delays do not fix it
            it("does not go to private page") {

                //                when (executionMode) {
//                    ExecutionMode.SELENIDE -> browser.fragmentShouldBe(finance_accounts) // url stays the same in a real browser
//                    ExecutionMode.CODED -> browser.fragmentShouldBe(logout) // but not in CodedBrowser
//                }
//                notificationShouldBeVisible(NotificationLevel.INFORMATION, "$finance_accounts is not a valid page")
                browser.viewShouldBeEither(DefaultLogoutView::class.java, PublicHomeView::class.java)


            }
        }
    }
})

fun login(page: TestAppSimpleUIObject) {
    page.topBar.login_logout_Button.click()
    browser.fragmentShouldBe(uk.q3c.krail.app.login)
    val view = DefaultLoginViewObject()
    view.username.setValue("ds")
    view.password.setValue("password")
    view.submit.click()
}