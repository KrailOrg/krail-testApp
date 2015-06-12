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

package uk.q3c.krail.testApp.test;

import com.vaadin.testbench.ScreenshotOnFailureRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.NavTreePageObject;

public class Login_Navigation_Rule extends KrailTestBenchTestCase {

    @Rule
    public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);
    private NavTreePageObject navTree = new NavTreePageObject(this);

    @Before
    public void setUp() throws Exception {
        appContext = "krail-testapp";
        startDriver();
    }

    @Test
    public void straight_to_login() {
        //given
        navigateTo("login");
        //when
        login();
        //then
        verifyUrl("private/home");
    }

    @Test
    public void public_page_then_login() {
        //given
        navigateTo("system-account/refresh-account");
        //when
        login();
        //then
        verifyUrl("system-account/refresh-account");
    }

    @Test
    public void login_out_in() {
        //given
        navigateTo("system-account/refresh-account");
        login();
        loginStatus.loginButton()
                   .click(); // logout
        //when
        login();
        //then

        verifyUrl("private/home");
    }

    @Test
    public void straight_to_logout_then_login() {
        //given
        navigateTo("logout");
        //when
        login();
        //then
        verifyUrl("private/home");
    }


    public void init(WebDriver driver, String baseUrl) {
        this.setDriver(driver);
        this.setBaseUrl(baseUrl);
    }

}