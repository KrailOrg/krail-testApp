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
import org.junit.*;
import org.openqa.selenium.NoSuchElementException;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.MessageBarPageObject;

import static org.assertj.core.api.Assertions.*;

public class OptionPopupTest extends KrailTestBenchTestCase {

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
    public void optionsPopup() {
        //given
        navigateTo(testPage);
        login();

        //when
        notificationsView.viewOptionsButton()
                         .click();

        //then make sure popup is there
        final WindowElement windowElement = optionsPopup.windowForViewOptions();
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
        notificationsView.viewOptionsButton()
                         .click();
        ButtonElement defaultsButton = notificationsView.optionsPopupDefaultsButton();
        defaultsButton.click();
        assertThat(infoButtonIsVisible);
        assertThat(notificationsView.optionsPopupInformationCheckbox()
                                    .getValue()).isEqualTo("checked");

        optionsPopup.closeWindow();
    }

    private boolean infoButtonVisible() {
        try {
            notificationsView.informationButton();
            return true;
        } catch (NoSuchElementException nse) {
            return false;
        }
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

    @Test
    public void systemLevelOption() {
        //given
        navigateTo(testPage);
        notificationsView.clearStoreButton()
                         .click(); // previous tests debris
        login();

        //when user makes info button visible
        notificationsView.viewOptionsButton()
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
        notificationsView.viewOptionsButton()
                         .click();
        notificationsView.optionsPopupDefaultsButton()
                         .click();

        //then
        //then default is false (system option has been set false)
        assertThat(infoButtonVisible()).isFalse();
        optionsPopup.closeWindow();

    }

    private void clickInfoButtonCheckBox() {
        final CheckBoxElement checkBoxElement = notificationsView.optionsPopupInformationCheckbox();
        checkBoxElement.click();
        checkBoxElement.sendKeys(" ");
    }

    @Ignore("Clicking UI button works manually but not in this test, see https://github.com/davidsowerby/krail-testApp/issues/22")
    @Test
    public void openUIOptionsWithViewOptionsOpen() {
        //given
        navigateTo(testPage);
        ensureInfoButtonVisible();
        notificationsView.clearStoreButton()
                .click(); // previous tests debris
        login();
        notificationsView.viewOptionsButton()
                .click();  // open view options

        //when
        notificationsView.uiOptionsButton()
                .click(); // open ui options

        //then ui options displayed
        //then make sure popup is there
        final WindowElement windowElement = optionsPopup.windowForUIOptions();
        assertThat(notificationsView.uiOptionsPopupMessageBarCheckbox()
                .isDisplayed()).isTrue();
        assertThat(windowElement.getCaption()).isEqualTo("Application Options");

    }
}
