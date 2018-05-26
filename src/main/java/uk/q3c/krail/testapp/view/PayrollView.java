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
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.q3c.krail.core.option.VaadinOptionContext;
import uk.q3c.krail.core.view.Grid3x3ViewBase;
import uk.q3c.krail.core.view.component.ViewChangeBusMessage;
import uk.q3c.krail.eventbus.GlobalMessageBus;
import uk.q3c.krail.eventbus.SubscribeTo;
import uk.q3c.krail.i18n.Translate;
import uk.q3c.krail.option.Option;
import uk.q3c.krail.option.OptionChangeMessage;
import uk.q3c.krail.option.OptionKey;
import uk.q3c.krail.option.UserHierarchy;
import uk.q3c.krail.option.UserHierarchyDefault;
import uk.q3c.krail.option.persist.OptionCache;
import uk.q3c.krail.option.persist.OptionDao;
import uk.q3c.krail.testapp.i18n.Caption;
import uk.q3c.krail.testapp.i18n.DescriptionKey;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.util.guice.SerializationSupport;

/**
 * Created by David Sowerby on 22/05/15.
 */
@SuppressFBWarnings({"LSC_LITERAL_STRING_COMPARISON", "LSC_LITERAL_STRING_COMPARISON", "LSC_LITERAL_STRING_COMPARISON", "LSC_LITERAL_STRING_COMPARISON",
        "LSC_LITERAL_STRING_COMPARISON"})
@Listener
@SubscribeTo(GlobalMessageBus.class)
public class PayrollView extends Grid3x3ViewBase implements VaadinOptionContext {
    public static final OptionKey<Integer> payrollOption = new OptionKey<>(5, PayrollView.class, LabelKey.Payroll);
    private static Logger log = LoggerFactory.getLogger(PayrollView.class);
    private final OptionDao optionDao;
    private Option option;
    private UserHierarchy userHierarchy;
    private final transient OptionCache optionCache;
    @Caption(caption = LabelKey.Set_System_Level, description = DescriptionKey.Set_System_Level)
    private Button adminButton;
    private Button setValue1Button;
    private Button setValue2Button;
    private TextArea textArea;
    private Button refreshButton;
    private Button clearCacheButton;
    private Button clearOptionDatabaseButton;

    @Inject
    public PayrollView(Option option, SerializationSupport serializationSupport, @UserHierarchyDefault UserHierarchy userHierarchy, OptionCache optionCache, Translate translate, OptionDao optionDao) {
        super(translate, serializationSupport);
        this.option = option;
        this.userHierarchy = userHierarchy;
        this.optionDao = optionDao;
        log.debug("UserHierarchy is instance of {}", userHierarchy.getClass().getName());
        this.optionCache = optionCache;
        nameKey = LabelKey.Payroll;
    }


    @Override
    public Option optionInstance() {
        return option;
    }

    @Handler
    public void optionValueChanged(OptionChangeMessage<?> msg) {
        if (msg.getOptionKey().getContext().equals(this.getClass())) {
            updateText();
        }
    }

    @Override
    protected void doBuild(ViewChangeBusMessage busMessage) {
        super.doBuild(busMessage);
        adminButton = new Button();
        adminButton.addClickListener(click -> setSystemLevel());
        setTopLeft(adminButton);

        setValue1Button = new Button("Set user value 433");
        setValue1Button.addClickListener(click -> setUserValue(433));
        setTopCentre(setValue1Button);

        setValue2Button = new Button("Set user value 22");
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

        clearOptionDatabaseButton = new Button("clear database");
        clearOptionDatabaseButton.addClickListener(click -> optionDao.clear());
        setMiddleRight(clearOptionDatabaseButton);
    }

    protected void setSystemLevel() {
        option.set(payrollOption, 1, 999);
    }

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


}
