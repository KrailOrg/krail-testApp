package uk.q3c.krail.testapp.view;

import com.google.inject.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.q3c.krail.app.TestAppStartupModule;
import uk.q3c.krail.core.guice.CoreBindingsCollator;
import uk.q3c.krail.core.i18n.KrailI18NModule;
import uk.q3c.krail.core.navigate.sitemap.SystemAccountManagementPages;
import uk.q3c.krail.core.shiro.DefaultShiroModule;
import uk.q3c.krail.core.shiro.aop.KrailShiroAopModule;
import uk.q3c.krail.core.sysadmin.SystemAdminPages;
import uk.q3c.krail.core.ui.DefaultUIModule;
import uk.q3c.krail.testapp.TestAppServletModule;
import uk.q3c.krail.testapp.TestAppUI;
import uk.q3c.krail.testapp.i18n.LabelKey;
import uk.q3c.krail.testapp.uac.TestAppRealm;

import java.util.List;
import java.util.Locale;

/**
 * Created by David Sowerby on 27 Feb 2018
 */
public class TestAppBindingsCollator extends CoreBindingsCollator {
    private static Logger log = LoggerFactory.getLogger(TestAppBindingsCollator.class);

    public TestAppBindingsCollator() {
    }

    public TestAppBindingsCollator(Module... modules) {
        super(modules);
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
    protected Module viewModule() {
        return new TestAppViewModule();
    }

    @Override
    protected Module servletModule() {
        return new TestAppServletModule();
    }

    @Override
    protected Module i18NModule() {
        log.debug("Binding VaadinI18NModule");
        return new KrailI18NModule().supportedLocales(Locale.UK, Locale.ITALY, Locale.GERMANY);
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

    @Override
    protected Module startupModule() {
        return new TestAppStartupModule();
    }
}
