package uk.q3c.krail.testapp

import org.junit.Test
import uk.q3c.krail.core.view.DefaultLoginView
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.objects.LoginViewObject
import uk.q3c.krail.functest.selenide.SelenideBrowser


/**
 * Created by David Sowerby on 25 Jan 2018
 */
class DreamCase {


    @Test
    fun doit() {
//        browser = CodedBrowser()
        browser = SelenideBrowser()
        browser.setup()
        browser.navigateTo("login")
        browser.viewShouldBe(DefaultLoginView::class.java)

        val view = browser.view as LoginViewObject

        view.password.captionShouldBe("wiggly")


    }


}






