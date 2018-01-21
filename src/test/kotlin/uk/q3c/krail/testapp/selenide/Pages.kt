package uk.q3c.krail.testapp.selenide


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

class MessageBoxPage(val page: Page = Page("widgetset", "used purely")) : PageObj by page

class NotificationsPage(val page: Page = Page("notifications", "Vaadin provides")) : PageObj by page

class FinancePage(val page: Page = Page("private/finance", "FinanceView")) : PageObj by page
