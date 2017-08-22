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

package uk.q3c.krail.testapp.view;

import com.google.inject.Inject;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.onami.persist.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.q3c.krail.core.option.VaadinOptionContext;
import uk.q3c.krail.core.vaadin.ID;
import uk.q3c.krail.core.view.Grid3x3ViewBase;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.option.Option;
import uk.q3c.krail.option.OptionKey;
import uk.q3c.krail.option.UserHierarchy;
import uk.q3c.krail.option.UserHierarchyDefault;
import uk.q3c.krail.option.persist.OptionCache;
import uk.q3c.krail.testapp.i18n.Caption;
import uk.q3c.krail.testapp.i18n.LabelKey;

import java.util.Optional;

/**
 * Created by David Sowerby on 22/05/15.
 */
@SuppressFBWarnings({"LSC_LITERAL_STRING_COMPARISON", "LSC_LITERAL_STRING_COMPARISON", "LSC_LITERAL_STRING_COMPARISON", "LSC_LITERAL_STRING_COMPARISON",
        "LSC_LITERAL_STRING_COMPARISON"})
public class PayrollView extends Grid3x3ViewBase implements VaadinOptionContext {
    private static Logger log = LoggerFactory.getLogger(PayrollView.class);
    public static final OptionKey<Integer> payrollOption = new OptionKey<>(5, PayrollView.class, LabelKey.Payroll);
    private Option option;
    private UserHierarchy userHierarchy;
    private OptionCache optionCache;
    @Caption(caption = LabelKey.Set_System_Level, description = LabelKey.Set_System_Level)
    private Button adminButton;
    private Button setValue1Button;
    private Button setValue2Button;
    private TextArea textArea;
    private Button refreshButton;
    private Button clearCacheButton;

    @Inject
    public PayrollView(Option option, @UserHierarchyDefault UserHierarchy userHierarchy, OptionCache optionCache, Translate translate) {
        super(translate);
        this.option = option;
        this.userHierarchy = userHierarchy;
        log.debug("UserHierarchy is instance of {}", userHierarchy.getClass().getName());
        this.optionCache = optionCache;
        nameKey = LabelKey.Payroll;
    }



    @Override
    public Option optionInstance() {
        return option;
    }

    @Override
    public void optionValueChanged(Property.ValueChangeEvent event) {
        updateText();
    }

    @Override
    protected void doBuild(ViewChangeBusMessage busMessage) {
        super.doBuild(busMessage);
        adminButton = new Button();
        adminButton.addClickListener(click -> setSystemLevel());
        setTopLeft(adminButton);

        setValue1Button = new Button("Set value 433");
        setValue1Button.addClickListener(click -> setUserValue(433));
        setTopCentre(setValue1Button);

        setValue2Button = new Button("Set value 22");
        setValue2Button.addClickListener(click -> setUserValue(22));
        setTopRight(setValue2Button);

        refreshButton = new Button("refresh");
        refreshButton.addClickListener(click -> updateText());
        setBottomRight(refreshButton);

        clearCacheButton = new Button("clear cache");
        clearCacheButton.addClickListener(click -> optionCache.clear());
        setBottomCentre(clearCacheButton);


        textArea = new TextArea();
        textArea.setSizeFull();
        setMiddleCentre(textArea);
    }

    @Transactional
    protected void setSystemLevel() {
        option.set(payrollOption, 1, 999);
    }

    @Transactional
    protected void setUserValue(int value) {
        option.set(payrollOption, value);
    }


    private void updateText() {
        StringBuilder buf = new StringBuilder();
        buf.append(userHierarchy.highestRankName());
        buf.append(" = ");
        buf.append(option.get(payrollOption));
        buf.append('\n');
        buf.append(userHierarchy.lowestRankName());
        buf.append(" = ");
        buf.append(option.getSpecificRanked(1, payrollOption));
        textArea.setValue(buf.toString());
    }

    @Override
    protected void setIds() {
        super.setIds();
        adminButton.setId(ID.getId(Optional.of("admin"), this, adminButton));
        setValue1Button.setId(ID.getId(Optional.of("setValue1Button"), this, setValue1Button));
        setValue2Button.setId(ID.getId(Optional.of("setValue2Button"), this, setValue2Button));
        refreshButton.setId(ID.getId(Optional.of("refresh"), this, refreshButton));
        clearCacheButton.setId(ID.getId(Optional.of("cache"), this, clearCacheButton));
        textArea.setId(ID.getId(Optional.of("text area"), this, textArea));
    }
}
