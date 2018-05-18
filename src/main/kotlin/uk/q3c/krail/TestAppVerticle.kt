package uk.q3c.krail

import com.github.mcollovati.vertx.vaadin.VaadinVerticleConfiguration
import com.vaadin.annotations.VaadinServletConfiguration
import uk.q3c.krail.core.env.KrailVerticle
import uk.q3c.krail.testapp.ui.PointlessUI

/**
 * Created by David Sowerby on 18 Apr 2018
 */
@VaadinVerticleConfiguration(mountPoint = "/krail-testapp", serviceName = "TestAppVerticle.Service")
@VaadinServletConfiguration(ui = PointlessUI::class, productionMode = false, widgetset = "uk.q3c.krail.testapp.widgetset.testAppWidgetset")
class TestAppVerticle : KrailVerticle()