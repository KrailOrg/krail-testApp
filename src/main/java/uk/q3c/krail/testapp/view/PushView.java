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
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.v7.data.Property.ValueChangeEvent;
import com.vaadin.v7.data.Property.ValueChangeListener;
import com.vaadin.v7.shared.ui.label.ContentMode;
import com.vaadin.v7.ui.CheckBox;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.v7.ui.Label;
import com.vaadin.v7.ui.TextField;
import uk.q3c.krail.config.ApplicationConfiguration;
import uk.q3c.krail.config.config.ConfigKeys;
import uk.q3c.krail.core.push.Broadcaster;
import uk.q3c.krail.core.vaadin.ID;
import uk.q3c.krail.core.view.component.BroadcastMessageLog;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.testapp.i18n.LabelKey;

import java.util.Optional;

public class PushView extends ViewBaseGrid {

    private final Broadcaster broadcaster;
    private final BroadcastMessageLog messageLog;
    private final ApplicationConfiguration applicationConfiguration;
    private TextField groupInput;
    private Label infoArea;
    private HorizontalLayout inputLayout;
    private TextField messageInput;
    private CheckBox pushEnabled;
    private Button sendButton;

    @Inject
    protected PushView(Broadcaster broadcaster, BroadcastMessageLog messageLog, ApplicationConfiguration applicationConfiguration, Translate translate) {
        super(translate);
        this.broadcaster = broadcaster;
        this.messageLog = messageLog;
        this.applicationConfiguration = applicationConfiguration;
        nameKey = LabelKey.Push;
    }


    @Override
    public void doBuild(ViewChangeBusMessage busMessage) {
        super.doBuild(busMessage);
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
        pushEnabled.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                applicationConfiguration.setProperty(ConfigKeys.SERVER_PUSH_ENABLED, event.getProperty()
                                                                                          .getValue());
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

        getGrid().setComponentAlignment(pushEnabled, Alignment.MIDDLE_CENTER);
        getGrid().setComponentAlignment(inputLayout, Alignment.MIDDLE_CENTER);
    }


    @Override
    public void setIds() {
        super.setIds();
        getGrid().setId(ID.getId(Optional.empty(), this, getGrid()));
        sendButton.setId(ID.getId(Optional.of("send"), this, sendButton));
        groupInput.setId(ID.getId(Optional.of("group"), this, groupInput));
        messageInput.setId(ID.getId(Optional.of("message"), this, messageInput));
        messageLog.setId(ID.getId(Optional.empty(), this, messageLog));
        pushEnabled.setId(ID.getId(Optional.empty(), this, pushEnabled));
    }


}
