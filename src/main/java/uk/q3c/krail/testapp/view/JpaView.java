package uk.q3c.krail.testapp.view;

import com.google.inject.Inject;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.apache.onami.persist.Transactional;
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
    private Button saveBtn1;
    private Button saveBtn2;
    private Table table1;
    private Table table2;

    @Inject
    protected JpaView(GenericJpaDaoProvider daoProvider) {
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
     * Called after the view itself has been constructed but before {@link #buildView(KrailViewChangeEvent)} is called.
     * Typically checks
     * whether a valid URI parameters are being passed to the view, or uses the URI parameters to set up some
     * configuration which affects the way the view is presented.
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
    @Transactional // because of JPAContainer
    public void buildView(KrailViewChangeEvent event) {
        VerticalLayout layout = new VerticalLayout();
        saveBtn1 = new Button("persist 1");
        saveBtn1.addClickListener(this);
        saveBtn2 = new Button("persist 2");
        saveBtn2.addClickListener(this);

        //        EntityManager em1 = entityManagerProvider1.get();
        //        log.debug("Entity Manager 1 is open: " + em1.isOpen());
        //        JPAContainer<Widget> jpa1data = JPAContainerFactory.make(Widget.class, em1);
        //        JPAContainer<Widget> jpa2data = JPAContainerFactory.make(Widget.class, entityManagerProvider2.get());
        //        table1 = new Table("Table 1", jpa1data);
        //        table2 = new Table("Table 2", jpa2data);

        //        layout.addComponent(table1);
        layout.addComponent(saveBtn1);
        //        layout.addComponent(table2);
        layout.addComponent(saveBtn2);
        setRootComponent(layout);
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
            widget.setName("a");
            widget.setDescription("a");
            dao1.persist(widget);
        } else {
            Widget widget = new Widget();
            widget.setName("b");
            widget.setDescription("b");
            dao2.persist(widget);
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
    }

    /**
     private final Logger LOG = LoggerFactory.getLogger(HistorieContainer.class);

     private final GuiceEntityManagerProvider emprovider;


     @Inject public HistorieContainer(final GuiceEntityManagerProvider inject) {
     super(Historie.class);

     this.emprovider = inject;

     final EntityProvider<Historie> jpaProvider = new CachingBatchableLocalEntityProvider<Historie>(Historie.class);
     jpaProvider.setEntityManagerProvider(this.emprovider);
     this.LOG.debug("Inject HistorieContainer... EntityProvider ist: " + jpaProvider);
     setEntityProvider(jpaProvider);
     setAutoCommit(false);
     // setWriteThrough(true);
     }






     import com.vaadin.addon.jpacontainer.EntityManagerProvider;

     public class GuiceEntityManagerProvider implements EntityManagerProvider {

     private final Logger LOG = LoggerFactory.getLogger(GuiceEntityManagerProvider.class);

     private Provider<EntityManager> emp = null;

     @Inject public GuiceEntityManagerProvider(final Provider<EntityManager> inject) {
     this.emp = inject;
     }

     @Override public EntityManager getEntityManager() {
     this.LOG.trace("Return new EntityManager");
     return this.emp.get();
     }
     }
     */
}
