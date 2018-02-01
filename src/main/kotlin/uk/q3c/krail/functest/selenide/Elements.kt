package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Selenide.`$`
import org.openqa.selenium.By
import uk.q3c.krail.functest.BaseElement
import uk.q3c.krail.functest.TextFieldElement
import uk.q3c.krail.functest.browser

/**
 * Created by David Sowerby on 30 Jan 2018
 */

class SelenideTextFieldElement(id: String) : TextFieldElement, BaseSelenideElement(id) {


    override fun valueShouldBe(expectedValue: String) {
        TODO()
    }
}


abstract class BaseSelenideElement(final override val id: String) : BaseElement {
    val viewElement: SelenideViewElement = browser.view as SelenideViewElement
    val fullId = "#${viewElement.id}-$id"

    override fun captionShouldBe(expectedCaption: String) {
        `$`(fullId).`$x`("..").`$`(By.className("v-captiontext")).shouldHave(exactText(expectedCaption))

    }
}