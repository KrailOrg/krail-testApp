package uk.q3c.krail.testapp.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import uk.q3c.krail.core.ui.ScopedUI;
import uk.q3c.krail.core.view.KrailViewChangeEvent;
import uk.q3c.krail.core.view.ViewBase;

/**
 * Created by David Sowerby on 29/12/14.
 */
public class JpaView extends ViewBase {
    private Button saveBtn1;
    private Button saveBtn2;

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
        saveBtn2 = new Button("persist 2");
        layout.addComponent(saveBtn1);
        layout.addComponent(saveBtn2);
        setRootComponent(layout);
    }
}
