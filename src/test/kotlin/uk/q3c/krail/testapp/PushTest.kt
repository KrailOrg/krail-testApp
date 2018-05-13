package uk.q3c.krail.testapp

import com.codeborne.selenide.Selenide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import uk.q3c.krail.functest.*
import kotlin.concurrent.thread

/**
 * Created by David Sowerby on 10 Feb 2018
 */
class PushTest : Spek({
    given("an application that should react to messages from other clients") {
        beforeGroup {
            executionMode = ExecutionMode.SELENIDE
            createBrowser()
        }
        afterGroup {
            if (executionMode == ExecutionMode.SELENIDE) Selenide.close()
        }

        on("a client sends a message") {
            navigateToPushView(browser)
            val pushView = PushViewObject()
            pushView.messageLog.valueShouldBe("")
            doInOtherBrowser {
                sendMessage("TEST", "First test")
            }

            it("should receive the message") {
                pushView.messageLog.valueShouldBe("TEST:First test\n")
            }

        }

        on("a client sends a message while push is not enabled") {
            val pushView = PushViewObject()
            pushView.messageLog.valueShouldBe("TEST:First test\n")
            pushView.pushEnabled.setValue(false)
            doInOtherBrowser {
                PushViewObject().pushEnabled.setValue(false)
                sendMessage("TEST", "Second test")
            }
            it("should receive the message") {
                pushView.messageLog.valueShouldBe("TEST:First test\n")
            }
        }
    }

})

fun navigateToPushView(theBrowser: Browser) {
    theBrowser.navigateTo("notifications/push")
    theBrowser.fragmentShouldBe("notifications/push")
}

fun doInOtherBrowser(block: (Browser) -> Unit) {
    thread(start = true) {
        val otherBrowser = createBrowser(applyGlobal = false)
        navigateToPushView(otherBrowser)
        block(otherBrowser)
        Selenide.close()
    }.join()

}

fun sendMessage(grp: String, msg: String) {
    val pushView = PushViewObject()
    pushView.groupInput.setValue(grp)
    pushView.messageInput.setValue(msg)
    pushView.sendButton.click()
}
