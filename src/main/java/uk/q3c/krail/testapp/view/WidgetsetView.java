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
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.risto.stepper.IntStepper;
import uk.q3c.krail.core.view.ViewBase;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.util.ID;

import java.util.Optional;

public class WidgetsetView extends ViewBase {
    private static Logger log = LoggerFactory.getLogger(WidgetsetView.class);
    protected MessageBox messageBox;
    private Panel buttonPanel;
    private Label infoArea;
    private Button popupButton;
    private IntStepper stepper;

    @Inject
    protected WidgetsetView(SessionObject sessionObject) {
        super();
        log.debug("Constructor injecting with session object");
    }




    @Override
    public void doBuild(ViewChangeBusMessage event) {
        buttonPanel = new Panel();
        VerticalLayout verticalLayout = new VerticalLayout();
        buttonPanel.setContent(verticalLayout);

        setRootComponent(new GridLayout(3, 4));

        GridLayout grid = getGrid();

        grid.addComponent(buttonPanel, 1, 2);
        grid.setSizeFull();
        grid.setColumnExpandRatio(0, 0.400f);
        grid.setColumnExpandRatio(1, 0.20f);
        grid.setColumnExpandRatio(2, 0.40f);

        grid.setRowExpandRatio(0, 0.05f);
        grid.setRowExpandRatio(1, 0.15f);
        grid.setRowExpandRatio(2, 0.4f);
        grid.setRowExpandRatio(3, 0.15f);

        popupButton = new Button("Popup message box");
        popupButton.setWidth("100%");
        popupButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                messageBox = MessageBox.showPlain(Icon.INFO, "Example 1", "Hello World!", ButtonId.OK);
            }
        });
        verticalLayout.addComponent(popupButton);

        stepper = new IntStepper("Stepper");
        stepper.setValue(5);
        verticalLayout.addComponent(stepper);

        infoArea = new Label();
        infoArea.setContentMode(ContentMode.HTML);
        infoArea.setSizeFull();
        infoArea.setValue("These components are used purely to ensure that the Widgetset has compiled and included "
                + "add-ons");
        grid.addComponent(infoArea, 0, 1, 1, 1);
    }

    public GridLayout getGrid() {
        return (GridLayout) getRootComponent();
    }

    @Override
    public void setIds() {
        super.setIds();
        getGrid().setId(ID.getId(Optional.empty(), this, getGrid()));
        popupButton.setId(ID.getId(Optional.of("popup"), this, popupButton));
        stepper.setId(ID.getId(Optional.empty(), this, stepper));
    }
}
