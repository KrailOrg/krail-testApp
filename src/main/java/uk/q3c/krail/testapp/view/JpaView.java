package uk.q3c.krail.testapp.view;

import com.google.inject.Inject;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.apache.onami.persist.EntityManagerProvider;
import uk.q3c.krail.core.ui.ScopedUI;
import uk.q3c.krail.core.view.KrailViewChangeEvent;
import uk.q3c.krail.core.view.ViewBase;
import uk.q3c.krail.testapp.persist.GenericJpaDao;
import uk.q3c.krail.testapp.persist.Jpa1;
import uk.q3c.krail.testapp.persist.Jpa2;
import uk.q3c.krail.testapp.persist.Widget;

/**
 * Created by David Sowerby on 29/12/14.
 */
public class JpaView extends ViewBase implements Button.ClickListener {
    private final EntityManagerProvider entityManagerProvider1;
    private final EntityManagerProvider entityManagerProvider2;
    private final GenericJpaDao dao1;
    private final GenericJpaDao dao2;
    private Button saveBtn1;
    private Button saveBtn2;

    @Inject
    protected JpaView(@Jpa1 EntityManagerProvider entityManagerProvider1, @Jpa2 EntityManagerProvider
            entityManagerProvider2, GenericJpaDao dao1, GenericJpaDao dao2) {
        this.entityManagerProvider1 = entityManagerProvider1;
        this.entityManagerProvider2 = entityManagerProvider2;
        this.dao1 = dao1;
        this.dao2 = dao2;
        dao1.setEntityManagerProvider(entityManagerProvider1);
        dao2.setEntityManagerProvider(entityManagerProvider2);
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
    public void buildView(KrailViewChangeEvent event) {
        VerticalLayout layout = new VerticalLayout();
        saveBtn1 = new Button("persist 1");
        saveBtn1.addClickListener(this);
        saveBtn2 = new Button("persist 2");
        saveBtn2.addClickListener(this);
        layout.addComponent(saveBtn1);
        layout.addComponent(saveBtn2);
        setRootComponent(layout);
    }

    /**
     * Called when a {@link Button} has been clicked. A reference to the
     * button is given by {@link ClickEvent#getButton()}.
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
}
