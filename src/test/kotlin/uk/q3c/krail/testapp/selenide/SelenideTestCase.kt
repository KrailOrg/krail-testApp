package uk.q3c.krail.testapp.selenide

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebDriverRunner.getWebDriver
import org.junit.Before
import org.slf4j.LoggerFactory

/**
 * Created by David Sowerby on 18 Jan 2018
 */
open class SelenideTestCase {
    private val log = LoggerFactory.getLogger(this.javaClass.name)
    private val baseUrl = "http://localhost:8080/krail-testapp/#"

    @Before
    fun setup() {
        Selenide.navigator
        System.setProperty("selenide.browser", "chrome")
        System.setProperty("selenide.baseUrl", baseUrl)
    }

    fun browserBack() {
        back()
    }

    fun browserForward() {
        forward()
    }
// see if we can use xxxPage.open() instead
//    fun navigateTo(relativeUrl :String, verificationText : String = ""){
//        open(relativeUrl)
//        if (verificationText.isNotBlank()) {
//            `$`(Selectors.ByText(verificationText)).`is`(Condition.visible)
//        }
//    }

    fun currentUrl(): String {
        val fullUrl = getWebDriver().currentUrl
        if (fullUrl == null) {
            return ""
        } else {
            return fullUrl.removePrefix(baseUrl)
        }

    }

//    fun navigateTo(url: String) {
//        log.debug("Navigating to: {}", url)
//        open(url)
//    }
}

interface PageObj {
    fun open()
}


class Page(val relativeUrl: String, val verificationText: String) : PageObj {

    /**
     * Opens the relativeUrl, and waits for the [verificationText] to be visible - or times out
     * @param verificationText the text to look for to confirm that the page has opened.  No verification takes place if this is blank
     */
    override fun open() {
        open("$relativeUrl")
        if (verificationText.isNotBlank()) {
            `$`(Selectors.ByText(verificationText)).`is`(Condition.visible)
        }
    }

    fun gridById(id: String): GridElement {
        return GridElement(`$`(id))
    }

    fun buttonById(id: String): ButtonElement {
        return ButtonElement(`$`(id))
    }
}

class ButtonElement(val selenideElement: SelenideElement) : SelenideElement by selenideElement {

    val caption: String
        get() {
            return selenideElement.innerText()
        }

}

class GridElement(val selenideElement: SelenideElement) : SelenideElement by selenideElement {

    val caption: String
        get() {
            return selenideElement.innerText()
        }

}
