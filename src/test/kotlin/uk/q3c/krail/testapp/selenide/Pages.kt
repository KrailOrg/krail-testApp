package uk.q3c.krail.testapp.selenide

import com.vaadin.ui.Label
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import uk.q3c.krail.core.view.DefaultLoginView
import uk.q3c.krail.testapp.view.WidgetsetView
import java.util.*


/**
 * Created by David Sowerby on 18 Jan 2018
 */

class JPAPage : Page("jpa", "Table 1") {


    fun common1Button(): ButtonElement {
        return buttonById("#JpaView-Button-1")
    }

    fun table1(): GridElement {
        return gridById("#JpaView-Grid-1")
    }
}


class SystemAccountPage : Page("system-account", "DefaultSystemAccountView")
class HomePage : Page("home", "TestAppPublicHomeView")
class PrivateHomePage : Page("private/home", "DefaultPrivateHomeView")

class MessageBoxPage : Page("widgetset", "used purely") {
    val id: LabelElement
        get() {
            return labelById(Optional.of("id"), WidgetsetView::class.java, Label::class.java)
        }

    val age: LabelElement
        get() {
            return labelById(Optional.of("age"), WidgetsetView::class.java, Label::class.java)
        }
}

class NotificationsPage : Page("notifications", "Vaadin provides")

class FinancePage : Page("private/finance", "FinanceView")

class LoginPage : Page("login", "Please enter your username and password") {
    val username: ButtonElement
        get() {
            return buttonById(Optional.of("username"), DefaultLoginView::class.java, TextField::class.java)
        }
    val password: ButtonElement
        get() {
            return buttonById(Optional.of("password"), DefaultLoginView::class.java, PasswordField::class.java)
        }
}

class LogoutPage : Page("logout", "Logged out")


