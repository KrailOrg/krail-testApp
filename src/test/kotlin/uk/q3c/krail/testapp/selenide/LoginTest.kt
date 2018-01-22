package uk.q3c.krail.testapp.selenide

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


}