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
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.q3c.krail.core.i18n.LabelKey;
import uk.q3c.krail.core.i18n.MessageKey;
import uk.q3c.krail.core.option.OptionPopup;
import uk.q3c.krail.core.option.VaadinOptionContext;
import uk.q3c.krail.core.user.notify.UserNotifier;
import uk.q3c.krail.core.view.ViewBase;
import uk.q3c.krail.core.view.component.AssignComponentId;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.eventbus.GlobalMessageBus;
import uk.q3c.krail.eventbus.SubscribeTo;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.option.Option;
import uk.q3c.krail.option.OptionChangeMessage;
import uk.q3c.krail.option.OptionKey;
import uk.q3c.krail.option.persist.OptionCache;
import uk.q3c.krail.option.persist.OptionSource;
import uk.q3c.krail.testapp.TestAppUI;
import uk.q3c.krail.testapp.i18n.DescriptionKey;

import javax.annotation.Nonnull;

@SuppressFBWarnings({"LSC_LITERAL_STRING_COMPARISON", "FCBL_FIELD_COULD_BE_LOCAL"})
@Listener
@SubscribeTo(GlobalMessageBus.class)
public class NotificationsView extends ViewBase implements VaadinOptionContext {
    private static final OptionKey<Boolean> errorButtonVisible = new OptionKey<>(Boolean.TRUE, NotificationsView.class, LabelKey.Error, MessageKey
            .Button_is_Visible);
    private static final OptionKey<Boolean> infoButtonVisible = new OptionKey<>(Boolean.TRUE, NotificationsView.class, LabelKey.Information, MessageKey
            .Button_is_Visible);
    private static final OptionKey<Boolean> warningButtonVisible = new OptionKey<>(Boolean.TRUE, NotificationsView.class, LabelKey.Warning, MessageKey
            .Button_is_Visible);
    private static Logger log = LoggerFactory.getLogger(NotificationsView.class);
    private final UserNotifier userNotifier;
    private final Translate translate;
    protected GridLayout grid;
    @AssignComponentId(assign = false, drilldown = false)
    private Panel buttonPanel;
    private Button clearOptionStoreButton;
    private Button errorButton;
    private Label infoArea;
    private Button infoButton;
    private Option option;
    private OptionCache optionCache;
    private OptionSource optionDaoProvider;
    private OptionPopup optionPopup;
    private Button systemLevelOptionButton;
    @AssignComponentId(assign = false, drilldown = false)
    private Button uiOptionsButton;
    private Button viewOptionsButton;
    private Button warnButton;

    @Inject
    protected NotificationsView(UserNotifier userNotifier, Translate translate, Option option, OptionPopup optionPopup, OptionCache optionCache, OptionSource optionDaoProvider) {
        super(translate);
        this.userNotifier = userNotifier;
        this.translate = translate;
        this.option = option;
        this.optionPopup = optionPopup;
        this.optionCache = optionCache;
        this.optionDaoProvider = optionDaoProvider;
        nameKey = LabelKey.Notifications;
    }


    @Override
    public void doBuild(ViewChangeBusMessage busMessage) {
        buttonPanel = new Panel();
        VerticalLayout verticalLayout = new VerticalLayout();
        buttonPanel.setContent(verticalLayout);
        grid = new GridLayout(3, 4);

        grid.addComponent(buttonPanel, 1, 2);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 0.400f);
        grid.setColumnExpandRatio(1, 0.20f);
        grid.setColumnExpandRatio(2, 0.40f);

        grid.setRowExpandRatio(0, 0.05f);
        grid.setRowExpandRatio(1, 0.15f);
        grid.setRowExpandRatio(2, 0.4f);
        grid.setRowExpandRatio(3, 0.15f);

        errorButton = new Button("Fake an error");
        errorButton.setWidth("100%");
        errorButton.addClickListener(c -> userNotifier.notifyError(MessageKey.Service_not_Started, "Fake Service"));

        verticalLayout.addComponent(errorButton);

        warnButton = new Button("Fake a warning");
        warnButton.setWidth("100%");
        warnButton.addClickListener(c -> userNotifier.notifyWarning(MessageKey.Service_not_Started, "Fake Service"));
        verticalLayout.addComponent(warnButton);

        infoButton = new Button("Fake user information");
        infoButton.setWidth("100%");
        infoButton.addClickListener(c -> userNotifier.notifyInformation(MessageKey.Service_not_Started, "Fake Service"));
        verticalLayout.addComponent(infoButton);

        viewOptionsButton = new Button("Show options");
        viewOptionsButton.addClickListener(c -> optionPopup.popup(this, LabelKey.Notification_Options));
        viewOptionsButton.setWidth("100%");
        verticalLayout.addComponent(viewOptionsButton);


        uiOptionsButton = new Button("Show UI options");
        uiOptionsButton.addClickListener(c -> optionPopup.popup((TestAppUI) UI.getCurrent(), LabelKey.Application_Options));
        uiOptionsButton.setWidth("100%");
        verticalLayout.addComponent(uiOptionsButton);

        systemLevelOptionButton = new Button("Set system level option - info button not visible");
        systemLevelOptionButton.addClickListener(event -> {
            option.set(infoButtonVisible, 1, Boolean.FALSE);
            refreshFromOptions();
        });
        systemLevelOptionButton.setWidth("100%");
        verticalLayout.addComponent(systemLevelOptionButton);

        clearOptionStoreButton = new Button("clear option store");
        clearOptionStoreButton.setWidth("100%");
        clearOptionStoreButton.addClickListener(event -> {
            optionDaoProvider.getActiveDao()
                    .clear();
            optionCache.clear();
            refreshFromOptions();
        });
        verticalLayout.addComponent(clearOptionStoreButton);

        infoArea = new Label();
        infoArea.setContentMode(ContentMode.HTML);
        infoArea.setSizeFull();
        infoArea.setValue(translate.from(DescriptionKey.Notifications));
        grid.addComponent(infoArea, 0, 1, 1, 1);
        setRootComponent(grid);
        refreshFromOptions();

    }


    @Handler
    public void optionValueChanged(OptionChangeMessage<?> msg) {

        if (msg.getOptionKey().getContext().equals(this.getClass())) {
            log.info("Option value changed - refreshing from options, new value is: {}", msg.getNewValue());
            refreshFromOptions();
        } else {
            log.info("Option value changed - not relevant to this view");
        }
    }

    private void refreshFromOptions() {
        boolean infoVis = option.get(infoButtonVisible);
        log.info("refreshing - info button is visible = {}, option key is: {}", infoVis, infoButtonVisible.compositeKey());
        infoButton.setVisible(option.get(infoButtonVisible));
        warnButton.setVisible(option.get(warningButtonVisible));
        errorButton.setVisible(option.get(errorButtonVisible));
    }



    /**
     * Returns the {@link Option} instance being used by this context
     *
     * @return the {@link Option} instance being used by this context
     */
    @Nonnull
    @Override
    public Option optionInstance() {
        return option;
    }


}
