package uk.q3c.krail.testapp

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import org.junit.Test
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode

/**
 * Created by David Sowerby on 06 Feb 2018
 */
class NavTest {


    @Test
    fun doit() {
        // given("Browser selection") {
        System.setProperty("selenide.browser", "chrome")
        System.setProperty("selenide.baseUrl", "http://localhost:8080/krail-testapp/#")
        executionMode = ExecutionMode.SELENIDE
        createBrowser()

        val page = TestAppUIObject()
        val menuBar = Selenide.`$`("#TestAppUI-menu")
        menuBar.`is`(visible)
//        val loginElement = menuBar.`$`(Selectors.ByText("Log In")).`$x`("..")
//        loginElement.click()

//        page.userStatus.login_logout_Button.click()
//        Selenide.open("login")
        login(page)

        // on("login, move to private page, logout and try to navigate back to private page") {
//        page.userStatus.login_logout_Button.click()
//        view.username.setValue("ds")
//        view.password.setValue("password")
//        view.submit.click()

//            Selenide.open("http://localhost:8080/krail-testapp/#jpa")
//                browser.navigateTo("jpa")

//        page.userStatus.login_logout_Button.click()
//        browser.back()
//
//        //   it("does not go to private page, notification is shown") {
//        browser.fragmentShouldNotBe("jpa")
//        browser.fragmentShouldBe("logout")
        // }
    }//
//}
//}
}

fun login(page: TestAppUIObject) {
    page.userStatus.login_logout_Button.click()
    browser.fragmentShouldBe("login")
    val view = DefaultLoginViewObject()
    view.username.setValue("ds")
    view.password.setValue("password")
    view.submit.click()
    browser.fragmentShouldBe("home")
}