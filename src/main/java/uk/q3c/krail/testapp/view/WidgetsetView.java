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


import java.util.Arrays;

import com.google.inject.Inject;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.risto.stepper.IntStepper;
import org.vaadin.spinkit.Spinner;
import org.vaadin.spinkit.shared.SpinnerType;
import uk.q3c.krail.core.view.ViewBase;
import uk.q3c.krail.core.view.component.AfterViewChangeBusMessage;
import uk.q3c.krail.core.view.component.AssignComponentId;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.testapp.i18n.Caption;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.util.guice.SerializationSupport;

public class WidgetsetView extends ViewBase {
    private static Logger log = LoggerFactory.getLogger(WidgetsetView.class);
    @AssignComponentId(assign = false, drilldown = false)
    private Panel buttonPanel;
    private Label infoArea;
    private IntStepper stepper;
    private Spinner spinner;
    private Button changeSpinnerType;
    @Caption(caption = LabelKey.id, description = LabelKey.id)
    private Label param1;
    @Caption(caption = LabelKey.age, description = LabelKey.age)
    private Label param2;

    @Inject
    protected WidgetsetView(SessionObject sessionObject, Translate translate, SerializationSupport serializationSupport) {
        super(translate, serializationSupport);
        nameKey = LabelKey.Widgetset;
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


        spinner = new Spinner(SpinnerType.FOLDING_CUBE).large();
        verticalLayout.addComponent(spinner);

        changeSpinnerType = new Button("Change Spinner Type");
        changeSpinnerType.addClickListener(e -> spinner.setType(SpinnerType.WAVE));
        verticalLayout.addComponent(changeSpinnerType);

        stepper = new IntStepper("Stepper");
        stepper.setValue(5);
        verticalLayout.addComponent(stepper);

        infoArea = new Label();
        infoArea.setContentMode(ContentMode.HTML);
        infoArea.setSizeFull();
        infoArea.setValue("These components are used purely to ensure that the Widgetset has compiled and included "
                + "add-ons");
        grid.addComponent(infoArea, 0, 1, 1, 1);

        param1 = new Label();
        param2 = new Label();
        VerticalLayout parameters = new VerticalLayout(param1, param2);
        grid.addComponent(parameters, 0, 2, 0, 2);
    }

    public GridLayout getGrid() {
        return (GridLayout) getRootComponent();
    }

    @Override
    protected void loadData(AfterViewChangeBusMessage busMessage) {
        super.loadData(busMessage);
        param1.setValue(busMessage.getToState().getParameters().get("id"));
        param2.setValue(busMessage.getToState().getParameters().get("age"));
    }
}
