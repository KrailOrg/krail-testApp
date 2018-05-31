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

package uk.q3c.krail.testapp;

import com.google.inject.Inject;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.ErrorHandler;
import uk.q3c.krail.core.guice.uiscope.UIKey;
import uk.q3c.krail.core.i18n.I18NProcessor;
import uk.q3c.krail.core.navigate.Navigator;
import uk.q3c.krail.core.push.Broadcaster;
import uk.q3c.krail.core.push.KrailPushConfiguration;
import uk.q3c.krail.core.push.PushMessageRouter;
import uk.q3c.krail.core.ui.ApplicationTitle;
import uk.q3c.krail.core.ui.DefaultApplicationUI;
import uk.q3c.krail.core.user.notify.InformationNotificationMessage;
import uk.q3c.krail.core.user.notify.VaadinNotification;
import uk.q3c.krail.core.view.component.ApplicationHeader;
import uk.q3c.krail.core.view.component.ApplicationLogo;
import uk.q3c.krail.core.view.component.Breadcrumb;
import uk.q3c.krail.core.view.component.LocaleSelector;
import uk.q3c.krail.core.view.component.MessageBar;
import uk.q3c.krail.core.view.component.SubPagePanel;
import uk.q3c.krail.core.view.component.UserNavigationMenu;
import uk.q3c.krail.core.view.component.UserNavigationTree;
import uk.q3c.krail.core.view.component.UserStatusPanel;
import uk.q3c.krail.i18n.CurrentLocale;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.option.Option;
import uk.q3c.krail.testapp.view.SessionObject;
import uk.q3c.util.guice.SerializationSupport;

/**
 * The UI class used in this test application for Krail
 *
 * @author David Sowerby
 */
//@Theme(ValoTheme.THEME_NAME)
@Theme("krail")
@Push
public class TestAppUI extends DefaultApplicationUI {

    @Inject
    protected TestAppUI(Navigator navigator, ErrorHandler errorHandler,
                        ApplicationLogo logo, ApplicationHeader header, UserStatusPanel userStatus,
                        UserNavigationMenu menu, UserNavigationTree navTree, Breadcrumb breadcrumb,
                        SubPagePanel subpage, MessageBar messageBar, Broadcaster broadcaster,
                        PushMessageRouter pushMessageRouter, SessionObject sessionObject,
                        ApplicationTitle applicationTitle, Translate translate, CurrentLocale currentLocale, I18NProcessor translator, LocaleSelector localeSelector, VaadinNotification vaadinNotification, Option option, SerializationSupport serializationSupport, KrailPushConfiguration pushConfiguration) {
        super(navigator, errorHandler, logo, header, userStatus, menu, navTree, breadcrumb, subpage, messageBar, broadcaster,
                pushMessageRouter, applicationTitle, translate, currentLocale, translator, localeSelector, vaadinNotification, option, serializationSupport, pushConfiguration);

    }

    @Override
    protected void processBroadcastMessage(String group, String message, UIKey uiKey, int messageId) {
        super.processBroadcastMessage(group, message, uiKey, messageId);
        getMessageBar().informationMessage(new InformationNotificationMessage(group + ':' + message));
    }

}
