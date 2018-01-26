package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.SelenideElement
import com.vaadin.data.HasValue
import com.vaadin.ui.*
import uk.q3c.krail.functest.*
import java.util.*


/**
 * Created by David Sowerby on 23 Jan 2018
 */
class SelenideButtonElement(button: Button) : AbstractSelenideElement(button), ButtonElement {
    override fun click() {
        TODO()
    }

}

class SelenideLabelElement(label: Label) : AbstractSelenideElement(label), LabelElement
class SelenideGridElement<T>(grid: Grid<T>) : AbstractSelenideElement(grid), GridElement

class SelenideTextFieldElement(textField: TextField) : SelenideValueElement<String>(textField), TextFieldElement

class SelenideTextBoxElement(textBox: TextArea) : SelenideValueElement<String>(textBox), TextAreaElement

@Suppress("UNCHECKED_CAST")
abstract class SelenideValueElement<T>(hasValue: HasValue<T>) : AbstractSelenideElement(hasValue as Component), ValueElement<T> {
    override fun requiredIndicatorShouldBeVisible() {
        TODO()
    }

    override fun requiredIndicatorShouldNotBeVisible() {
        TODO()
    }

    override fun valueShouldBe(expectedValue: T) {
        TODO()
    }
}

abstract class AbstractSelenideElement(val component: Component) : BaseElement {
    val element: SelenideElement = `$`(component.id)


    override fun shouldBeVisible() {
        element.shouldBe(Condition.visible)
    }

    override fun shouldNotBeVisible() {
        element.shouldNotBe(Condition.visible)
    }

    override fun captionShouldBe(expectedCaption: String?) {
        // TODO how to find the caption
        element.shouldBe(Condition.exactTextCaseSensitive(expectedCaption))
    }

    override fun descriptionShouldBe(expectedDescription: String) {
        TODO()
    }

    override fun localeShouldBe(expectedLocale: Locale) {
        TODO()
    }

    override fun primaryStyleNameShouldBe(expectedPrimaryStyleName: String) {
        TODO()
    }

    override fun styleNameShouldBe(expectedStyleName: String) {
        TODO()
    }

    override fun shouldBeEnabled() {
        TODO()
    }

    override fun shouldNotBeEnabled() {
        TODO()
    }
}