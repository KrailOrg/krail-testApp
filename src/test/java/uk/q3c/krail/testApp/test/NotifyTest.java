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
import com.vaadin.testbench.elements.CheckBoxElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.MessageBarPageObject;

import static org.assertj.core.api.Assertions.*;

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
//        closeNotification();
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
//        closeNotification();
    }

    private void ensureInfoButtonVisible() {
        notificationsView.clearStoreButton()
                         .click();
        while (!infoButtonVisible()) {
            notificationsView.viewOptionsButton()
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


    private void clickInfoButtonCheckBox() {
        final CheckBoxElement checkBoxElement = notificationsView.optionsPopupInformationCheckbox();
        checkBoxElement.click();
        checkBoxElement.sendKeys(" ");
    }
}
