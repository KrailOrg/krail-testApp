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


import com.vaadin.testbench.TestBench;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.LoginFormPageObject;

import static org.assertj.core.api.Assertions.*;

public class MultiUserOptions extends KrailTestBenchTestCase {

    final String page = "private/finance/payroll";
    protected LoginFormPageObject loginForm = new LoginFormPageObject(this);
    private PayrollViewPageObject payrollView = new PayrollViewPageObject(this);

    @Before
    public void setup() {
        appContext = "krail-testapp";
        selectDriver(0);
        WebDriver driver2 = TestBench.createDriver(new FirefoxDriver());
        driver2.manage()
               .window()
               .setPosition(new Point(1026, 0));
        driver2.manage()
               .window()
               .setSize(new Dimension(1024, 768));
        addDriver(driver2);
        WebDriver driver3 = TestBench.createDriver(new FirefoxDriver());
        driver3.manage()
               .window()
               .setPosition(new Point(536, 400));
        driver3.manage()
               .window()
               .setSize(new Dimension(1024, 768));
        addDriver(driver3);
    }


    /**
     * Passes messages between 2 browser instances, in both directions, then disables push
     */
    @Test
    public void navigate() {
        //given all logged in and on right page
        loginUser(0, "ds", "password");
        loginUser(1, "eq", "eq");
        loginUser(2, "admin", "password");
        navigateToPage();
//        when
        String result0 = refresh(0);
        String result1 = refresh(1);
        String result2 = refresh(2);

//        then
        assertThat(result0).isEqualTo("ds = 5\nsystem = 5\n"); // why is this 'me'??
        assertThat(result1).isEqualTo("eq = 5\nsystem = 5\n");
        assertThat(result2).isEqualTo("admin = 5\nsystem = 5\n");

        //when
        selectDriver(0);
        payrollView.setValue1Button()
                   .click();
        payrollView.refreshButton()
                   .click();
        assertThat(payrollView.text()
                              .getValue()).isEqualTo("ds = 433\nsystem = 5");
        selectDriver(1);
        payrollView.refreshButton()
                   .click();
        assertThat(payrollView.text()
                              .getValue()).isEqualTo("eq = 5\nsystem = 5");
        selectDriver(2);
        payrollView.refreshButton()
                   .click();
        assertThat(payrollView.text()
                              .getValue()).isEqualTo("admin = 5\nsystem = 5");

//        when
        payrollView.adminButton()
                   .click();
        payrollView.refreshButton()
                   .click();
        assertThat(payrollView.text()
                              .getValue()).isEqualTo("admin = 999\nsystem = 999");
        selectDriver(1);
        payrollView.refreshButton()
                   .click();
        assertThat(payrollView.text()
                              .getValue()).isEqualTo("eq = 5\nsystem = 5"); // original value is still cached
        loginStatus.clickButton(); //logout
        pause(100);
        loginStatus.clickButton(); //login, cache should be cleared
        loginForm.login("eq", "eq");
        navigateTo(page);
        payrollView.refreshButton()
                   .click();
        assertThat(payrollView.text()
                              .getValue()).isEqualTo("eq = 999\nsystem = 999"); //user level value had not been changed, so highest is system

        selectDriver(0);
        payrollView.refreshButton()
                   .click();
        assertThat(payrollView.text()
                              .getValue()).isEqualTo("ds = 433\nsystem = 5");// this user has changed their setting, but old system value still in cache
        payrollView.clearCacheButton()
                   .click();//clear the cache
        payrollView.refreshButton()
                   .click();
        assertThat(payrollView.text()
                              .getValue()).isEqualTo("ds = 433\nsystem = 999");

    }

    private String refresh(int driver) {
        selectDriver(driver);
        payrollView.refreshButton()
                   .click();
        return payrollView.text()
                          .getValue();
    }

    private void navigateToPage() {

        selectDriver(0);
        navigateTo(page);
        selectDriver(1);
        navigateTo(page);
        selectDriver(2);
        navigateTo(page);
    }

    private void loginUser(int driver, String username, String password) {
        selectDriver(driver);
        navigateTo("home");
        loginStatus.loginButton()
                   .click();
        waitForUrl("login");
        loginForm.login(username, password);
    }


}
