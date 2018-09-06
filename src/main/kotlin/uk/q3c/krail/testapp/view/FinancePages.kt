/*
 * Copyright (c) 2015. David Sowerby
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package uk.q3c.krail.testapp.view

import uk.q3c.krail.app.finance
import uk.q3c.krail.app.finance_accounts
import uk.q3c.krail.app.finance_payroll
import uk.q3c.krail.core.navigate.sitemap.DirectSitemapModule
import uk.q3c.krail.core.shiro.PageAccessControl
import uk.q3c.krail.testapp.i18n.LabelKey

class FinancePages : DirectSitemapModule() {

    override fun define() {
        addEntry(finance, LabelKey.Finance, PageAccessControl.PERMISSION, FinanceView::class.java)
        addEntry(finance_accounts, LabelKey.Accounts, PageAccessControl.PERMISSION, AccountsView::class.java)
        addEntry(finance_payroll, LabelKey.Payroll, PageAccessControl.PERMISSION, PayrollView::class.java)
    }
}
