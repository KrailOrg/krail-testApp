package uk.q3c.krail.functest.selenide

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.WebDriverRunner
import com.google.inject.Injector
import com.google.inject.Key
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import com.vaadin.server.*
import com.vaadin.shared.ApplicationConstants
import com.vaadin.ui.UI
import org.amshove.kluent.mock
import org.apache.onami.persist.PersistenceService
import uk.q3c.krail.core.navigate.Navigator
import uk.q3c.krail.core.navigate.sitemap.MasterSitemap
import uk.q3c.krail.core.ui.ScopedUI
import uk.q3c.krail.core.view.ViewFactory
import uk.q3c.krail.core.view.component.ViewChangeBusMessage
import uk.q3c.krail.functest.Browser
import uk.q3c.krail.functest.ViewElement
import uk.q3c.krail.functest.coded.FunctionalTestBindingManager
import uk.q3c.krail.functest.coded.TestVaadinService
import uk.q3c.krail.functest.coded.TestVaadinSession
import uk.q3c.krail.functest.waitForNavigationState
import uk.q3c.krail.testapp.TestAppUI
import uk.q3c.krail.testapp.persist.Jpa1
import uk.q3c.krail.testapp.persist.Jpa2
import uk.q3c.krail.testapp.ui.TestAppUIProvider
import java.util.concurrent.locks.ReentrantLock


class SelenideViewElement(override val id: String) : ViewElement

class SelenideBrowser : Browser {
    override fun viewShouldBe(viewClass: Class<*>) {
        `$`("#${viewClass.simpleName}").`is`(visible)
    }

    override lateinit var view: ViewElement
    private lateinit var ui: ScopedUI
    private lateinit var navigator: Navigator
    private lateinit var masterSitemap: MasterSitemap
    private lateinit var viewFactory: ViewFactory
    private val viewChangeBusMessage: ViewChangeBusMessage = mock()

    override fun navigateTo(fragment: String) {
        Selenide.open(fragment)
        val node = masterSitemap.nodeFor(fragment)
        view = SelenideViewElement(node.viewClass.simpleName)
    }


    override fun setup() {
        System.setProperty("selenide.browser", "chrome")
        System.setProperty("selenide.baseUrl", "http://localhost:8080/krail-testapp/#")
        val injector: Injector = FunctionalTestBindingManager().injector
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
//        navigator = ui.krailNavigator
//        ui.page.location = URI.create("http://localhost:8080/krail-testapp/#login")
        masterSitemap = injector.getInstance(MasterSitemap::class.java)
        viewFactory = injector.getInstance(ViewFactory::class.java)
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
}

