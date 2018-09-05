package uk.q3c.krail.testapp

import uk.q3c.krail.functest.AbstractCustomObject
import uk.q3c.krail.functest.AbstractPageObject
import uk.q3c.krail.functest.AbstractViewObject
import uk.q3c.krail.functest.Button
import uk.q3c.krail.functest.CheckBox
import uk.q3c.krail.functest.Label
import uk.q3c.krail.functest.PasswordField
import uk.q3c.krail.functest.Spinner
import uk.q3c.krail.functest.TextArea
import uk.q3c.krail.functest.TextField

class TestAppPublicHomeViewObject : AbstractViewObject()

class DefaultLoginViewObject : AbstractViewObject() {
    val label by Label()
    val username by TextField()
    val statusMessage by Label()
    val submit by Button()
    val password by PasswordField()
}

class DefaultLogoutViewObject : AbstractViewObject()

class TestAppPrivateHomeViewObject : AbstractViewObject()

class DefaultNavigationViewObject : AbstractViewObject()

class DefaultRequestSystemAccountCreateViewObject : AbstractViewObject() {

    val label by Label()
}

class DefaultRequestSystemAccountEnableViewObject : AbstractViewObject() {

    val label by Label()
}

class DefaultRequestSystemAccountRefreshViewObject : AbstractViewObject() {

    val label by Label()
}

class DefaultRequestSystemAccountResetViewObject : AbstractViewObject() {

    val label by Label()
}

class DefaultRequestSystemAccountUnlockViewObject : AbstractViewObject() {

    val label by Label()
}

class NotificationsViewObject : AbstractViewObject() {

    val systemLevelOptionButton by Button()
    val errorButton by Button()
    val infoArea by Label()
    val warnButton by Button()
    val clearOptionStoreButton by Button()
    val infoButton by Button()
    val viewOptionsButton by Button()
}

class SpinnerObject(id: String) : AbstractCustomObject(id)

class IntStepperObject(id: String) : AbstractCustomObject(id)

class WidgetsetViewObject : AbstractViewObject() {

    val spinner by Spinner() //= SpinnerObject ("WidgetsetView-spinner")
    val stepper = IntStepperObject("WidgetsetView-stepper")
    val infoArea by Label()
    val changeSpinnerType by Button()
    val param2 by Label()
    val param1 by Label()
}

class PushViewObject : AbstractViewObject() {

    val pushEnabled by CheckBox()
    val sendButton by Button()
    val messageLog by TextArea()
    val groupInput by TextField()
    val messageInput by TextField()
    val infoArea by Label()
}

class FormLayoutObject(id: String) : AbstractCustomObject(id)

class ManualFormObject : AbstractViewObject() {

    val nameField by TextField()
    val ageField by TextField()
    val validationMsg by Label()
    val validateButton by Button()
    val layout = FormLayoutObject("ManualForm-layout")
    val titleField by TextField()
}

class LocaleChangerObject : AbstractViewObject() {

    val changeToUK by Button()
    val changeToGerman by Button()
    val currentLocale by Label()
    val layout = FormLayoutObject("LocaleChanger-layout")
}

class HelpViewObject : AbstractViewObject() {

    val button by Button()
}

class DefaultFormObject : AbstractViewObject()

class FinanceViewObject : AbstractViewObject()

class AccountsViewObject : AbstractViewObject()

class PayrollViewObject : AbstractViewObject() {

    val setValue2Button by Button()
    val refreshButton by Button()
    val clearCacheButton by Button()
    val setValue1Button by Button()
    val textArea by TextArea()
    val adminButton by Button()
    val clearOptionDatabaseButton by Button()
}

class SystemAdminViewObject : AbstractViewObject()

class SitemapReportViewObject : AbstractViewObject() {

    val reportArea by TextArea()
}

class I18NViewObject : AbstractViewObject() {

    val exportButton by Button()
    val localeList by TextArea()
    val instructions1 by Label()
    val instructions2 by Label()
    val exportStatus by Label()
}

class PurchasingViewObject : AbstractViewObject() {

    val button by Button()
}

class NavigationBarObject(id: String) : AbstractCustomObject(id) {

    val menuButton by Button()
    val titleLabel by Label()
    val settingsButton by Button()
    val login_logout_Button by Button()
    val helpButton by Button()
    val homeButton by Button()
    val notificationsButton by Button()
}

class TestAppSimpleUIObject : AbstractPageObject() {

    val topBar = NavigationBarObject("TestAppSimpleUI-topBar")
}

