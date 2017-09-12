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
package uk.q3c.krail.testApp.push;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.MessageBarPageObject;

import static org.assertj.core.api.Assertions.assertThat;

public class Push_Functional extends KrailTestBenchTestCase {

    private MessageBarPageObject messageBar = new MessageBarPageObject(this);
    private PushViewPageObject pushView = new PushViewPageObject(this);

    @Before
    public void setup() {
        appContext = "krail-testapp";
//        defaultDriverType= FIREFOX;
        selectDriver(0);
        WebDriver driver2 = addDefaultDriver();
        driver2.manage()
               .window()
               .setPosition(new Point(1026, 0));
        driver2.manage()
               .window()
               .setSize(new Dimension(1024, 768));
    }

    /**
     * Passes messages between 2 browser instances, in both directions, then disables push
     */
    @Test
    public void enabled() {
        // given
        int pauseTime = 2000;
        navigateTo("notifications/push");
        pause(pauseTime);
        selectDriver(1);
        pause(pauseTime);
        navigateTo("notifications/push");
        // when

        selectDriver(0);
        pause(pauseTime);
        pushView.groupBox()
                .sendKeys("a");
        pushView.messageBox()
                .sendKeys("a1");
        pushView.sendButton()
                .click();
        // then
        pause(pauseTime);
        assertThat(pushView.messageLog()
                           .getValue()).isEqualTo("a:a1\n");
        assertThat(messageBar.message()).isEqualTo("a:a1");
        selectDriver(1);
        pause(pauseTime);
        assertThat(pushView.messageLog()
                           .getValue()).isEqualTo("a:a1\n");


        // when
        pushView.groupBox()
                .sendKeys("b");
        pushView.messageBox()
                .sendKeys("b1");
        pushView.sendButton()
                .click();
        pause(pauseTime);
        // then
        assertThat(pushView.messageLog()
                           .getValue()).isEqualTo("b:b1\na:a1\n");

        selectDriver(0);
        assertThat(pushView.messageLog()
                           .getValue()).isEqualTo("b:b1\na:a1\n");
        assertThat(messageBar.message()).isEqualTo("b:b1");

    }

    @Test
    public void disabled() {
        // given
        navigateTo("notifications/push");
        selectDriver(1);
        navigateTo("notifications/push");
        selectDriver(0);
        pushView.checkbox()
                .click();
        pause(1000);
        // when

        pushView.groupBox()
                .sendKeys("x");
        pushView.messageBox()
                .sendKeys("x1");

        pushView.sendButton()
                .click();

        // then message not processed
        assertThat(messageBar.message()).isEqualTo("Message Bar");
    }


}
