package uk.q3c.krail.testapp

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Injector
import com.google.inject.Module
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import org.junit.Test
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.core.view.LoginView
import uk.q3c.krail.functest.LabelElement
import uk.q3c.krail.functest.TextFieldElement
import uk.q3c.krail.functest.browser

/**
 * Created by David Sowerby on 25 Jan 2018
 */
class DreamCase {
    val injector: Injector = FunctionalTestBindingManager().injector


    @Test
    fun doit() {
        val page = injector.getInstance(LoginPage2::class.java)
        page.view.username.e().captionShouldBe("username")
        page.view.username.e().valueShouldBe("")
        page.view.submit.click()

//        page.ui.statusPanel.label.valueShouldBe("ds")

        page.view.username.value = "ds"

    }
}

class FunctionalTestBindingManager : TestAppBindingManager() {
    override fun addUtilModules(coreModules: MutableList<Module>?) {
        super.addUtilModules(coreModules)
        if (coreModules != null) {
            coreModules.add(PageObjectModule())
        }
    }
}

class PageObjectModule : AbstractModule() {
    override fun configure() {
        bind(LoginPage2::class.java)
    }
}


fun TextField.e(): TextFieldElement {
    return browser.element(this)
}

fun Label.e(): LabelElement {
    return browser.element(this)
}


class LoginPage2 @Inject constructor(loginView: LoginView) : Page2<LoginView>("login", loginView)

abstract class Page2<out T : KrailView>(val urlFragment: String, val view: T) : PageObj2<T> {

    override fun open(): Page2<T> {
        Selenide.open(urlFragment)
        shouldBeOpen()
        return this
    }

    override fun openWithParams(params: String): Page2<T> {
        Selenide.open("$urlFragment/$params")
        shouldBeOpen()
        return this
    }


    override fun shouldBeOpen() {
        Selenide.`$`(view.javaClass.simpleName).`is`(Condition.visible)
        // ignore any params
        browser.currentFragmentShouldBe(urlFragment)
    }

    override fun shouldBeOpenWithUrl(otherFragment: String) {
        Selenide.`$`(view.javaClass.simpleName).`is`(Condition.visible)
        browser.currentFragmentShouldBe(otherFragment)
    }


    fun currentUrl(): String {
        val fullUrl = WebDriverRunner.getWebDriver().currentUrl
        if (fullUrl == null) {
            return ""
        } else {
            return fullUrl.removePrefix(Configuration.baseUrl)
        }
    }


}

interface PageObj2<out T : KrailView> {
    fun open(): Page2<T>
    fun openWithParams(params: String = ""): Page2<T>
    fun shouldBeOpen()
//    fun shouldNotBeOpen()

    /**
     * The page is open but with a different fragment to normal - this happens with failed navigation (where a user does not have permission)
     */
    fun shouldBeOpenWithUrl(otherFragment: String)
}