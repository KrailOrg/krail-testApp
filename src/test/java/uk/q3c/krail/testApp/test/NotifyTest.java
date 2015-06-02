/*
 * Copyright (C) 2013 David Sowerby
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package uk.q3c.krail.testApp.test;

import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.testbench.elements.WindowElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.MessageBarPageObject;

import static org.assertj.core.api.Assertions.assertThat;

public class NotifyTest extends KrailTestBenchTestCase {

    private final String testPage = "notifications";
    @Rule
    public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);
    private MessageBarPageObject messageBar = new MessageBarPageObject(this);
    private NotificationsViewPageObject notificationsView = new NotificationsViewPageObject(this);
    private OptionsPopupPageObject optionsPopup = new OptionsPopupPageObject(this);

    @Before
    public void setUp() throws Exception {
        appContext = "krail-testapp";
    }

    @After
    public void teardown() {
        navigateTo(testPage);
        notificationsView.clearStoreButton();
    }

    @Test
    public void defaultMessage() {

        // given
        navigateTo(testPage);
        // when

        // then
        assertThat(messageBar.message()).isEqualTo("Message Bar");
    }

    @Test
    public void notifyError() {

        // given
        navigateTo(testPage);

        // when
        notificationsView.errorButton()
                         .click();
        // then

        assertThat(messageBar.message()).isEqualTo("ERROR: You cannot use service Fake Service until it has been " +
                "started");
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("You cannot use service Fake Service until it has been started");
        //        assertThat(notification().getText()).isEqualTo("Error: You cannot use service Fake Service until it
        // has been " +
        //                "started - close with ESC-key");
        assertThat(notification().getAttribute("class")).isEqualTo("v-Notification error v-Notification-error");
        closeNotification();

    }

    @Test
    public void notifyWarning() {
        // given
        navigateTo(testPage);
        // when
        notificationsView.warningButton()
                         .click();
        // then
        assertThat(messageBar.message()).isEqualTo("Warning: You cannot use service Fake Service until it has been " +
                "" + "started");
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("You cannot use service Fake Service until it has been started");
        //        assertThat(notification().getText()).isEqualTo("Warning: You cannot use service Fake Service until it has been started");
        assertThat(notification().getAttribute("class")).isEqualTo("v-Notification warning v-Notification-warning");
        closeNotification();
    }

    @Test
    public void notifyInformation() {
        // given
        navigateTo(testPage);
        ensureInfoButtonVisible();

        // when
        notificationsView.informationButton()
                         .click();
        // then
        assertThat(messageBar.message()).isEqualTo("You cannot use service Fake Service until it has been started");
        assertThat(notification()).isNotNull();
        assertThat(notification().getText()).isEqualTo("You cannot use service Fake Service until it has been started");
        //        assertThat(notification().getText()).isEqualTo("Info: You cannot use service Fake Service until it has been started");
        assertThat(notification().getAttribute("class")).isEqualTo("v-Notification humanized v-Notification-humanized");
        closeNotification();
    }

    private void ensureInfoButtonVisible() {
        notificationsView.clearStoreButton()
                         .click();
        while (!infoButtonVisible()) {
            notificationsView.optionsButton()
                             .click();
            final CheckBoxElement checkBoxElement = notificationsView.optionsPopupInformationCheckbox();
            checkBoxElement.click();
            checkBoxElement.sendKeys(" ");
            optionsPopup.closeWindow();
        }
    }

    private boolean infoButtonVisible() {
        try {
            notificationsView.informationButton();
            return true;
        } catch (NoSuchElementException nse) {
            return false;
        }
    }

    @Test
    public void optionsPopup() {
        //given
        navigateTo(testPage);
        login();

        //when
        notificationsView.optionsButton()
                         .click();

        //then make sure popup is there
        final WindowElement windowElement = optionsPopup.window();
        assertThat(notificationsView.optionsPopupInformationCheckbox()
                                    .isDisplayed()).isTrue();
        assertThat(windowElement.getCaption()).isEqualTo("Notification Options");

        //when the state is reversed (successive tests may leave previous value in Option store)
        boolean infoButtonIsVisible = infoButtonVisible();
        final CheckBoxElement checkBoxElement = notificationsView.optionsPopupInformationCheckbox();
        checkBoxElement.click();
        checkBoxElement.sendKeys(" ");
        assertThat(infoButtonVisible()).isNotEqualTo(infoButtonIsVisible);
        optionsPopup.closeWindow();

        //given
        ensureInfoButtonVisible();


        //when
        notificationsView.optionsButton()
                         .click();
        ButtonElement defaultsButton = notificationsView.optionsPopupDefaultsButton();
        defaultsButton.click();
        assertThat(infoButtonIsVisible);
        assertThat(notificationsView.optionsPopupInformationCheckbox()
                                    .getValue()).isEqualTo("checked");

        optionsPopup.closeWindow();
    }

    @Test
    public void systemLevelOption() {
        //given
        navigateTo(testPage);
        notificationsView.clearStoreButton()
                         .click(); // previous tests debris
        login();

        //when user makes info button visible
        notificationsView.optionsButton()
                         .click();
        clickInfoButtonCheckBox();
        //then check user permissions allowed change
        assertThat(infoButtonVisible()).isFalse();
        //when
        notificationsView.optionsPopupDefaultsButton()
                         .click();
        //then default is true (no system option has been set)
        assertThat(infoButtonVisible()).isTrue();
        optionsPopup.closeWindow();

        //when

        loginStatus.loginButton()
                   .click();//logout
        loginForm.setCredentials("admin", "password");
        login();

        //set the system option to false
        navigateTo(testPage);
        notificationsView.systemLevelOptionButton()
                         .click();
        loginStatus.loginButton()
                   .click();//logout

        loginForm.setCredentials("ds", "password");
        login();
        navigateTo(testPage);
        notificationsView.optionsButton()
                         .click();
        notificationsView.optionsPopupDefaultsButton()
                         .click();

        //then
        pause(500);
        //then default is false (system option has been set false)
        assertThat(infoButtonVisible()).isFalse();
        optionsPopup.closeWindow();

        //when user tries to set system option

    }

    private void clickInfoButtonCheckBox() {
        final CheckBoxElement checkBoxElement = notificationsView.optionsPopupInformationCheckbox();
        checkBoxElement.click();
        checkBoxElement.sendKeys(" ");
    }
}
