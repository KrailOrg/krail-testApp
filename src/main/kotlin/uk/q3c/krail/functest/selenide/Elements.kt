package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Condition.exactTextCaseSensitive
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selenide.`$`
import com.google.common.base.Splitter
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.openqa.selenium.By
import org.openqa.selenium.InvalidArgumentException
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

    // ##checked
    override fun setValue(value: String) {
        `$`(fullId).value = value
    }

    // ##checked
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
    // ##checked
    override fun valueShouldBe(expectedValue: String) {
        `$`(fullId).text.shouldBeEqualTo(expectedValue)
    }
}


class SelenideButtonElement(id: String) : ButtonElement, AbstractSelenideElement(id) {
    // ##checked
    override fun captionShouldBe(expectedCaption: String) {
        `$`(fullId).`$x`("..").`$`(By.className("v-button-caption")).shouldHave(exactTextCaseSensitive(expectedCaption))

    }

    // ##checked
    override fun click() {
        `$`(fullId).shouldBe(visible).click()
    }

}

class SelenideMenuElement(id: String) : MenuElement, AbstractSelenideElement(id) {
    override fun select(path: String) {
        if (path.isBlank()) {
            throw InvalidArgumentException("Menu path cannot be blank")
        }
        val segments = Splitter.on("/").split(path)
        val menuBar = `$`(fullId)
        menuBar.`is`(visible)
        val iter = segments.iterator()
        //no need for hasNext() path cannot be blank
        // select the first level
        val firstLevelItem = menuBar.`$`(Selectors.ByText(iter.next())).`$x`("..")
        firstLevelItem.click()

        // in browser html, the popup is separate from the first level element
        while (iter.hasNext()) {
            val popup = `$`(Selectors.byClassName("v-menubar-popup"))
            popup.`$`(Selectors.ByText(iter.next()))
        }
        // TODO we should be able to confirm that the correct page / view is ready, from the FunctionalTestSupport model
    }
}