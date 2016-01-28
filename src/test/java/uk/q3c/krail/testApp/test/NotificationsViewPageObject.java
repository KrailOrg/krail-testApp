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

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CheckBoxElement;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import uk.q3c.krail.core.option.DefaultOptionPopup;
import uk.q3c.krail.testapp.view.NotificationsView;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.PageObject;

import java.util.Optional;

/**
 * Created by David Sowerby on 19/10/14.
 */
public class NotificationsViewPageObject extends PageObject {

    public NotificationsViewPageObject(KrailTestBenchTestCase parentCase) {
        super(parentCase);
    }


    public ButtonElement errorButton() {
        return element(ButtonElement.class, Optional.of("error"), NotificationsView.class, Button.class);
    }

    public ButtonElement warningButton() {
        return element(ButtonElement.class, Optional.of("warning"), NotificationsView.class, Button.class);
    }

    public ButtonElement informationButton() {
        return element(ButtonElement.class, Optional.of("information"), NotificationsView.class, Button.class);
    }

    public ButtonElement viewOptionsButton() {
        return element(ButtonElement.class, Optional.of("view-options"), NotificationsView.class, Button.class);
    }

    public ButtonElement uiOptionsButton() {
        return element(ButtonElement.class, Optional.of("ui-options"), NotificationsView.class, Button.class);
    }


    public ButtonElement systemLevelOptionButton() {
        return element(ButtonElement.class, Optional.of("system-level-option"), NotificationsView.class, Button.class);
    }

    public ButtonElement clearStoreButton() {
        return element(ButtonElement.class, Optional.of("clear-store"), NotificationsView.class, Button.class);
    }


    public CheckBoxElement optionsPopupInformationCheckbox() {
        return element(CheckBoxElement.class, Optional.of("Information"), DefaultOptionPopup.class, CheckBox.class);
    }

    public ButtonElement optionsPopupDefaultsButton() {
        return element(ButtonElement.class, Optional.of("Information"), DefaultOptionPopup.class, Button.class);
    }

    public CheckBoxElement uiOptionsPopupMessageBarCheckbox() {
        return element(CheckBoxElement.class, Optional.of("Message_bar_is_Visible"), DefaultOptionPopup.class, CheckBox.class);
    }
}
