package uk.q3c.krail.testapp.selenide

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.open
import com.codeborne.selenide.SelenideElement
import org.junit.Before
import org.slf4j.LoggerFactory

/**
 * Created by David Sowerby on 18 Jan 2018
 */
open class SelenideTestCase {
    private val log = LoggerFactory.getLogger(this.javaClass.name)

    @Before
    fun setup() {
        System.setProperty("selenide.browser", "chrome")
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
    val baseUrl = "http://localhost:8080/krail-testapp/#"


    override fun open() {
        open("$baseUrl$relativeUrl")
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
