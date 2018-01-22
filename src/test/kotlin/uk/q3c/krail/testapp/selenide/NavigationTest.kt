package uk.q3c.krail.testapp.selenide

import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byClassName
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selenide.`$`
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test


class NavigationTest : SelenideTestCase() {

    @Test
    fun browserBackForward() {

        // when we build a browser history
        SystemAccountPage().open()
        MessageBoxPage().open()
        NotificationsPage().open()
        browserBack()

        // then we are back to the right page
        MessageBoxPage().shouldBeOpen()

        // when
        browserBack()

        //then
        SystemAccountPage().shouldBeOpen()

        // when
        browserForward()

        //then
        MessageBoxPage().shouldBeOpen()
    }


    /**
     * Because each page object verifies itself there are no checks needed
     */
    @Test
    fun pageSelection() {
        SystemAccountPage().open()
        MessageBoxPage().open()
        NotificationsPage().open()
        SystemAccountPage().open()
    }

    @Test
    fun navigateToPageWithoutPermission() {
        // given
        SystemAccountPage().open().logoutIfLoggedIn()

        // when
        FinancePage().open()

        // then notification shown
        `$`(byText("private/finance is not a valid page")).shouldBe(visible)
        `$`(byClassName("v-Notification")).shouldBe(visible)

        // we now have difference between url and page
        SystemAccountPage().currentUrl().shouldBeEqualTo("private/finance")

    }
}
