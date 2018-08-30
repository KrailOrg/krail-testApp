/*
 *
 *  * Copyright (c) 2016. David Sowerby
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations under the License.
 *
 */

package uk.q3c.krail.testapp.view;

import uk.q3c.krail.core.i18n.CommonLabelKey;
import uk.q3c.krail.core.i18n.LabelKey;
import uk.q3c.krail.core.navigate.sitemap.DirectSitemapModule;
import uk.q3c.krail.core.shiro.PageAccessControl;

public class TestAppPages extends DirectSitemapModule {

    public TestAppPages() {
        rootURI("p");
    }

    @Override
    protected void define() {
        addEntry("notifications", LabelKey.Notifications, PageAccessControl.PUBLIC, NotificationsView.class);
        addEntry("widgetset", LabelKey.Message_Box, PageAccessControl.PUBLIC, WidgetsetView.class);
        addEntry("notifications/push", CommonLabelKey.Push, PageAccessControl.PUBLIC, PushView.class);
        addEntry("form", uk.q3c.krail.testapp.i18n.LabelKey.Form, PageAccessControl.PUBLIC, ManualForm.class);
        addEntry("locale", uk.q3c.krail.testapp.i18n.LabelKey.Locale, PageAccessControl.PUBLIC, LocaleChanger.class);
        addEntry("help", CommonLabelKey.Help, PageAccessControl.PUBLIC, HelpView.class);
        addForm("person", uk.q3c.krail.testapp.i18n.LabelKey.Person, PageAccessControl.PUBLIC, PersonFormConfiguration.class);

    }

}