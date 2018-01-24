package uk.q3c.krail.testapp.functest

import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import uk.q3c.krail.core.navigate.NavigationState
import uk.q3c.krail.core.navigate.StrictURIFragmentHandler
import java.net.URI
import java.time.LocalDateTime

/**
 * Created by David Sowerby on 23 Jan 2018
 */
interface Browser {
    fun back()

    /**
     * Returns when the [desiredFragment] appears in the browser url, or throws an [AssertionError] on timeout
     */
    fun currentFragmentShouldBe(desiredFragment: String)

    fun currentFragmentShouldNotBe(desiredFragment: String)
}

class SelenideBrowser : Browser {


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
     * The 'fragment' is the portion of the url remaining after the baseUrl has been removed from the start, and parameters
     * removed from the end
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


