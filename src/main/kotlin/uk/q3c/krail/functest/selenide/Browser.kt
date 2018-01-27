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
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import org.amshove.kluent.mock
import org.apache.onami.persist.PersistenceService
import uk.q3c.krail.core.navigate.Navigator
import uk.q3c.krail.core.ui.ScopedUI
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.functest.Browser
import uk.q3c.krail.functest.LabelElement
import uk.q3c.krail.functest.TextFieldElement
import uk.q3c.krail.functest.coded.FunctionalTestBindingManager
import uk.q3c.krail.functest.coded.TestVaadinService
import uk.q3c.krail.functest.coded.TestVaadinSession
import uk.q3c.krail.functest.waitForNavigationState
import uk.q3c.krail.testapp.TestAppUI
import uk.q3c.krail.testapp.persist.Jpa1
import uk.q3c.krail.testapp.persist.Jpa2
import uk.q3c.krail.testapp.ui.TestAppUIProvider
import java.util.concurrent.locks.ReentrantLock


class SelenideBrowser : Browser {
    override fun viewShouldBe(viewClass: Class<*>) {
        `$`("#${viewClass.simpleName}").shouldBe(visible)
    }

    override lateinit var view: KrailView
    private lateinit var ui: ScopedUI
    private lateinit var navigator: Navigator

    override fun navigateTo(fragment: String) {
        Selenide.open(fragment)
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
//        UI.setCurrent(ui)
//        ui.session = session
//        ui.doInit(vaadinRequest, 1, null)
//        navigator = ui.krailNavigator
//        ui.page.location = URI.create("http://localhost:8080/krail-testapp/#login")
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

