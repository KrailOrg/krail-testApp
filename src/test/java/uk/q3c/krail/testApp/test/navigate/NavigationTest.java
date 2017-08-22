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

package uk.q3c.krail.testApp.test.navigate;

import com.vaadin.testbench.By;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import org.junit.*;
import org.openqa.selenium.WebElement;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.BreadcrumbPageObject;
import uk.q3c.krail.testbench.page.object.NavMenuPageObject;
import uk.q3c.krail.testbench.page.object.NavTreePageObject;
import uk.q3c.krail.testbench.page.object.SubPagePanelPageObject;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("Duplicates")
public class NavigationTest extends KrailTestBenchTestCase {

    @Rule
    public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);
    private BreadcrumbPageObject breadcrumb = new BreadcrumbPageObject(this);
    private NavMenuPageObject menu = new NavMenuPageObject(this);
    private NavTreePageObject navTree = new NavTreePageObject(this);
    private SubPagePanelPageObject subPagePanel = new SubPagePanelPageObject(this);

    @Before
    public void setUp() throws Exception {
        appContext = "krail-testapp";
    }

    @Test
    public void straight_to_private() {
        //when
        navigateWithRedirectExpected("private/finance", "home");
        //then

        verifyUrl("home");
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("private/finance is not a valid page");
    }

    @Test
    public void navigateFromTree() throws InterruptedException {

        // given
        startDriver();
        // when

        // then
        verifyUrl("home");
        // // when
        navTree.select("System Account");
        // // then
        verifyUrl("system-account");
        assertThat(navTree.currentSelection()).isEqualTo("System Account");
        // // when
        navTree.select("Public Home");
        // // then
        verifyUrl("home");
        // // when
        navTree.select("Log In");
        // // then
        verifyUrl("login");


    }

    @Ignore("NavTree does not work as expected, see https://github.com/davidsowerby/krail-testApp/issues/21")
    @Test
    public void expandTreeEntry() throws Exception {

        // given
        startDriver();
        // when

        // then
        verifyUrl("home");
        // when
        navTree.select("System Account/Enable Account");
        // then
        verifyUrl("system-account/enable-account");
    }


    @Test
//    @Ignore("https://github.com/davidsowerby/krail-testApp/issues/11")
    public void navigateToInvalidPage() {
        // given
        startDriver();
        // when

        navigateTo("rubbish");

        // then
        pause(500);
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("rubbish is not a valid page");
        //        assertThat(notification().getText()).isEqualTo("Info: rubbish is not a valid page");
        assertThat(notification().getAttribute("class")).isEqualTo("v-Notification humanized v-Notification-humanized");

        // then
        verifyUrl("rubbish");
    }

    @Test
    public void redirectFromPrivate() {

        // given
        startDriver();
        login();
        // when
        navigateTo("widgetset");
        navigateTo("private");
        // then
        verifyUrl("private/home");
        assertThat(navTree.currentSelection()).isEqualTo("Private Home");

    }

    @Test
    public void annotatedSitemapPageIsPresent() {

        // given
        startDriver();
        login();
        //when
        navigateTo("private/finance/purchasing");
        //then
        verifyUrl("private/finance/purchasing");
    }

    @Test
    public void browserBackForward() {

        // given
        startDriver();
        // when
        navTree.select(5);
        // then
        verifyUrl("system-account");
        assertThat(navTree.currentSelection()).isEqualTo("System Account");

        // when
        navigateTo("notifications");
        // then
        verifyUrl("notifications");
        assertThat(navTree.currentSelection()).isEqualTo("Notifications");
        // when
        navigateTo("system-account/enable-account");
        // then
        verifyUrl("system-account/enable-account");
        assertThat(navTree.currentSelection()).isEqualTo("Enable Account");
        // when
        navigateBack();
        // then
        verifyUrl("notifications");
        assertThat(navTree.currentSelection()).isEqualTo("Notifications");
        // when
        navigateForward();
        verifyUrl("system-account/enable-account");
        assertThat(navTree.currentSelection()).isEqualTo("Enable Account");
    }

    /**
     * Originally this would have reported an unauthorised action. The introduction of the UserSitemap means that an
     * authorised page will apparently not exist, so an "invalid page" will be reported instead. In some ways that is
     * actually better as even the existence of the page is masked.
     */
    @Test
    public void navigateToUnauthorisedPage() {

        // given
        startDriver();
        // when

        navigateTo("private/home");

        // then
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("private/home is not a valid page");
        pause(500);

        closeNotification();

        verifyNotUrl("private/home"); // not a valid test, but maybe it should be
        navigateTo("system-account");
        // when
        login();

        // then
        verifyUrl("system-account");
        // when
        navigateTo("private/home");
        // then
        verifyUrl("private/home");

    }



    @Test
    public void breadcrumb_navigate() {

        // given
        // when
        navigateTo("system-account/reset-account");
        assertThat(breadcrumb.button(0)).isNotNull();
        breadcrumb.button(0)
                .click();
        // then
        verifyUrl("system-account");
    }

    @Test
    public void subPage_navigate() {

        // given
        // when
        navigateTo("system-account");
        //then
        assertThat(subPagePanel.buttonLabels()).containsExactly("Enable Account", "Refresh Account",
                "Request Account", "Reset Account", "Unlock Account");

        subPagePanel.button(0)
                .click();
        // then
        verifyUrl("system-account/enable-account");
        assertThat(subPagePanel.buttonLabels()).isEmpty();

    }

    @Test
    @Ignore("https://github.com/davidsowerby/krail-testApp/issues/15")
    public void menuNavigate() {

        // given
        startDriver();
        // when

        //        testBenchElement(driver.findElement(By.vaadin("testapp::PID_SDefaultUserNavigationMenu#item4")))
        // .click(43, 6);
        //        testBenchElement(driver.findElement(By.vaadin("testapp::Root/VOverlay[0]/VMenuBar[0]#item0")))
        // .click(44, 8);

        menu.menuBar()
                .clickItem("System Account");
        assertThat(isItemVisible("Notifications")).isTrue();
        menu.clickItem("System Account", "Enable Account");
        //then
        verifyUrl("system-account/enable-account");

    }

    private boolean isItemVisible(String item) {
        for (WebElement webElement : getItemCaptions()) {
            if (webElement.getText()
                    .equals(item)) {
                return true;
            }
        }
        return false;
    }

    private List<WebElement> getItemCaptions() {
        return findElements(By.className("v-menubar-menuitem-caption"));
    }


    @After
    public void tearDown2() throws Exception {
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }


}
