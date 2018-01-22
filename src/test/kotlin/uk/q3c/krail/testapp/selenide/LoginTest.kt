package uk.q3c.krail.testapp.selenide

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selenide.`$`
import org.junit.Test

/**
 * Created by David Sowerby on 21 Jan 2018
 */

class LoginTest : SelenideTestCase() {


    @Test
    fun loginSuccessful() {

        // given
        val page = LoginPage()

        // when
        page.open()

        // then page is shown
        page.shouldBeOpen()

        // when
        page.username.value = "ds"
        page.password.setValue("password").pressEnter()

        // then
        PrivateHomePage().shouldBeOpen()

    }


    @Test
    fun loginFailed() {

        // given
        val page = LoginPage()

        // when
        page.open()

        // then page is shown
        page.shouldBeOpen()

        // when
        page.username.value = "ds"
        page.password.setValue("rubbish").pressEnter()

        // then
        page.shouldBeOpen()
        `$`(byText("That username or password was not recognised")).shouldBe(Condition.visible)
    }

    @Test
    fun logoutFromPublicPage() {
        // given
        val page = HomePage().open()
        page.loginIfLoggedOut()

        // when
        page.logoutIfLoggedIn()

        //then
        LogoutPage().shouldBeOpen()
    }

    @Test
    fun logoutFromPrivatePage() {
        // given
        HomePage().open().loginIfLoggedOut()
        val privateHomePage = PrivateHomePage().open()

        // when
        privateHomePage.logoutIfLoggedIn()

        //then
        LogoutPage().shouldBeOpen()

    }

    @Test
    fun logoutFromPrivatePageAndTryToGoBack() {
        // given
        HomePage().open().loginIfLoggedOut()
        val privateHomePage = PrivateHomePage().open()
        privateHomePage.logoutIfLoggedIn()

        // when
        browserBack()

        // then notification shown
        `$`(byText("private/home is not a valid page")).shouldBe(Condition.visible)
        `$`(Selectors.byClassName("v-Notification")).shouldBe(Condition.visible)

        // and page not moved
        LogoutPage().shouldBeOpenWithUrl("private/home")

    }
}