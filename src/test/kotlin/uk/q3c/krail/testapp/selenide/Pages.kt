package uk.q3c.krail.testapp.selenide

import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import uk.q3c.krail.core.view.DefaultLoginView
import java.util.*


/**
 * Created by David Sowerby on 18 Jan 2018
 */

class JPAPage(val page: Page = Page("jpa", "Table 1")) : PageObj by page {


    fun common1Button(): ButtonElement {
        return page.buttonById("#JpaView-Button-1")
    }

    fun table1(): GridElement {
        return page.gridById("#JpaView-Grid-1")
    }
}


class SystemAccountPage(val page: Page = Page("system-account", "DefaultSystemAccountView")) : PageObj by page
class HomePage(val page: Page = Page("home", "TestAppPublicHomeView")) : PageObj by page
class PrivateHomePage(val page: Page = Page("private/home", "DefaultPrivateHomeView")) : PageObj by page

class MessageBoxPage(val page: Page = Page("widgetset", "used purely")) : PageObj by page

class NotificationsPage(val page: Page = Page("notifications", "Vaadin provides")) : PageObj by page

class FinancePage(val page: Page = Page("private/finance", "FinanceView")) : PageObj by page

class LoginPage(val page: Page = Page("login", "Please enter your username and password")) : PageObj by page {
    val username: ButtonElement
        get() {
            return page.buttonById(Optional.of("username"), DefaultLoginView::class.java, TextField::class.java)
        }
    val password: ButtonElement
        get() {
            return page.buttonById(Optional.of("password"), DefaultLoginView::class.java, PasswordField::class.java)
        }
}

class LogoutPage(val page: Page = Page("logout", "Logged out")) : PageObj by page


