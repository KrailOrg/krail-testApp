package uk.q3c.krail.testapp

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byClassName
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selenide.`$`
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import uk.q3c.krail.testapp.selenide.*


class NavigationTest : SelenideTestCase() {

    @Test
    fun browserBackForward() {

        // when we build a browser history
        SystemAccountPage().open()
        MessageBoxPage().open()
        NotificationsPage().open()
        browserBack()

        // then we are back to the right page
        currentUrl().shouldBeEqualTo("widgetset")

        // when
        browserBack()

        //then
        currentUrl().shouldBeEqualTo("system-account")

        // when
        browserForward()

        //then
        currentUrl().shouldBeEqualTo("widgetset")
    }

    @Test
    fun pageSelection() {
        SystemAccountPage().open()
        currentUrl().shouldBeEqualTo("system-account")
        MessageBoxPage().open()
        currentUrl().shouldBeEqualTo("widgetset")
        NotificationsPage().open()
        currentUrl().shouldBeEqualTo("notifications")
        SystemAccountPage().open()
        currentUrl().shouldBeEqualTo("system-account")
    }

    @Test
    fun navigateToPageWithoutPermission() {
        // given
        SystemAccountPage().open()

        // when
        FinancePage().open()

        // then notification shown
        `$`(byText("private/finance is not a valid page")).shouldBe(visible)
        `$`(byClassName("v-Notification")).shouldBe(visible)

        // and page has not moved
        currentUrl().shouldBeEqualTo("private/finance")

    }
}
