package uk.q3c.krail.testapp.view

import com.google.inject.Inject
import com.google.inject.Provider
import uk.q3c.krail.core.navigate.sitemap.DirectSitemapModule
import uk.q3c.krail.core.navigate.sitemap.MasterSitemap
import uk.q3c.krail.core.navigate.sitemap.StandardPageKey
import uk.q3c.krail.core.shiro.PageAccessControl
import uk.q3c.krail.core.user.LoginView
import uk.q3c.krail.core.view.DefaultNavigationView
import uk.q3c.krail.core.view.LogoutView
import uk.q3c.krail.core.view.NavigationView
import uk.q3c.krail.core.view.PrivateHomeView
import uk.q3c.krail.core.view.PublicHomeView
import uk.q3c.krail.core.view.ViewModule
import uk.q3c.krail.core.view.component.PageNavigationPanel
import uk.q3c.krail.i18n.Translate
import uk.q3c.util.guice.SerializationSupport

/**
 * Created by David Sowerby on 27 Aug 2018
 */
class SimplePagesModule : DirectSitemapModule() {

    init {
        rootURI = "p"
    }
    // private MapBinder<String, StandardPageSitemapEntry> mapBinder;
    // private MapBinder<String, RedirectEntry> redirectBinder;

    /**
     * Override this method to define different [MasterSitemap] entries for Standard Pages. All of the views
     * specified
     * here are interfaces, so if you only want to change the View implementation you can change the binding in
     * [ViewModule]
     *
     *
     */
    override fun define() {
        addEntry("", StandardPageKey.Public_Home, PageAccessControl.PUBLIC, PublicHomeView::class.java)
        addEntry(uri = "login", labelKey = StandardPageKey.Log_In, pageAccessControl = PageAccessControl.PUBLIC, viewClass = LoginView::class.java, positionIndex = -1)
        addEntry(uri = "logout", labelKey = StandardPageKey.Log_Out, pageAccessControl = PageAccessControl.PUBLIC, viewClass = LogoutView::class.java, positionIndex = -1)
        addEntry("private", StandardPageKey.Private_Home, PageAccessControl.PERMISSION, PrivateHomeView::class.java)
    }

}

class TestAppPublicHomeView @Inject constructor(serialisationSupport: SerializationSupport, translate: Translate, navigationPanelProvider: Provider<PageNavigationPanel>) : PublicHomeView, NavigationView by DefaultNavigationView(serialisationSupport, translate, navigationPanelProvider)
class TestAppPrivateHomeView @Inject constructor(serialisationSupport: SerializationSupport, translate: Translate, navigationPanelProvider: Provider<PageNavigationPanel>) : PrivateHomeView, NavigationView by DefaultNavigationView(serialisationSupport, translate, navigationPanelProvider)
