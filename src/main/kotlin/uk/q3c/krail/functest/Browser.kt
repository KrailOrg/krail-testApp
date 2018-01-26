package uk.q3c.krail.functest

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import uk.q3c.krail.core.navigate.NavigationState
import uk.q3c.krail.core.navigate.StrictURIFragmentHandler
import uk.q3c.krail.functest.selenide.SelenideLabelElement
import uk.q3c.krail.functest.selenide.SelenideTextFieldElement
import uk.q3c.util.clazz.DefaultClassNameUtils
import java.net.URI
import java.time.LocalDateTime
import java.util.*

/**
 * Created by David Sowerby on 23 Jan 2018
 */
interface Browser {
    fun back()
    fun element(textField: TextField): TextFieldElement
    fun element(label: Label): LabelElement

    /**
     * Returns when the [desiredFragment] appears in the browser url, or throws an [AssertionError] on timeout
     */
    fun currentFragmentShouldBe(desiredFragment: String)

    fun currentFragmentShouldNotBe(desiredFragment: String)
    fun setup()
}

class SelenideBrowser : Browser {
    override fun setup() {
        TODO()
    }

    override fun element(label: Label): LabelElement {
        return SelenideLabelElement(label)
    }

    override fun element(textField: TextField): TextFieldElement {
        return SelenideTextFieldElement(textField)
    }


    override fun back() {
        Selenide.back()
    }


    override fun currentFragmentShouldBe(desiredFragment: String) {
        waitForNavigationState({ currentUrl() }, { navState -> desiredFragment == navState.fragment })
    }

    override fun currentFragmentShouldNotBe(desiredFragment: String) {
        waitForNavigationState({ currentUrl() }, { navState -> desiredFragment != navState.fragment })
    }

    /**
     * Waits for source to provide a url which contains the fragment [expectedCondition], or times out
     *
     * The 'fragment' is as defined by [java.net.URI.fragment], but broadly is the portion of the url remaining after the
     * baseUrl has been removed from the start, and parameters removed from the end
     *
     *
     */

    protected fun waitForNavigationState(source: () -> String, condition: (NavigationState) -> Boolean) {
        val timeout = 10L
        val endTime = LocalDateTime.now().plusSeconds(timeout)
        var conditionMet = false
        while (!conditionMet && LocalDateTime.now().isBefore(endTime)) {
            val uriFragmentHandler = StrictURIFragmentHandler()
            val navState = uriFragmentHandler.navigationState(URI.create(source()))
            navState.update(uriFragmentHandler)
            if (condition(navState)) {
                conditionMet = true
            }
        }
        if (!conditionMet) {
            throw  AssertionError("Timed out after $timeout, condition not met")
        }
    }

    private fun currentUrl(): String {
        return WebDriverRunner.getWebDriver().currentUrl
    }
}

var browser: Browser = SelenideBrowser()



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