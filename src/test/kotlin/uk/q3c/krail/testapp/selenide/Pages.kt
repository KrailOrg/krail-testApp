package uk.q3c.krail.testapp.selenide

import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selenide
import com.vaadin.ui.Label
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import uk.q3c.krail.core.view.DefaultLoginView
import uk.q3c.krail.core.view.DefaultLogoutView
import uk.q3c.krail.core.view.DefaultPrivateHomeView
import uk.q3c.krail.core.view.DefaultSystemAccountView
import uk.q3c.krail.testapp.view.*
import java.util.*


/**
 * Created by David Sowerby on 18 Jan 2018
 */

class JPAPage : Page("jpa", JpaView::class.java) {


    fun common1Button(): ButtonElement {
        return buttonById("#JpaView-Button-1")
    }

    fun table1(): GridElement {
        return gridById("#JpaView-Grid-1")
    }
}


class SystemAccountPage : Page("system-account", DefaultSystemAccountView::class.java)
class HomePage : Page("home", TestAppPublicHomeView::class.java)
class PrivateHomePage : Page("private/home", DefaultPrivateHomeView::class.java)

class MessageBoxPage : Page("widgetset", WidgetsetView::class.java) {
    val id: LabelElement
        get() {
            return labelById(Optional.of("id"), WidgetsetView::class.java, Label::class.java)
        }

    val age: LabelElement
        get() {
            return labelById(Optional.of("age"), WidgetsetView::class.java, Label::class.java)
        }
}

class NotificationsPage : Page("notifications", NotificationsView::class.java)

class FinancePage : Page("private/finance", FinanceView::class.java)

class LoginPage : Page("login", DefaultLoginView::class.java) {
    val username: ButtonElement
        get() {
            return buttonById(Optional.of("username"), DefaultLoginView::class.java, TextField::class.java)
        }
    val password: ButtonElement
        get() {
            return buttonById(Optional.of("password"), DefaultLoginView::class.java, PasswordField::class.java)
        }

    fun failedLoginBannerShouldBeVisible() {
        // this could look for the label then get the text - would be easier to mimic in coded verison
        Selenide.`$`(Selectors.byText("That username or password was not recognised")).shouldBe(Condition.visible)
    }
}

class LogoutPage : Page("logout", DefaultLogoutView::class.java)





