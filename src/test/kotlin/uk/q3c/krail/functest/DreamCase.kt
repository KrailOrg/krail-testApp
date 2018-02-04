package uk.q3c.krail.functest

import org.junit.Test
import uk.q3c.krail.core.view.DefaultLoginView
import uk.q3c.krail.functest.objects.LoginViewObject
import uk.q3c.krail.functest.selenide.SelenideBrowser


/**
 * Created by David Sowerby on 25 Jan 2018
 */
class DreamCase {


    @Test
    fun doit() {
//        executionMode=ExecutionMode.CODED
        executionMode = ExecutionMode.SELENIDE
//        browser = CodedBrowser()
        browser = SelenideBrowser()
        browser.setup()
        browser.navigateTo("login")
        browser.viewShouldBe(DefaultLoginView::class.java)

        val view = LoginViewObject()

        view.password.captionShouldBe("Password")
        view.password.setValue("password")
        view.password.valueShouldBe("password")
        view.submit.click()





    }


}






