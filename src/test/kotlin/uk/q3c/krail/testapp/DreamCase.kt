package uk.q3c.krail.testapp

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.WebDriverRunner
import com.google.inject.*
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.vaadin.server.*
import com.vaadin.shared.ApplicationConstants
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import com.vaadin.ui.UI
import org.amshove.kluent.mock
import org.amshove.kluent.shouldBeInstanceOf
import org.apache.onami.persist.PersistenceService
import org.junit.Test
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.core.view.LoginView
import uk.q3c.krail.functest.LabelElement
import uk.q3c.krail.functest.TextFieldElement
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.coded.CodedBrowser
import uk.q3c.krail.testapp.persist.Jpa1
import uk.q3c.krail.testapp.persist.Jpa2
import uk.q3c.krail.testapp.ui.TestAppUIProvider
import uk.q3c.krail.testapp.view.WidgetsetView
import java.net.URI
import java.util.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock


/**
 * Created by David Sowerby on 25 Jan 2018
 */
class DreamCase {
    val injector: Injector = FunctionalTestBindingManager().injector
    val vaadinServlet: VaadinServlet = mock()
    val deploymentConfiguration: DeploymentConfiguration = mock()
    val wrappedSession: WrappedSession = mock()
    val vaadinRequest: VaadinRequest = mock()
    //    val mockSession : VaadinSession = mock()
    lateinit var persistenceService1: PersistenceService
    lateinit var persistenceService2: PersistenceService

    @Test
    fun doit() {
        browser = CodedBrowser()
        browser.setup()
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
        val ui = uiProvider.createInstance(event) as TestAppUI
        UI.setCurrent(ui)
        ui.session = session
        ui.doInit(vaadinRequest, 1, null)
        val navigator = ui.krailNavigator
        ui.page.location = URI.create("http://localhost:8080/krail-testapp/#login")



        navigator.navigateTo("login")
        var currentView = ui.view
        currentView.shouldBeInstanceOf<LoginView>()

        navigator.navigateTo("widgetset")
        currentView = ui.view
        currentView.shouldBeInstanceOf<WidgetsetView>()

//        page.ui.statusPanel.label.valueShouldBe("ds")

//        page.view.username.value = "ds"

    }
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