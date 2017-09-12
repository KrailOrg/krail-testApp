/*
 *
 *  * Copyright (c) 2016. David Sowerby
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations under the License.
 *
 */
package uk.q3c.krail.testApp.mulituser;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import uk.q3c.krail.testApp.push.PushViewPageObject;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.MessageBarPageObject;

public class MultiUserNavigation extends KrailTestBenchTestCase {

    private MessageBarPageObject messageBar = new MessageBarPageObject(this);
    private PushViewPageObject pushView = new PushViewPageObject(this);

    @Before
    public void setup() {
        appContext = "krail-testapp";
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
    public void navigate() {
        //given both at home
        selectDriver(0);
        navigateTo("home");
        selectDriver(1);
        navigateTo("home");

        // when user 0 logs in
        selectDriver(0);
        login();
        // then user 1 still at home
        selectDriver(1);
        verifyUrl("home");

        //when user 0 navigates
        selectDriver(0);
        navigateTo("private/sysadmin");

        //then user1 unmoved
        selectDriver(1);
        verifyUrl("home");

    }


}
