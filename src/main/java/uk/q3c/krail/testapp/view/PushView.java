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

import com.google.inject.Inject;
import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.vaadin.addon.ewopener.EnhancedBrowserWindowOpener;
import uk.q3c.krail.config.ApplicationConfiguration;
import uk.q3c.krail.core.push.Broadcaster;
import uk.q3c.krail.core.ui.ScopedUIProvider;
import uk.q3c.krail.core.view.component.BroadcastMessageLog;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.util.guice.SerializationSupport;

import java.net.URI;

public class PushView extends ViewBaseGrid {

    private final Broadcaster broadcaster;
    private final BroadcastMessageLog messageLog;
    private final ApplicationConfiguration applicationConfiguration;
    private final ScopedUIProvider uiProvider;
    private TextField groupInput;
    private Label infoArea;
    private HorizontalLayout inputLayout;
    private TextField messageInput;
    private CheckBox pushEnabled;
    private Button sendButton;
    private Button newTab = new Button("new tab");

    @Inject
    protected PushView(Broadcaster broadcaster, SerializationSupport serializationSupport, BroadcastMessageLog messageLog, ApplicationConfiguration applicationConfiguration, Translate translate, ScopedUIProvider uiProvider) {
        super(translate, serializationSupport);
        this.broadcaster = broadcaster;
        this.messageLog = messageLog;
        this.applicationConfiguration = applicationConfiguration;
        this.uiProvider = uiProvider;
        nameKey = LabelKey.Push;
    }


    @Override
    public void doBuild() {
        super.doBuild();
        groupInput = new TextField("Group");
        groupInput.setWidth("100px");
        messageInput = new TextField("Message");

        sendButton = new Button("Send message");
        sendButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                broadcaster.broadcast(groupInput.getValue(), messageInput.getValue(), getRootComponent());
            }
        });

        inputLayout = new HorizontalLayout(groupInput, messageInput, sendButton);
        inputLayout.setComponentAlignment(sendButton, Alignment.BOTTOM_CENTER);

        pushEnabled = new CheckBox("Push enabled");
        pushEnabled.addValueChangeListener(new HasValue.ValueChangeListener<Boolean>() {

            @Override
            public void valueChange(HasValue.ValueChangeEvent event) {
//                applicationConfiguration.setProperty(PushModule.SERVER_PUSH_ENABLED, event.getValue());
            }

        });
        pushEnabled.setValue(Boolean.TRUE);

        infoArea = new Label();
        infoArea.setContentMode(ContentMode.HTML);
        infoArea.setSizeFull();
        infoArea.setValue("Test using multiple browser tabs or instances");

        setTopCentreCell(pushEnabled);
        setCentreCell(inputLayout);
        setTopLeftCell(infoArea);
        setBottomCentreCell(messageLog);
        setMiddleLeftCell(newTab);

        getGrid().setComponentAlignment(pushEnabled, Alignment.MIDDLE_CENTER);
        getGrid().setComponentAlignment(inputLayout, Alignment.MIDDLE_CENTER);

        prepareTabOpener();
    }

    private void prepareTabOpener() {
        EnhancedBrowserWindowOpener opener = new EnhancedBrowserWindowOpener()
                .popupBlockerWorkaround(true);

        newTab.addClickListener(e -> {
            URI currentLocation = uiProvider.get().getPage().getLocation();
            opener.open(currentLocation.toString());
        });
        opener.extend(newTab);
        newTab.click(); // pre-loads the connector

    }


}
