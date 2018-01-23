package uk.q3c.krail.testapp.selenide

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration.baseUrl
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebDriverRunner
import org.junit.Before
import org.slf4j.LoggerFactory
import uk.q3c.krail.core.view.component.DefaultUserStatusPanel
import uk.q3c.util.clazz.DefaultClassNameUtils
import java.util.*

/**
 * Created by David Sowerby on 18 Jan 2018
 */
open class SelenideTestCase {
    private val log = LoggerFactory.getLogger(this.javaClass.name)
    private val baseUrl = "http://localhost:8080/krail-testapp/#"

    @Before
    fun setup() {
//        Selenide.navigator
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


//    fun navigateTo(url: String) {
//        log.debug("Navigating to: {}", url)
//        open(url)
//    }


}

var browser: Browser = SelenideBrowser()

interface PageObj {
    fun open(): Page
    fun openWithParams(params: String = ""): Page
    fun shouldBeOpen()
//    fun shouldNotBeOpen()

    /**
     * The page is open but with a different fragment to normal - this happens with failed navigation (where a user does not have permission)
     */
    fun shouldBeOpenWithUrl(otherFragment: String)
}


abstract class Page(val urlFragment: String, val verificationId: Class<*>) : PageObj {

    /**
     * Opens the [urlFragment], and waits for the [verificationId] to be visible - or times out
     * @param verificationId is the class used to form the component id.  By convention that is a [KrailView].  It is used
     * to confirm that the view has opened.
     */
    override fun open(): Page {
        open(urlFragment)
        shouldBeOpen()
        return this
    }

    override fun openWithParams(params: String): Page {
        open("$urlFragment/$params")
        shouldBeOpen()
        return this
    }

    fun gridById(id: String): GridElement {
        return GridElement(`$`(id))
    }

    fun buttonById(id: String): ButtonElement {
        return ButtonElement(`$`(id))
    }

    fun buttonById(qualifier: Optional<*>, vararg componentClasses: Class<*>): ButtonElement {
        return ButtonElement(`$`(idc(qualifier, *componentClasses)))
    }

    fun labelById(qualifier: Optional<*>, vararg componentClasses: Class<*>): LabelElement {
        return LabelElement(`$`(idc(qualifier, *componentClasses)))
    }

    override fun shouldBeOpen() {
        `$`(verificationId.simpleName).`is`(Condition.visible)
        // ignore any params
        browser.currentFragmentShouldBe(urlFragment)
    }

    override fun shouldBeOpenWithUrl(otherFragment: String) {
        `$`(verificationId.simpleName).`is`(Condition.visible)
        browser.currentFragmentShouldBe(otherFragment)
    }


    fun currentUrl(): String {
        val fullUrl = WebDriverRunner.getWebDriver().currentUrl
        if (fullUrl == null) {
            return ""
        } else {
            return fullUrl.removePrefix(baseUrl)
        }
    }

    private val loginOutButton: ButtonElement
        get() {
            val qualifier: Optional<String> = Optional.empty()
            return ButtonElement(`$`(idc(qualifier, DefaultUserStatusPanel::class.java)))
        }
    val loggedIn: Boolean
        get () {
            return loginOutButton.caption.endsWith("log out") // selenideElement.innerText() contains the login name as well eg 'dslog out'
        }

    fun logoutIfLoggedIn() {
        if (loggedIn) {
            loginOutButton.click()
            LogoutPage().shouldBeOpen()
        }

    }

    fun loginIfLoggedOut() {
        if (!loggedIn) {
            val loginPage = LoginPage()
            with(loginPage) {
                open()
                username.value = "ds"
                password.setValue("password").pressEnter()
            }
            browser.currentFragmentShouldNotBe("login")
        }

    }


}

class ButtonElement(val selenideElement: SelenideElement) : SelenideElement by selenideElement {

    val caption: String
        get() {
            return selenideElement.innerText()
        }

}

class LabelElement(val selenideElement: SelenideElement) : SelenideElement by selenideElement {

    val caption: String
        get() {
            return selenideElement.innerText()
        }

    // use this for the
    val content: String
        get() {
            return caption
        }

}


class GridElement(val selenideElement: SelenideElement) : SelenideElement by selenideElement {

    val caption: String
        get() {
            return selenideElement.innerText()
        }

}

fun idc(qualifier: Optional<*>, vararg componentClasses: Class<*>): String {
    checkNotNull(qualifier)
    val buf = StringBuilder("#")
    var first = true
    val classNameUtil = DefaultClassNameUtils()
    for (c in componentClasses) {
        if (!first) {
            buf.append('-')
        } else {
            first = false
        }
        //https://github.com/davidsowerby/krail/issues/383
        //enhanced classes mess up the class name with $$Enhancer
        buf.append(classNameUtil.simpleClassNameEnhanceRemoved(c))
    }
    if (qualifier.isPresent) {
        buf.append('-')
        buf.append(qualifier.get())
    }
    return buf.toString()
}


