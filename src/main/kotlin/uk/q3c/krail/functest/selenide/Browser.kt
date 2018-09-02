package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.Wait
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.WebDriverRunner
import com.google.inject.Injector
import org.apache.commons.io.FileUtils
import org.openqa.selenium.support.ui.ExpectedConditions
import org.slf4j.LoggerFactory
import uk.q3c.krail.functest.Browser
import uk.q3c.krail.functest.PageElement
import uk.q3c.krail.functest.RouteMap
import uk.q3c.krail.functest.ViewElement
import uk.q3c.krail.functest.coded.FunctionalTestServletContextListener
import uk.q3c.krail.functest.routeMapFromJson
import uk.q3c.krail.functest.waitForNavigationState
import java.io.File
import java.net.URI


class SelenideViewElement(override val id: String) : ViewElement
class SelenidePageElement(override val id: String) : PageElement

class SelenideBrowser : Browser {
    override fun clickOnNavigationButton(urlSegment: String) {
        SelenideButtonElement("navigationbutton-$urlSegment").click()
    }


    private val log = LoggerFactory.getLogger(this.javaClass.name)

    val injector: Injector = FunctionalTestServletContextListener().injector

    override fun viewShouldBe(viewClass: Class<*>) {
        `$`("#${viewClass.simpleName}").`is`(visible)
    }

    override var view: ViewElement = SelenideViewElement("")
        get() {
            return SelenideViewElement(routeMap.viewFor(currentFragment()).viewId.id)
        }
    override var page: PageElement = SelenidePageElement("")
        get() {
            return SelenidePageElement(routeMap.uiFor(currentFragment()).uiId.id)
        }

    private lateinit var routeMap: RouteMap

    override fun navigateTo(fragment: String) {
        Selenide.open(fragment)
        // TODO - the condition and timeout for this need to be configurable
        val pageStatusId = "#SimpleUI-topBar-titleLabel"
        `$`(pageStatusId).waitUntil(Condition.not(Condition.exactTextCaseSensitive("Loading ...")), 30000L)
        //////////
        view = SelenideViewElement(routeMap.viewFor(fragment).viewId.id)
        page = SelenidePageElement(routeMap.uiFor(fragment).uiId.id)
        fragmentShouldBe(fragment)
    }


    override fun setup() {
        val serverPort = System.getProperty("krail.server.httpPort") ?: "8080"
        System.setProperty("selenide.browser", "chrome")
        System.setProperty("selenide.baseUrl", "http://localhost:${serverPort}/krail-testapp/#")

        val currentDir = File(".")
        val resourcesPath = "src/test/resources"
        val packagePath = "uk/q3c/krail/testapp/"


        val resourcesDir = File(currentDir, "$resourcesPath/$packagePath")
        if (!resourcesDir.exists()) {
            FileUtils.forceMkdir(resourcesDir)
        }
        val routeMapFile = File(resourcesDir, "routeMap.json")
        log.info("Reading RouteMap from ${routeMapFile.absolutePath}")
        routeMap = routeMapFromJson(routeMapFile)
        navigateTo("home")
    }


    override fun back() {
        Selenide.back()
    }


    override fun fragmentShouldBe(desiredFragment: String) {
        waitForNavigationState({ currentUrl() }, { navState -> desiredFragment == navState.fragment })
    }

    override fun fragmentShouldNotBe(desiredFragment: String) {
        waitForNavigationState({ currentUrl() }, { navState -> desiredFragment != navState.fragment })
    }


    override fun currentUrl(): String {
        return WebDriverRunner.getWebDriver().currentUrl
    }

    override fun forward() {
        Selenide.forward()
    }

    override fun currentFragment(): String {
        return URI.create(currentUrl()).fragment
    }

    override fun waitForTabs(requiredNumberOfTabs: Int, timeout: Long) {
//        Wait().withTimeout(Duration.ofMillis(timeout )).until(ExpectedConditions.numberOfWindowsToBe(2));
        Wait().until(ExpectedConditions.numberOfWindowsToBe(2))
        val pageStatusId = "#TestAppUI-pageStatus"
        `$`(pageStatusId).waitUntil(Condition.exactTextCaseSensitive("Ready"), 30000L)
    }

    override fun switchToTab(index: Int) {
        val driver = WebDriverRunner.getWebDriver()
        val handles = driver.windowHandles.toList()
        Selenide.switchTo().window(handles[index])
    }

    override fun openNewTab() {
        val driver = WebDriverRunner.getWebDriver()
        val currentTabCount = driver.windowHandles.size
        val newTab = "#TestAppUI-newTab"
        `$`(newTab).click()
        waitForTabs(currentTabCount + 1, 4000)
    }


}


