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

import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.WindowElement;
import com.vaadin.ui.Window;
import uk.q3c.krail.core.user.opt.DefaultOptionPopup;
import uk.q3c.krail.testapp.TestAppUI;
import uk.q3c.krail.testapp.view.NotificationsView;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.PageObject;

import java.util.Optional;

/**
 * The popup components are created from the OptionContext, so elements within the opopup must be part of the context's PageObject
 * <p>
 * Created by David Sowerby on 02/06/15.
 */
public class OptionsPopupPageObject extends PageObject {
    /**
     * {@inheritDoc}
     */
    public OptionsPopupPageObject(KrailTestBenchTestCase parentCase) {
        super(parentCase);
    }

    public void closeWindow() {
        windowForViewOptions().findElement(By.className("v-window-closebox"))
                .click();
    }

    public WindowElement windowForViewOptions() {
        return element(WindowElement.class, Optional.empty(), NotificationsView.class, DefaultOptionPopup.class, Window.class);
    }

    public WindowElement windowForUIOptions() {
        return element(WindowElement.class, Optional.empty(), TestAppUI.class, DefaultOptionPopup.class, Window.class);
    }


}
