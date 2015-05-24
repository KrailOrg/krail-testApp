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

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends KrailTestBenchTestCase {

    @Rule
    public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);
    private NavTreePageObject navTree = new NavTreePageObject(this);

    @Before
    public void setUp() throws Exception {
        appContext = "krail-testapp";
        startDriver();
    }

    @Test
    public void testLogin() {
        // given
        pause(1000);
        navTree.expand(0);
        // navTree().index(0).expand().get().click();
        String startFragment = "system-account";
        navigateTo(startFragment);

        pause(1000);

        // when
        // then initial state
        assertThat(loginStatus.loginButton()
                              .getText()).isEqualTo("log in");
        assertThat(loginStatus.username()).isEqualTo("Guest");

        // when LoginStatusPanel button clicked
        loginStatus.loginButton()
                   .click();
        // then
        verifyUrl("login");

        // when username and password entered
        loginForm.login("ds", "password");
        // then correct url and status panel updated
        verifyUrl(startFragment);
        assertThat(loginStatus.loginButton()
                              .getText()).isEqualTo("log out");
        assertThat(loginStatus.username()).isEqualTo("ds");

        // when
        navTree.select("Private/Finance/Accounts");

        //then making sure page is visible
        verifyUrl("private/finance/accounts");
    }



    /**
     * If previous tests have caused unsuccessful logins, or this test is run repeatedly, login will fail because of
     * excessive attempts instead of through invalid login. The only way to reset that through the UI at the moment is
     * to login successfully first (which resets the attempt count), logout, then use an invalid login.
     */
    @Test
    public void authenticationFailure() {

        // given
        login();
        //logout
        loginStatus.loginButton()
                   .click();
        // when
        loginStatus.loginButton()
                   .click();
        loginForm.login("ds", "rubbish");

        // // then
        verifyUrl("login"); // has not moved
        assertThat(navTree.currentSelection()).isEqualTo("Log In");
        //        pause(1000);
        //        WebElement label = label(Optional.of("status"), DefaultLoginView.class, Label.class);
        //        pause(1000);
        //        assertThat(label).isNotNull();
        //        String s = label.getText();

        assertThat(loginForm.message()).isEqualTo("That username or password was not recognised");
    }


    public void init(WebDriver driver, String baseUrl) {
        this.setDriver(driver);
        this.setBaseUrl(baseUrl);
    }

}