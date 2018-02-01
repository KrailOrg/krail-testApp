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
import com.google.inject.Provider;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.apache.onami.persist.EntityManagerProvider;
import org.apache.onami.persist.Transactional;
import org.apache.onami.persist.UnitOfWork;
import uk.q3c.krail.core.view.ViewBase;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.persist.jpa.common.JpaDao_LongInt;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.krail.testapp.persist.Jpa1;
import uk.q3c.krail.testapp.persist.Jpa2;
import uk.q3c.krail.testapp.persist.Widget;

import javax.persistence.EntityManager;

/**
 * A view for testing JPA
 * <p>
 * Created by David Sowerby on 29/12/14.
 */

public class JpaView extends ViewBase implements Button.ClickListener {
    private int count1;
    private int count2;
    private Label countLabelFromContainer1;
    private Label countLabelFromContainer2;
    private Label countLabelFromDao1;
    private Label countLabelFromDao2;
    private Provider<JpaDao_LongInt> daoProvider1;
    private Provider<JpaDao_LongInt> daoProvider2;
    private EntityManagerProvider entityManagerProvider1;
    private Button saveBtn1;
    private Button saveBtn2;
    private Button saveBtn3;
    private Grid<Widget> table1;
    private Grid<Widget> table2;
    private UnitOfWork unitOfWork1;

    @Inject
    protected JpaView(@Jpa1 Provider<JpaDao_LongInt> daoProvider1, @Jpa2 Provider<JpaDao_LongInt> daoProvider2, @Jpa1
            EntityManagerProvider entityManagerProvider1, @Jpa1 UnitOfWork unitOfWork1, Translate translate) {
        super(translate);
        this.daoProvider1 = daoProvider1;
        this.daoProvider2 = daoProvider2;
        this.entityManagerProvider1 = entityManagerProvider1;
        this.unitOfWork1 = unitOfWork1;
        nameKey = LabelKey.Jpa;
    }


    /**
     * {@inheritDoc}
     */
    @Override

    public void doBuild(ViewChangeBusMessage event) {

        VerticalLayout rootLayout = new VerticalLayout();
        saveBtn1 = new Button("common 1");
        saveBtn1.addClickListener(this);
        saveBtn2 = new Button("common 2");
        saveBtn2.addClickListener(this);
        saveBtn3 = new Button("common 3");
        saveBtn3.addClickListener(this);

        table1 = new Grid<>("Table 1");
        table2 = new Grid<>("Table 2");
        table1.addColumn(Widget::getName).setCaption("Name");
        table1.addColumn(Widget::getDescription).setCaption("Description");
        table2.addColumn(Widget::getName).setCaption("Name");
        table2.addColumn(Widget::getDescription).setCaption("Description");


        countLabelFromContainer1 = new Label();
        countLabelFromContainer1.setCaption("container 1 count = ");
        countLabelFromContainer2 = new Label();
        countLabelFromContainer2.setCaption("container 2 count = ");
        countLabelFromDao1 = new Label();
        countLabelFromDao1.setCaption("dao 1 count = ");
        countLabelFromDao2 = new Label();
        countLabelFromDao2.setCaption("dao 2 count = ");
        FormLayout formLayout1 = new FormLayout(countLabelFromContainer1, countLabelFromDao1);
        FormLayout formLayout2 = new FormLayout(countLabelFromContainer2, countLabelFromDao2);

        countLabelFromContainer1.setWidth("160px");
        countLabelFromContainer2.setWidth("160px");


        HorizontalLayout hl1 = new HorizontalLayout(saveBtn1, formLayout1);
        HorizontalLayout hl2 = new HorizontalLayout(saveBtn2, formLayout2);
        HorizontalLayout hl3 = new HorizontalLayout(saveBtn3);

        VerticalLayout tableLayout1 = new VerticalLayout(table1, hl1, hl3);
        VerticalLayout tableLayout2 = new VerticalLayout(table2, hl2);

        rootLayout.addComponent(tableLayout1);
        rootLayout.addComponent(tableLayout2);
        refresh(1);
        refresh(2);
        setRootComponent(new Panel(rootLayout));

    }

    private void refresh(int index) {
        switch (index) {
            case 1:
                table1.setItems(daoProvider1.get().findAll(Widget.class));
                String itemCountLabel = Long.toString(daoProvider1.get().count(Widget.class));
                countLabelFromContainer1.setValue(itemCountLabel);
                countLabelFromDao1.setValue(itemCountLabel);
                break;
            case 2:
                table2.setItems(daoProvider2.get().findAll(Widget.class));
                String itemCountLabel2 = Long.toString(daoProvider2.get().count(Widget.class));
                countLabelFromContainer2.setValue(itemCountLabel2);
                countLabelFromDao2.setValue(itemCountLabel2);
                break;
            default:
                throw new RuntimeException("Invalid index");
        }
    }

    /**
     * Called when a {@link Button} has been clicked. A reference to the
     * button is given by {@link Button.ClickEvent#getButton()}.
     *
     * @param event An event containing information about the click.
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button btn = event.getButton();
        if (btn == saveBtn1) {
            Widget widget = new Widget();
            widget.setName('a' + Integer.toString(count1++));
            widget.setDescription("a");
            Widget widget1 = new Widget();
            widget1.setName("aa" + count1++);


            widget1.setDescription("aa");
            //            UnitOfWork unitOfWork= (UnitOfWork) entityManagerProvider1;
            final boolean unitOfWorkWasInactive = !unitOfWork1.isActive();
            if (unitOfWorkWasInactive) {
                unitOfWork1.begin();
            }
            try {
                EntityManager entityManager = entityManagerProvider1.get();
                entityManager.getTransaction()
                        .begin();
                entityManager.persist(widget);
                entityManager.persist(widget1);
                entityManager.getTransaction()
                        .commit();
            } finally {
                if (unitOfWorkWasInactive) {
                    unitOfWork1.end();
                }
            }

            refresh(1);
            return;
        }
        if (btn == saveBtn2) {
            annotatedMethodUsingDao();
            refresh(2);
        }
        if (btn == saveBtn3) {

            annotatedMethod();
            refresh(1);
        }


    }

    @Transactional
    protected void annotatedMethod() {
        Widget widget = new Widget();
        widget.setName('a' + Integer.toString(count1++));
        widget.setDescription("a");
        final EntityManager entityManager = entityManagerProvider1.get();
        entityManager.persist(widget);
    }

    @Transactional
    protected void annotatedMethodUsingDao() {
        Widget widget = new Widget();
        widget.setName('b' + Integer.toString(count2++));
        widget.setDescription("b");
        daoProvider2.get()
                .save(widget);

    }


}
