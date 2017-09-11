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

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextAreaElement;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import uk.q3c.krail.testApp.push.Push_Functional;
import uk.q3c.krail.testapp.view.PayrollView;
import uk.q3c.krail.testapp.view.PushView;
import uk.q3c.krail.testbench.KrailTestBenchTestCase;
import uk.q3c.krail.testbench.page.object.PageObject;

import java.util.Optional;

/**
 * PageObject representing {@link PushView} used to assist the {@link Push_Functional} test
 * <p>
 * Created by David Sowerby on 18/10/14.
 */
public class PayrollViewPageObject extends PageObject {

    public PayrollViewPageObject(KrailTestBenchTestCase parentCase) {
        super(parentCase);
    }

    public ButtonElement clearDatabaseButton() {
        return element(ButtonElement.class, Optional.of("clear-database"), PayrollView.class, Button.class);
    }

    public ButtonElement adminButton() {
        return element(ButtonElement.class, Optional.of("admin"), PayrollView.class, Button.class);
    }

    public ButtonElement setValue1Button() {
        return element(ButtonElement.class, Optional.of("setValue1Button"), PayrollView.class, Button.class);
    }

    public ButtonElement setValue2Button() {
        return element(ButtonElement.class, Optional.of("setValue2Button"), PayrollView.class, Button.class);
    }

    public ButtonElement refreshButton() {
        return element(ButtonElement.class, Optional.of("refresh"), PayrollView.class, Button.class);
    }

    public ButtonElement clearCacheButton() {
        return element(ButtonElement.class, Optional.of("cache"), PayrollView.class, Button.class);
    }

    public TextAreaElement text() {
        return element(TextAreaElement.class, Optional.of("text area"), PayrollView.class, TextArea.class);
    }


}
