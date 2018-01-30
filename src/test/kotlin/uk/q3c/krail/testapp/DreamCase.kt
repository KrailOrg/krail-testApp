package uk.q3c.krail.testapp

import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Test
import uk.q3c.krail.core.view.DefaultLoginView
import uk.q3c.krail.functest.LabelElement
import uk.q3c.krail.functest.TextFieldElement
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.objects.CodedTextFieldElement2
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

//        browser.view.e["password"].captionShouldBe("padsword")
//
//        with(browser.view as LoginView) {
//            password.e().captionShouldBe("Password")
//        }
//        browser.navigateTo("widgetset")
//        browser.fragmentShouldBe("widgetset")
//
//        println("=================>> ${browser.currentUrl()}")
//
//
//        browser.view.shouldBeInstanceOf<WidgetsetView>()
//
//        println("=================>> ${browser.currentUrl()}")

//        page.ui.statusPanel.label.valueShouldBe("ds")

//        page.view.username.value = "ds"

    }

    @Test
    fun byMap() {
        val pp = LoginViewObject("DefaultLoginView")

        pp.password shouldBeInstanceOf (CodedTextFieldElement2::class.java)
        pp.password.captionShouldBe("wiggly")

    }
}




fun TextField.e(): TextFieldElement {
    return browser.element(this)
}

fun Label.e(): LabelElement {
    return browser.element(this)
}




