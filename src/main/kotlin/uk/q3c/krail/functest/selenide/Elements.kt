package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Condition.exactTextCaseSensitive
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide.`$`
import org.amshove.kluent.shouldBeEqualTo
import org.openqa.selenium.By
import uk.q3c.krail.functest.*

/**
 * Created by David Sowerby on 30 Jan 2018
 */

abstract class BaseSelenideElement(final override val id: String) : BaseElement {
    private val viewElement: SelenideViewElement = browser.view as SelenideViewElement
    val fullId = "#${viewElement.id}-$id"

    override fun captionShouldBe(expectedCaption: String) {
        `$`(fullId).`$x`("..").`$`(By.className("v-captiontext")).shouldHave(exactTextCaseSensitive(expectedCaption))

    }
}

class SelenideTextFieldElement(id: String) : TextFieldElement, BaseSelenideElement(id) {
    override fun setValue(value: String) {
        `$`(fullId).value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        `$`(fullId).value.shouldBeEqualTo(expectedValue)
    }
}

class SelenideTextAreaElement(id: String) : TextAreaElement, BaseSelenideElement(id) {
    override fun setValue(value: String) {
        `$`(fullId).value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        `$`(fullId).value.shouldBeEqualTo(expectedValue)
    }
}

class SelenideLabelElement(id: String) : LabelElement, BaseSelenideElement(id) {
    override fun setValue(value: String) {
        `$`(fullId).value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        `$`(fullId).value.shouldBeEqualTo(expectedValue)
    }
}


class SelenideButtonElement(id: String) : ButtonElement, BaseSelenideElement(id) {
    override fun click() {
        `$`(fullId).shouldBe(visible).click()
    }

}