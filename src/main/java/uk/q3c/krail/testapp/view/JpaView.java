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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.*;
import uk.q3c.krail.core.view.ViewBase;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.persist.jpa.*;
import uk.q3c.krail.testapp.persist.Jpa1;
import uk.q3c.krail.testapp.persist.Jpa2;
import uk.q3c.krail.testapp.persist.Widget;
import uk.q3c.util.ID;

import java.util.Optional;

/**
 * A view for testing JPA
 * <p>
 * Created by David Sowerby on 29/12/14.
 */

public class JpaView extends ViewBase implements Button.ClickListener {
    private final JpaBlockDao blockDao;
    private final JpaStatementDao statementDao;
    private final JpaContainerProvider containerProvider;
    private int count1;
    private int count2;
    private Label countLabel1;
    private Label countLabel2;
    private JPAContainer<Widget> jpa1Container;
    private JPAContainer<Widget> jpa2Container;
    private Button saveBtn1;
    private Button saveBtn2;
    private Table table1;
    private Table table2;

    @Inject
    protected JpaView(@Jpa1 Provider<StandardJpaBlockDao> blockDaoProvider, @Jpa2 Provider<StandardJpaStatementDao> statementDaoProvider,
                      JpaContainerProvider containerProvider) {
        this.containerProvider = containerProvider;
        this.blockDao = blockDaoProvider.get();
        this.statementDao = statementDaoProvider.get();
    }



    /**
     * {@inheritDoc}
     */
    @Override

    public void doBuild(ViewChangeBusMessage event) {
        VerticalLayout rootLayout = new VerticalLayout();
        saveBtn1 = new Button("persist 1");
        saveBtn1.addClickListener(this);
        saveBtn2 = new Button("persist 2");
        saveBtn2.addClickListener(this);

        jpa1Container = containerProvider.get(Jpa1.class, Widget.class, JpaContainerProvider.ContainerType.CACHED);
        jpa2Container = containerProvider.get(Jpa2.class, Widget.class, JpaContainerProvider.ContainerType.CACHED);
        table1 = new Table("Table 1", jpa1Container);
        table2 = new Table("Table 2", jpa2Container);

        countLabel1 = new Label();
        countLabel2 = new Label();


        countLabel1.setWidth("60px");
        countLabel2.setWidth("60px");


        HorizontalLayout hl1 = new HorizontalLayout(saveBtn1, countLabel1);
        HorizontalLayout hl2 = new HorizontalLayout(saveBtn2, countLabel2);

        VerticalLayout tableLayout1 = new VerticalLayout(table1, hl1);
        VerticalLayout tableLayout2 = new VerticalLayout(table2, hl2);

        rootLayout.addComponent(tableLayout1);
        rootLayout.addComponent(tableLayout2);
        refresh(1);
        refresh(2);
        setRootComponent(rootLayout);
    }

    private void refresh(int index) {
        switch (index) {
            case 1:
                jpa1Container.refresh();
                countLabel1.setValue(Integer.toString(jpa1Container.getItemIds()
                                                                   .size()));
                break;
            case 2:
                jpa2Container.refresh();
                countLabel2.setValue(Integer.toString(jpa2Container.getItemIds()
                                                                   .size()));
        }
    }

    /**
     * Called when a {@link Button} has been clicked. A reference to the
     * button is given by {@link Button.ClickEvent#getButton()}.
     *
     * @param event
     *         An event containing information about the click.
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {
        Button btn = event.getButton();
        if (btn == saveBtn1) {
            Widget widget = new Widget();
            widget.setName("a" + count1++);
            widget.setDescription("a");
            Widget widget1 = new Widget();
            widget1.setName("aa" + count1++);
            widget1.setDescription("aa");
            blockDao.transact(d -> {
                d.save(widget);
                d.save(widget1);
            });
            refresh(1);
        } else {
            Widget widget = new Widget();
            widget.setName("b" + count2++);
            widget.setDescription("b");
            statementDao.save(widget);
            refresh(2);
        }
    }

    /**
     * You only need to override / implement this method if you are using TestBench, or another testing tool which
     * looks for debug ids. If you do override it to add your own subclass ids, make sure you call super
     */
    @Override
    protected void setIds() {
        super.setIds();
        saveBtn1.setId(ID.getId(Optional.of(1), this, saveBtn1));
        saveBtn2.setId(ID.getId(Optional.of(2), this, saveBtn2));
        table1.setId(ID.getId(Optional.of(1), this, table1));
        table2.setId(ID.getId(Optional.of(2), this, table2));
        countLabel1.setId(ID.getId(Optional.of(1), this, countLabel1));
        countLabel2.setId(ID.getId(Optional.of(2), this, countLabel2));
    }

}
