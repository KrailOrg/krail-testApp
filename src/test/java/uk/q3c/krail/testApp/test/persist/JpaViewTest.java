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

package uk.q3c.krail.testApp.test.persist;

import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TableElement;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
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
        // TODO better way of ensuring connection is ready
    }

    @Test
    public void persist() {
        //given
        navigateTo("jpa");
        pageObject = new JpaViewPageObject(this);
        int startCount1 = countFor("container 1");
        int startCount2 = countFor("container 2");

        //when
        pause(1000);
        pageObject.saveButton(1)
                  .click();
        pageObject.saveButton(2)
                  .click();
        pageObject.saveButton(3)
                  .click();
        int newCount1 = countFor("container 1");
        int newCount2 = countFor("container 2");
        //then
        TableElement t1 = pageObject.table(1);
        TableElement t2 = pageObject.table(2);
        assertThat(t1.getRow(0)).isNotNull();
        assertThat(t2.getRow(0)).isNotNull();
        assertThat(newCount1).isEqualTo(startCount1 + 3);
        assertThat(newCount2).isEqualTo(startCount2 + 1);
        assertThat(countFor("dao 1")).isEqualTo(newCount1);
        assertThat(countFor("dao 2")).isEqualTo(newCount2);
    }

    private int countFor(String qualifier) {
        LabelElement label = pageObject.countLabel(qualifier);
        String text = label.getText();
        if (StringUtils.isEmpty(text)) {
            return 0;
        } else {
            return Integer.parseInt(text);
        }
    }
}
