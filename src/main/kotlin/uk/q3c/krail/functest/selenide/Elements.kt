package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Condition.exactTextCaseSensitive
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selenide.`$$`
import com.codeborne.selenide.Selenide.`$`
import com.google.common.base.Splitter
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.openqa.selenium.By
import org.openqa.selenium.InvalidArgumentException
import org.openqa.selenium.NoSuchElementException
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

/**
 * Selects from the menu using a 'path' constructed from the menu item captions, for example:
 *
 * Private/Finance/Accounts
 *
 * Remember this uses the menu item caption, case must be correct
 */
class SelenideMenuBarElement(id: String) : MenuBarElement, AbstractSelenideElement(id) {
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

        // try .. catch did not work????

//        try {
        val firstLevelItem = menuBar.`$`(Selectors.ByText(iter.next())).`$x`("..")
        firstLevelItem.click()
//        } catch (e: Exception) {
//            throw NoSuchElementException("Could not find first level menu item '${segments.first()}'.  Remember this uses the menu item caption, case must be correct")
//        }

        // in browser html, the popup is separate from the first level element
        while (iter.hasNext()) {
            val segment = iter.next()
            val menuItems = `$$`(Selectors.byClassName("v-menubar-menuitem-caption"))
            val selectedItem = menuItems.find({ item -> item.text().equals(segment) })
            if (selectedItem != null) {
                selectedItem.click()
                // TODO we should be able to confirm that the correct page / view is ready, from the FunctionalTestSupport model, except that the menu labels are not the same as the fragment
                // it might be possible to extend the FTS though
            } else {
                throw NoSuchElementException("Wibbly BoO!")
            }
        }
    }
}

class SelenideImageElement(id: String) : ImageElement, AbstractSelenideElement(id)

class SelenideTreeElement(id: String) : TreeElement, AbstractSelenideElement(id)