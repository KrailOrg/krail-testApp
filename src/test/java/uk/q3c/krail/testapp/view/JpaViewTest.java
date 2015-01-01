package uk.q3c.krail.testapp.view;

import org.junit.Before;
import org.junit.Test;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by David Sowerby on 01/01/15.
 */
public class JpaViewTest extends KrailTestBenchTestCase {

    @Before
    public void setUp() throws Exception {
        appContext = "krail-testapp";
        driver.get(rootUrl());

    }

    @Test
    public void persist() {
        //given

        //when

        //then
        assertThat(true).isFalse();
    }
}
