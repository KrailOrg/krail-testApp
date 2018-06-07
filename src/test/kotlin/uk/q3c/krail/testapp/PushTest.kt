package uk.q3c.krail.testapp

import com.codeborne.selenide.Selenide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.dsl.xon
import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.browser
import uk.q3c.krail.functest.createBrowser
import uk.q3c.krail.functest.executionMode


/**
 * Created by David Sowerby on 10 Feb 2018
 */
class PushTest : Spek({
    val pushPage = "notifications/push"
    given("an application that should react to messages from other clients") {
        beforeGroup {
            executionMode = ExecutionMode.SELENIDE
            createBrowser()
        }
        afterGroup {
            if (executionMode == ExecutionMode.SELENIDE) Selenide.close()
        }

        on("a client sends a message") {
            with(browser) {
                navigateTo(pushPage)
                openNewTab()
                switchToTab(1)
                navigateTo(pushPage)
            }

            sendMessage("TEST", "First test")
            browser.switchToTab(0)


            it("should receive the message") {
                val pushView = PushViewObject()
                pushView.messageLog.valueShouldBe("TEST:First test\n")
            }

        }

        /**
         * Ignored see https://github.com/KrailOrg/krail/issues/707
         */
        xon("a client sends a message while push is not enabled") {
            browser.navigateTo(pushPage)
            browser.openNewTab()
            browser.switchToTab(1)
            browser.navigateTo(pushPage)
            val pushView1 = PushViewObject()
            pushView1.pushEnabled.setValue(false)
            sendMessage("TEST", "Second test")
            browser.switchToTab(0)

            it("should receive the message") {
                val pushView0 = PushViewObject()
                pushView0.messageLog.valueShouldBe("")
            }
        }
    }

})


fun sendMessage(grp: String, msg: String) {
    val pushView = PushViewObject()
    pushView.groupInput.setValue(grp)
    pushView.messageInput.setValue(msg)
    pushView.sendButton.click()
}

