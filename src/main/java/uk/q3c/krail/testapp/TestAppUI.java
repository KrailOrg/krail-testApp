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
import com.google.inject.Provider;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.ErrorHandler;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import uk.q3c.krail.core.guice.uiscope.UIKey;
import uk.q3c.krail.core.i18n.I18NProcessor;
import uk.q3c.krail.core.monitor.PageLoadingMessage;
import uk.q3c.krail.core.monitor.PageReadyMessage;
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
import uk.q3c.krail.core.view.component.LocaleSelector;
import uk.q3c.krail.core.view.component.MessageBar;
import uk.q3c.krail.core.view.component.PageNavigationPanel;
import uk.q3c.krail.core.view.component.UserNavigationMenu;
import uk.q3c.krail.core.view.component.UserNavigationTree;
import uk.q3c.krail.core.view.component.UserStatusPanel;
import uk.q3c.krail.eventbus.MessageBus;
import uk.q3c.krail.i18n.CurrentLocale;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.option.Option;
import uk.q3c.util.guice.SerializationSupport;

import java.net.URI;

/**
 * The UI class used in this test application for Krail
 *
 * @author David Sowerby
 */
//@Theme(ValoTheme.THEME_NAME)
@Theme("krail")
@Push
@Listener
public class TestAppUI extends DefaultApplicationUI {

    private Label pageStatus = new Label();
    private Button newTab = new Button("new tab");
    private transient MessageBus messageBus;

    @Inject
    protected TestAppUI(Navigator navigator, ErrorHandler errorHandler,
                        ApplicationLogo logo, ApplicationHeader header, UserStatusPanel userStatus,
                        UserNavigationMenu menu, UserNavigationTree navTree, Provider<PageNavigationPanel> pageNavigationPanelProvider,
                        MessageBar messageBar, Broadcaster broadcaster,
                        PushMessageRouter pushMessageRouter,
                        ApplicationTitle applicationTitle, Translate translate, CurrentLocale currentLocale, I18NProcessor translator, LocaleSelector localeSelector, VaadinNotification vaadinNotification, Option option, SerializationSupport serializationSupport, KrailPushConfiguration pushConfiguration, MessageBus messageBus) {
        super(navigator, errorHandler, logo, header, userStatus, menu, navTree, pageNavigationPanelProvider, messageBar, broadcaster,
                pushMessageRouter, applicationTitle, translate, currentLocale, translator, localeSelector, vaadinNotification, option, serializationSupport, pushConfiguration);

        this.messageBus = messageBus;
        messageBus.subscribe(this);
        this.viewDisplayPanelSizeFull = false;
    }

    @Override
    protected void processBroadcastMessage(String group, String message, UIKey uiKey, int messageId) {
        super.processBroadcastMessage(group, message, uiKey, messageId);
        getMessageBar().informationMessage(new InformationNotificationMessage(group + ':' + message));
    }

    @Override
    protected Component headerRow() {
        HorizontalLayout headerRow = new HorizontalLayout(header, pageStatus, newTab, localeCombo, userStatus);
        pageStatus.setValue("Loading");


        EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener()
                .popupBlockerWorkaround(true);

        newTab.addClickListener(e -> {
            URI currentLocation = this.getPage().getLocation();
            opener.open(currentLocation.toString());
        });
        opener.extend(newTab);
        newTab.click(); // pre-loads the connector


        headerRow.setWidth("100%");
        headerRow.setExpandRatio(header, 1f);
        return headerRow;
    }

    @Handler
    public void pageLoading(PageLoadingMessage message) {
        if (message.getUiKey() == this.getInstanceKey()) {
            pageStatus.setValue("Loading");
        }
    }

    @Handler
    public void pageReady(PageReadyMessage message) {
        if (message.getUiKey() == this.getInstanceKey()) {
            pageStatus.setValue("Ready");
        }
    }


}
