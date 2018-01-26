package uk.q3c.krail.testapp

import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Test
import uk.q3c.krail.core.view.LoginView
import uk.q3c.krail.functest.LabelElement
import uk.q3c.krail.functest.TextFieldElement
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.coded.CodedBrowser
import uk.q3c.krail.testapp.view.WidgetsetView


/**
 * Created by David Sowerby on 25 Jan 2018
 */
class DreamCase {


    @Test
    fun doit() {
        browser = CodedBrowser()
        browser.setup()
        browser.navigateTo("login")
        var currentView = (browser as CodedBrowser).view
        currentView.shouldBeInstanceOf<LoginView>()

        browser.navigateTo("widgetset")
        currentView = (browser as CodedBrowser).view
        currentView.shouldBeInstanceOf<WidgetsetView>()

//        page.ui.statusPanel.label.valueShouldBe("ds")

//        page.view.username.value = "ds"

    }
}


fun TextField.e(): TextFieldElement {
    return browser.element(this)
}

fun Label.e(): LabelElement {
    return browser.element(this)
}




