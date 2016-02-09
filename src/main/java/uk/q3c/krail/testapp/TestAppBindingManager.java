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

package uk.q3c.krail.testapp;

import com.google.inject.Module;
import uk.q3c.krail.core.guice.DefaultBindingManager;
import uk.q3c.krail.core.i18n.I18NModule;
import uk.q3c.krail.core.navigate.sitemap.SystemAccountManagementPages;
import uk.q3c.krail.core.option.OptionModule;
import uk.q3c.krail.core.shiro.DefaultShiroModule;
import uk.q3c.krail.core.shiro.aop.KrailShiroAopModule;
import uk.q3c.krail.core.sysadmin.SystemAdminPages;
import uk.q3c.krail.core.ui.DefaultUIModule;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.krail.testapp.persist.Jpa1;
import uk.q3c.krail.testapp.persist.TestAppJpaModule;
import uk.q3c.krail.testapp.uac.TestAppRealm;
import uk.q3c.krail.testapp.view.AnnotationPagesModule;
import uk.q3c.krail.testapp.view.FinancePages;
import uk.q3c.krail.testapp.view.TestAppPages;
import uk.q3c.krail.testapp.view.TestAppViewModule;

import java.util.List;
import java.util.Locale;

//@WebListener
public class TestAppBindingManager extends DefaultBindingManager {

    @Override
    protected void addAppModules(List<Module> modules) {
        modules.add(new TestAppJpaModule());
    }

    @Override
    protected Module viewModule() {
        return new TestAppViewModule();
    }

    @Override
    protected void addSitemapModules(List<Module> modules) {
        super.addSitemapModules(modules);
        modules.add(new SystemAccountManagementPages());
        modules.add(new TestAppPages());
        modules.add(new FinancePages());
        modules.add(new AnnotationPagesModule());
        modules.add(new SystemAdminPages().rootURI("private/sysadmin"));
    }

    @Override
    protected Module servletModule() {
        return new TestAppServletModule();
    }

    @Override
    protected Module i18NModule() {
        return new I18NModule().supportedLocales(Locale.UK, Locale.ITALY, Locale.GERMANY);
    }

    @Override
    protected Module uiModule() {
        return new DefaultUIModule().uiClass(TestAppUI.class)
                                    .applicationTitleKey(LabelKey.Krail_Test);
    }

    @Override
    protected Module shiroAopModule() {
        return new KrailShiroAopModule().selectAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Module shiroModule() {
        return new DefaultShiroModule().enableCache()
                                       .addRealm(TestAppRealm.class);
    }

    /**
     * Override this if you have provided your own {@link OptionModule}.
     *
     * @return module instance
     */
    @Override
    protected Module optionModule() {
        return new OptionModule().activeSource(Jpa1.class);
    }
}
