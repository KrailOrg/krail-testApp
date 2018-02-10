package uk.q3c.krail.testapp.p

import com.codeborne.selenide.Selenide
import org.junit.After
import org.junit.Before
import org.junit.Test
import uk.q3c.krail.functest.*
import uk.q3c.krail.functest.NotificationLevel.*
import uk.q3c.krail.testapp.view.NotificationsView

/**
 * Created by David Sowerby on 10 Feb 2018
 */
class NotificationTest {

    @Before
    fun before() {
        executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED
        createBrowser()
        browser.navigateTo("home")
    }

    @After
    fun after() {
//        println("Sleeping after")
        Selenide.close()
    }


    @Test
    fun notifications() {

//    given("navigate to Notifications page")
        val page = TestAppUIObject()
        page.menu.select("Notifications/Push")
        page.breadcrumb.select(0)
        browser.viewShouldBe(NotificationsView::class.java)
        val view = NotificationsViewObject()
        val msg = "You cannot use service Fake Service until it has been started"

//        on("click the fake error button") {
        view.errorButton.click()

//            it("displays error notification") {

        notificationShouldBeVisible(ERROR, msg)
        notificationShouldNotBeVisible()
        page.messageBar.display.valueShouldBe("ERROR: $msg")

//        on("click the fake warn button") {
        view.warnButton.click()

//            it("displays warning notification") {

        notificationShouldBeVisible(WARNING, "You cannot use service Fake Service until it has been started")
        notificationShouldNotBeVisible()
        page.messageBar.display.valueShouldBe("Warning: $msg")
//            }
//        }

//        on("click the fake info button") {
        view.infoButton.click()

//            it("displays info notification") {

        notificationShouldBeVisible(INFORMATION, "You cannot use service Fake Service until it has been started")
        notificationShouldNotBeVisible()
        page.messageBar.display.valueShouldBe(msg)
    }
}

