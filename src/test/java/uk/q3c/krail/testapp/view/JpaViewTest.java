package uk.q3c.krail.testapp.view;

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
        //when
        pageObject.saveButton(1)
                  .click();
        pageObject.saveButton(2)
                  .click();
        //then
        assertThat(true).isFalse();
    }
}
