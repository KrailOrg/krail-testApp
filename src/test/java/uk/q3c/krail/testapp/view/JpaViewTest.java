package uk.q3c.krail.testapp.view;

import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TableElement;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import uk.q3c.krail.testApp.test.JpaViewPageObject;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by David Sowerby on 01/01/15.
 */

public class JpaViewTest extends KrailTestBenchTestCase {

    JpaViewPageObject pageObject;



    @Before
    public void setUp() throws Exception {
        appContext = "krail-testapp";
        driver.get(rootUrl());
        pause(500);
    }

    @Test
    public void persist() {
        //given
        navigateTo("jpa");
        pageObject = new JpaViewPageObject(this);
        int startCount1 = countFor(1);
        int startCount2 = countFor(2);

        //when
        pageObject.saveButton(1)
                  .click();
        pageObject.saveButton(2)
                  .click();
        int newCount1 = countFor(1);
        int newCount2 = countFor(2);
        //then
        TableElement t1 = pageObject.table(1);
        TableElement t2 = pageObject.table(2);
        assertThat(t1.getRow(0)).isNotNull();
        assertThat(t2.getRow(0)).isNotNull();
        assertThat(newCount1).isEqualTo(startCount1 + 1);
        assertThat(newCount2).isEqualTo(startCount2 + 1);
    }

    private int countFor(int i) {
        LabelElement label = pageObject.label(i);
        String text = label.getText();
        if (StringUtils.isEmpty(text)) {
            return 0;
        } else {
            return Integer.parseInt(text);
        }
    }
}
