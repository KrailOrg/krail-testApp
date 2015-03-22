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
        startDriver();
        pause(1500); // without a delay, intermittently fails, possibly because of database connection
    }

    @Test
    public void persist() {
        //given
        navigateTo("jpa");
        pageObject = new JpaViewPageObject(this);
        int startCount1 = countFor(1);
        int startCount2 = countFor(2);

        //when
        pause(500);
        pageObject.saveButton(1)
                  .click();
        pause(500);
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
