package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Condition.exactTextCaseSensitive
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide.`$`
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.openqa.selenium.By
import uk.q3c.krail.functest.*

/**
 * Created by David Sowerby on 30 Jan 2018
 */

abstract class AbstractSelenideElement(final override val id: String) : BaseElement {
    val fullId = "#$id"

    override fun captionShouldBe(expectedCaption: String) {
        `$`(fullId).`$x`("..").`$`(By.className("v-captiontext")).shouldHave(exactTextCaseSensitive(expectedCaption))

    }
}

class SelenideGridElement(id: String) : GridElement, AbstractSelenideElement(id)

class SelenideTreeGridElement(id: String) : TreeGridElement, AbstractSelenideElement(id)

class SelenideTextFieldElement(id: String) : TextFieldElement, AbstractSelenideElement(id) {
    override fun setValue(value: String) {
        `$`(fullId).value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        `$`(fullId).value.shouldBeEqualTo(expectedValue)
    }
}

class SelenideCheckBoxElement(id: String) : CheckBoxElement, AbstractSelenideElement(id) {
    override fun setValue(value: Boolean) {
        `$`(fullId).value = value.toString()
    }


    override fun valueShouldBe(expectedValue: Boolean) {
        `$`(fullId).value.shouldBe(expectedValue)
    }
}

class SelenideTextAreaElement(id: String) : TextAreaElement, AbstractSelenideElement(id) {
    override fun setValue(value: String) {
        `$`(fullId).value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        `$`(fullId).value.shouldBeEqualTo(expectedValue)
    }
}

class SelenideLabelElement(id: String) : LabelElement, AbstractSelenideElement(id) {
    override fun setValue(value: String) {
        `$`(fullId).value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        `$`(fullId).value.shouldBeEqualTo(expectedValue)
    }
}


class SelenideButtonElement(id: String) : ButtonElement, AbstractSelenideElement(id) {
    override fun click() {
        `$`(fullId).shouldBe(visible).click()
    }

}