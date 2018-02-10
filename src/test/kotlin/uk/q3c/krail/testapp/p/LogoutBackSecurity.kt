package uk.q3c.krail.testapp.p

import com.codeborne.selenide.Selenide
import org.junit.After
import org.junit.Before
import org.junit.Test
import uk.q3c.krail.core.view.DefaultLogoutView
import uk.q3c.krail.functest.*


class LogoutBackSecurity {

    // given("user may try to navigate back to a page after logging out") {
//    companion object {
//
//        @BeforeClass
//        @JvmStatic
//        fun beforeClass() {
//
//            executionMode = ExecutionMode.SELENIDE
////        executionMode = ExecutionMode.CODED
//            createBrowser()
//
//        }
//    }

    @Before
    fun before() {
        executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED
        createBrowser()
        browser.navigateTo("home")
    }

    @After
    fun after() {
//        println("Sleeping after")
        Selenide.close()
    }

    @Test
    fun privateLoginBrowserBack() {
        // on("login, move to private page, logout and try to navigate back to private page") {
        val page = TestAppUIObject()
        login(page)
        page.menu.select("Private/Finance/Accounts")
        browser.fragmentShouldBe("private/finance/accounts")
        page.userStatus.login_logout_Button.click()
        browser.fragmentShouldBe("logout")
        browser.back()

//            it("does not go to private page, notification is shown") {

        when (executionMode) {
            ExecutionMode.SELENIDE -> browser.fragmentShouldBe("private/finance/accounts") // url stays the same in a real browser
            ExecutionMode.CODED -> browser.fragmentShouldBe("logout") // but not in CodedBrowser
        }
        notificationShouldBeVisible(NotificationLevel.INFORMATION, "private/finance/accounts is not a valid page")
        browser.viewShouldBe(DefaultLogoutView::class.java)


    }
}


fun login(page: TestAppUIObject) {
    page.userStatus.login_logout_Button.click()
    browser.fragmentShouldBe("login")
    val view = DefaultLoginViewObject()
    view.username.setValue("ds")
    view.password.setValue("password")
    view.submit.click()
}