package uk.q3c.krail.functest.coded

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import com.google.inject.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.vaadin.server.*
import com.vaadin.shared.ApplicationConstants
import com.vaadin.ui.UI
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBe
import org.apache.commons.io.FileUtils
import org.apache.onami.persist.PersistenceService
import org.slf4j.LoggerFactory
import uk.q3c.krail.core.guice.BindingsCollator
import uk.q3c.krail.core.navigate.NavigationState
import uk.q3c.krail.core.navigate.Navigator
import uk.q3c.krail.core.navigate.sitemap.DefaultMasterSitemap
import uk.q3c.krail.core.navigate.sitemap.MasterSitemap
import uk.q3c.krail.core.navigate.sitemap.SitemapModule
import uk.q3c.krail.core.ui.ScopedUI
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.core.view.LoginView
import uk.q3c.krail.functest.*
import uk.q3c.krail.testapp.TestAppServletContextListener
import uk.q3c.krail.testapp.TestAppUI
import uk.q3c.krail.testapp.persist.Jpa1
import uk.q3c.krail.testapp.persist.Jpa2
import uk.q3c.krail.testapp.ui.TestAppUIProvider
import uk.q3c.krail.testapp.view.TestAppBindingsCollator
import uk.q3c.util.clazz.UnenhancedClassIdentifier
import java.io.File
import java.net.URI
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class CodedBrowser : Browser {


    override lateinit var page: PageElement
    private val log = LoggerFactory.getLogger(this.javaClass.name)
    private lateinit var injector: Injector
    private var setup = false
    private val history: MutableList<NavigationState> = mutableListOf()
    private var historyOffset = 0
    private var updateHistory = true

    override fun viewShouldBe(viewClass: Class<*>) {
        (view as CodedViewElement).view.javaClass.shouldBe(viewClass)
    }

    override fun currentUrl(): String {
        return ui.page.location.toString()
    }

    private lateinit var navigator: Navigator
    override lateinit var view: ViewElement
    private lateinit var ui: ScopedUI

    override fun setup() {
        injector = FunctionalTestServletContextListener().injector

        val vaadinServlet: VaadinServlet = mock()
        val deploymentConfiguration: DeploymentConfiguration = mock()
        val wrappedSession: WrappedSession = mock()
        val vaadinRequest: VaadinRequest = mock()
        //    val mockSession : VaadinSession = mock()
        lateinit var persistenceService1: PersistenceService
        lateinit var persistenceService2: PersistenceService


        val lock = ReentrantLock()
        lock.lock()
        val vaadinService: VaadinService = TestVaadinService(vaadinServlet, deploymentConfiguration)
        whenever(wrappedSession.getAttribute(any())).thenReturn(lock)
        val session = TestVaadinSession(vaadinService)
        session.testLock = lock
        session.refreshTransients(wrappedSession, vaadinService)
        VaadinSession.setCurrent(session)
        persistenceService1 = injector.getInstance(Key.get(PersistenceService::class.java, Jpa1::class.java))
        persistenceService2 = injector.getInstance(Key.get(PersistenceService::class.java, Jpa2::class.java))
        persistenceService1.start()
        persistenceService2.start()
        val uiProvider = injector.getInstance(TestAppUIProvider::class.java)
        whenever(vaadinRequest.service).thenReturn(vaadinService)
        whenever(vaadinRequest.getParameter("v-loc")).thenReturn("http://localhost:8080/krail-testapp/#home")
        whenever(vaadinRequest.getAttribute(ApplicationConstants.UI_ROOT_PATH)).thenReturn("http://localhost:8080/krail-testapp")
        val event = UICreateEvent(vaadinRequest, TestAppUI::class.java)
        ui = uiProvider.createInstance(event) as TestAppUI
        UI.setCurrent(ui)
        ui.session = session
        ui.doInit(vaadinRequest, 1, null)
        navigator = ui.krailNavigator
        ui.page.location = URI.create("http://localhost:8080/krail-testapp/#home")
        setup = true
        log.debug("setup complete")
        navigateTo("home")
    }

    override fun forward() {
        if (historyOffset - 1 > 0) {
            historyOffset--
            updateHistory = false
            navigateTo(history[historyOffset].fragment)
        } else {
            throw CodedBrowserException("There is no history to browser forward to")
        }
    }

    /**
     * Generates page objects for Functional Testing, plus the [RouteMap] to JSON
     */
    fun generatePageObjects() {
        if (!setup) {
            setup()
        }

        val functionalTestSupportBuilder = injector.getInstance(FunctionalTestSupportBuilder::class.java)
        log.info("creating functional test model")
        val model = functionalTestSupportBuilder.generate()

        val pageObjectGenerator = KotlinPageObjectGenerator()
        val currentDir = File(".")
        val kotlinPath = "src/test/kotlin"
        val resourcesPath = "src/test/resources"
        val packagePath = "uk/q3c/krail/testapp/"
        val targetDir = File(currentDir, "$kotlinPath/$packagePath")
        val targetFile = File(targetDir, "PageObjects.kt")
        log.debug("generating page objects to ${targetFile.absolutePath}")
        pageObjectGenerator.generate(file = targetFile, model = model, packageName = packagePath.replace("/", ".").dropLast(1))

        val resourcesDir = File(currentDir, "$resourcesPath/$packagePath")
        if (!resourcesDir.exists()) {
            FileUtils.forceMkdir(resourcesDir)
        }
        val routeMapFile = File(resourcesDir, "routeMap.json")
        log.info("Writing RouteMap to ${routeMapFile.absolutePath}")
        model.routeMap.toJson(routeMapFile)

    }


    override fun fragmentShouldBe(desiredFragment: String) {
        waitForNavigationState({ currentUrl() }, { navState ->
            println("Test harness:  waiting for fragment $desiredFragment")
            desiredFragment == navState.fragment
        })
    }

    override fun fragmentShouldNotBe(desiredFragment: String) {
        waitForNavigationState({ currentUrl() }, { navState -> desiredFragment != navState.fragment })
    }

    override fun back() {
        if (historyOffset + 1 < history.size) {
            historyOffset++
            updateHistory = false
            navigateTo(history[historyOffset].fragment)
        } else {
            throw CodedBrowserException("There is no history to browser back to")
        }
    }

    override fun navigateTo(fragment: String) {
        navigator.navigateTo(fragment)
//        fragmentShouldBe(fragment) can't do this here, because there are circumstances where it is not true (eg, trying to go back to a private page after logout)
        update()
    }

    override fun currentFragment(): String {
        return URI.create(currentUrl()).fragment
    }

    fun update() {
        val real = injector.getInstance(UnenhancedClassIdentifier::class.java)
        view = CodedViewElement(ui.view, real.getOriginalClassFor(ui.view).simpleName)
        page = CodedPageElement(ui, real.getOriginalClassFor(ui).simpleName)
        if (updateHistory) {
            history.add(0, navigator.currentNavigationState)
        } else {
            updateHistory = true  // one shot block on changing history
        }
    }
}

class CodedBrowserException(msg: String) : RuntimeException(msg)


class CodedViewElement(val view: KrailView, override val id: String) : ViewElement

class CodedPageElement(val ui: ScopedUI, override val id: String) : PageElement


class FunctionalTestBindingsCollator : TestAppBindingsCollator() {
    override fun sitemapModule(): Module {
        return FunctionalTestSitemapModule()
    }

    override fun addUtilModules(coreModules: MutableList<Module>?) {
        super.addUtilModules(coreModules)
        if (coreModules != null) {
            coreModules.add(FunctionalTestSupportModule())
        }
    }
}

class FunctionalTestServletContextListener : TestAppServletContextListener() {


    override fun getBindingsCollator(): BindingsCollator {
        return FunctionalTestBindingsCollator()
    }
}


class FunctionalTestSitemapModule : SitemapModule() {
    override fun bindMasterSitemap() {
        bind(MasterSitemap::class.java).to(DefaultMasterSitemap::class.java).`in`(Singleton::class.java)
    }
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
        browser.fragmentShouldBe(urlFragment)
    }

    override fun shouldBeOpenWithUrl(otherFragment: String) {
        Selenide.`$`(view.javaClass.simpleName).`is`(Condition.visible)
        browser.fragmentShouldBe(otherFragment)
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

class TestVaadinSession(service: VaadinService) : VaadinSession(service) {
    lateinit var testLock: ReentrantLock
    override fun getLockInstance(): Lock {
        return testLock
    }
}

class TestVaadinService(servlet: VaadinServlet, deploymentConfiguration: DeploymentConfiguration?) : VaadinServletService(servlet, deploymentConfiguration) {
    override fun generateConnectorId(session: VaadinSession?, connector: ClientConnector?): String {
        return UUID.randomUUID().toString()
    }
}


