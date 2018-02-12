package uk.q3c.krail.testapp

import uk.q3c.krail.functest.*

class TestAppPublicHomeViewObject : AbstractViewObject() {

    val label by Label()
}

class DefaultLoginViewObject : AbstractViewObject() {

    val label by Label()
    val username by TextField()
    val statusMsgLabel by Label()
    val submit by Button()
    val password by PasswordField()
}

class DefaultLogoutViewObject : AbstractViewObject()

class DefaultSystemAccountViewObject : AbstractViewObject() {

    val label by Label()
}

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

class IntStepperObject(id: String) : AbstractCustomObject(id)

class WidgetsetViewObject : AbstractViewObject() {

    val stepper = IntStepperObject("WidgetsetView-stepper")
    val infoArea by Label()
    val param2 by Label()
    val popupButton by Button()
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

class JpaViewObject : AbstractViewObject() {

    val countLabelFromContainer1 by Label()
    val countLabelFromContainer2 by Label()
    val saveBtn1 by Button()
    val saveBtn2 by Button()
    val saveBtn3 by Button()
    val table1 by Grid()
    val table2 by Grid()
    val countLabelFromDao1 by Label()
    val countLabelFromDao2 by Label()
}

class FormLayoutObject(id: String) : AbstractCustomObject(id)

class AutoFormObject : AbstractViewObject() {

    val validationMsg by Label()
    val layout = FormLayoutObject("AutoForm-layout")
}

class LocaleChangerObject : AbstractViewObject() {

    val changeToUK by Button()
    val changeToGerman by Button()
    val currentLocale by Label()
    val layout = FormLayoutObject("LocaleChanger-layout")
}

class DefaultPrivateHomeViewObject : AbstractViewObject() {

    val label by Label()
}

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

class PurchasingViewObject : AbstractViewObject()

class SystemAdminViewObject : AbstractViewObject() {

    val buildReportBtn by Button()
}

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

class DefaultUserStatusPanelObject(id: String) : AbstractCustomObject(id) {

    val login_logout_Button by Button()
    val usernameLabel by Label()
}

class DefaultApplicationHeaderObject(id: String) : AbstractCustomObject(id) {

    val label by Label()
}

class DefaultMessageBarObject(id: String) : AbstractCustomObject(id) {

    val display by Label()
}


class DefaultSubPagePanelObject(id: String) : AbstractCustomObject(id)

class TestAppUIObject : AbstractPageObject() {

    val logo by Image()
    val navTree by Tree()
    val userStatus = DefaultUserStatusPanelObject("TestAppUI-userStatus")
    val header = DefaultApplicationHeaderObject("TestAppUI-header")
    val messageBar = DefaultMessageBarObject("TestAppUI-messageBar")
    val localeCombo by ComboBox()
    val menu by MenuBar()
    val breadcrumb by Breadcrumb()
    val subpage = DefaultSubPagePanelObject("TestAppUI-subpage")
}

