package uk.q3c.krail.testApp.test;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import uk.q3c.krail.testapp.view.JpaView;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.PageObject;

import java.util.Optional;

/**
 * Created by David Sowerby on 02/01/15.
 */
public class JpaViewPageObject extends PageObject {

    public JpaViewPageObject(KrailTestBenchTestCase parentCase) {
        super(parentCase);
    }

    public ButtonElement saveButton(int index) {
        return element(ButtonElement.class, Optional.of(index), JpaView.class, Button.class);
    }

    public TableElement table(int index) {
        return element(TableElement.class, Optional.of(index), JpaView.class, Table.class);
    }

    public LabelElement label(int index) {
        return element(LabelElement.class, Optional.of(index), JpaView.class, Label.class);
    }

}
