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

package uk.q3c.krail.testApp.test.persist;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import uk.q3c.krail.testapp.view.JpaView;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.PageObject;

import java.util.Optional;

/**
 * Created by David Sowerby on 02/01/15.
 */
public class JpaViewPageObject extends PageObject {

    public JpaViewPageObject(KrailTestBenchTestCase parentCase) {
        super(parentCase);
    }

    public ButtonElement saveButton(int index) {
        return element(ButtonElement.class, Optional.of(index), JpaView.class, Button.class);
    }

    public TableElement table(int index) {
        return element(TableElement.class, Optional.of(index), JpaView.class, Table.class);
    }

    public LabelElement label(int index) {
        return element(LabelElement.class, Optional.of(index), JpaView.class, Label.class);
    }

}
