package uk.q3c.krail.testapp

import com.codeborne.selenide.Selenide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.NotificationLevel.*
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode
import uk.q3c.krail.functest.notificationShouldBeVisible
import uk.q3c.krail.functest.notificationShouldNotBeVisible
import uk.q3c.krail.testapp.view.NotificationsView

/**
 * Created by David Sowerby on 10 Feb 2018
 */
object NotificationTest : Spek({
    val msg = "You cannot use service Fake Service until it has been started"

    given("navigate to Notifications page") {
        beforeGroup {
            executionMode = ExecutionMode.SELENIDE
//        executionMode = ExecutionMode.CODED
            createBrowser()

        }
        afterGroup {
            if (executionMode == ExecutionMode.SELENIDE) Selenide.close()
        }

        on("click the fake error button") {
            browser.navigateTo(notifications)
            browser.viewShouldBe(NotificationsView::class.java)
            val view = NotificationsViewObject()

            view.errorButton.click()

            it("displays error notification") {

                notificationShouldBeVisible(ERROR, msg)
                notificationShouldNotBeVisible()
            }
        }

        on("click the fake warn button") {
            val view = NotificationsViewObject()
            view.warnButton.click()

            it("displays warning notification") {

                notificationShouldBeVisible(WARNING, "You cannot use service Fake Service until it has been started")
                notificationShouldNotBeVisible()
            }
        }

        on("click the fake info button") {
            val view = NotificationsViewObject()
            view.infoButton.click()

            it("displays info notification") {

                notificationShouldBeVisible(INFORMATION, "You cannot use service Fake Service until it has been started")
                notificationShouldNotBeVisible()
            }
        }
    }
})