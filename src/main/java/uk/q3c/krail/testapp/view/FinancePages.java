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

package uk.q3c.krail.testapp.view;

import uk.q3c.krail.core.navigate.sitemap.DirectSitemapModule;
import uk.q3c.krail.core.shiro.PageAccessControl;
import uk.q3c.krail.testapp.i18n.LabelKey;

public class FinancePages extends DirectSitemapModule {

    @Override
    protected void define() {
        addEntry("private/finance", FinanceView.class, LabelKey.Finance, PageAccessControl.PERMISSION);
        addEntry("private/finance/accounts", AccountsView.class, LabelKey.Accounts, PageAccessControl.PERMISSION);
        addEntry("private/finance/payroll", PayrollView.class, LabelKey.Payroll, PageAccessControl.PERMISSION);
    }
}
