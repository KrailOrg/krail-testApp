package uk.q3c.krail.testapp.view;

import com.google.inject.Inject;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.q3c.krail.core.ui.ScopedUI;
import uk.q3c.krail.core.view.KrailViewChangeEvent;
import uk.q3c.krail.core.view.ViewBase;
import uk.q3c.krail.testapp.persist.*;
import uk.q3c.util.ID;

import java.util.Optional;

/**
 * Created by David Sowerby on 29/12/14.
 */

public class JpaView extends ViewBase implements Button.ClickListener {
    private static Logger log = LoggerFactory.getLogger(JpaView.class);
    //    private final EntityManagerProvider entityManagerProvider1;
    //    private final EntityManagerProvider entityManagerProvider2;
    private final GenericJpaDao dao1;
    private final GenericJpaDao dao2;
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
    protected JpaView(GenericJpaDaoProvider daoProvider, JpaContainerProvider containerProvider) {
        this.containerProvider = containerProvider;
        this.dao1 = daoProvider.getDao(Jpa1.class);
        this.dao2 = daoProvider.getDao(Jpa2.class);
    }

    public Table getTable1() {
        return table1;
    }

    public Table getTable2() {
        return table2;
    }

    /**
     * Called after the view itself has been constructed but before {@link #buildView(KrailViewChangeEvent)} is
     * called. Typically checks whether a valid URI parameters are being passed to the view, or uses the URI
     * parameters to set up some configuration which affects the way the view is presented.
     *
     * @param event
     *         contains information about the change to this View
     */
    @Override
    public void beforeBuild(KrailViewChangeEvent event) {

    }

    /**
     * Builds the UI components of the view.  MUST set the root component of the View (returned by {@link
     * #getRootComponent()}, which is used to insert into the {@link ScopedUI} view area. The view implementation may
     * need to check whether components have already been constructed, as this method may be called when the View is
     * selected again after initial construction.
     *
     * @param event
     *         contains information about the change to this View
     */
    @Override

    public void buildView(KrailViewChangeEvent event) {
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
            dao1.persist(widget);
            refresh(1);
        } else {
            Widget widget = new Widget();
            widget.setName("b" + count2++);
            widget.setDescription("b");
            dao2.persist(widget);
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
