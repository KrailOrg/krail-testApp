package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.functest.Browser
import uk.q3c.krail.functest.LabelElement
import uk.q3c.krail.functest.TextFieldElement
import uk.q3c.krail.functest.waitForNavigationState


class SelenideBrowser : Browser {
    override lateinit var view: KrailView
    override fun navigateTo(fragment: String) {
        TODO()
    }

    override fun setup() {
        TODO()
    }

    override fun element(label: Label): LabelElement {
        return SelenideLabelElement(label)
    }

    override fun element(textField: TextField): TextFieldElement {
        return SelenideTextFieldElement(textField)
    }


    override fun back() {
        Selenide.back()
    }


    override fun fragmentShouldBe(desiredFragment: String) {
        waitForNavigationState({ currentUrl() }, { navState -> desiredFragment == navState.fragment })
    }

    override fun fragmentShouldNotBe(desiredFragment: String) {
        waitForNavigationState({ currentUrl() }, { navState -> desiredFragment != navState.fragment })
    }


    override fun currentUrl(): String {
        return WebDriverRunner.getWebDriver().currentUrl
    }
}

