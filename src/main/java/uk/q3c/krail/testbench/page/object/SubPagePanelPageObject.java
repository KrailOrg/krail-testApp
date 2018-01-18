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

package uk.q3c.krail.testbench.page.object;

import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.HorizontalLayoutElement;
import org.openqa.selenium.WebElement;
import uk.q3c.krail.core.vaadin.ID;
import uk.q3c.krail.core.view.component.DefaultSubPagePanel;
import uk.q3c.krail.core.view.component.NavigationButton;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by david on 04/10/14.
 */
public class SubPagePanelPageObject extends PageObject {

    public SubPagePanelPageObject(KrailTestBenchTestCase parentCase) {
        super(parentCase);
    }

    public ButtonElement button(int index) {
        return element(ButtonElement.class, Optional.of(index), DefaultSubPagePanel.class, NavigationButton.class);
    }

    public List<String> buttonLabels() {
        HorizontalLayoutElement panelElement = subPagePanel();
        parentCase.pause(500);  // give the panel time to build
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            try {
                String id = ID.getIdc(Optional.of(i), DefaultSubPagePanel.class, NavigationButton.class);
                WebElement element = panelElement.findElement(By.id(id));
                labels.add(element.getText());
            } catch (Exception e) {
                break;
            }
        }


        return labels;
    }

    public HorizontalLayoutElement subPagePanel() {
        return element(HorizontalLayoutElement.class, Optional.empty(), DefaultSubPagePanel.class);
    }
}
