package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.WebDriverRunner
import com.google.inject.Injector
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import uk.q3c.krail.functest.*
import uk.q3c.krail.functest.coded.FunctionalTestServletContextListener
import java.io.File
import java.net.URI


class SelenideViewElement(override val id: String) : ViewElement
class SelenidePageElement(override val id: String) : PageElement

class SelenideBrowser : Browser {


    private val log = LoggerFactory.getLogger(this.javaClass.name)

    val injector: Injector = FunctionalTestServletContextListener().injector

    override fun viewShouldBe(viewClass: Class<*>) {
        `$`("#${viewClass.simpleName}").`is`(visible)
    }

    override var view: ViewElement = SelenideViewElement("")
        get() {
            return SelenideViewElement(routeMap.viewFor(browser.currentFragment()).viewId.id)
        }
    override var page: PageElement = SelenidePageElement("")
        get() {
            return SelenidePageElement(routeMap.uiFor(browser.currentFragment()).uiId.id)
        }

    private lateinit var routeMap: RouteMap

    override fun navigateTo(fragment: String) {
        Selenide.open(fragment)
        view = SelenideViewElement(routeMap.viewFor(fragment).viewId.id)
        page = SelenidePageElement(routeMap.uiFor(fragment).uiId.id)
        browser.fragmentShouldBe(fragment)
    }


    override fun setup() {
        System.setProperty("selenide.browser", "chrome")
        System.setProperty("selenide.baseUrl", "http://localhost:8080/krail-testapp/#")

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
}


