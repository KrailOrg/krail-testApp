package uk.q3c.krail.functest

import uk.q3c.krail.functest.ExecutionMode.CODED
import uk.q3c.krail.functest.ExecutionMode.SELENIDE
import uk.q3c.krail.functest.coded.CodedNotificationElement
import uk.q3c.krail.functest.selenide.SelenideNotificationElement

/**
 * Created by David Sowerby on 10 Feb 2018
 */
enum class NotificationLevel { ERROR, WARNING, INFORMATION }

fun notificationShouldBeVisible(level: NotificationLevel, text: String) {

    val notificationElement = when (executionMode) {
        SELENIDE -> SelenideNotificationElement()
        CODED -> CodedNotificationElement()
    }
    notificationElement.shouldBeVisible(level, text)

}

fun notificationShouldNotBeVisible() {
    val notificationElement = when (executionMode) {
        SELENIDE -> SelenideNotificationElement()
        CODED -> CodedNotificationElement()
    }
    notificationElement.shouldNotBeVisible()
}