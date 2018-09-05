package uk.q3c.krail.testapp.view

import com.google.inject.Inject
import com.vaadin.server.ErrorHandler
import com.vaadin.ui.themes.ValoTheme
import uk.q3c.krail.core.i18n.I18NProcessor
import uk.q3c.krail.core.navigate.Navigator
import uk.q3c.krail.core.option.VaadinOptionContext
import uk.q3c.krail.core.push.Broadcaster
import uk.q3c.krail.core.push.KrailPushConfiguration
import uk.q3c.krail.core.push.PushMessageRouter
import uk.q3c.krail.core.ui.ApplicationTitle
import uk.q3c.krail.core.ui.SimpleUI
import uk.q3c.krail.core.user.notify.VaadinNotification
import uk.q3c.krail.core.view.component.NavigationBar
import uk.q3c.krail.eventbus.MessageBus
import uk.q3c.krail.i18n.CurrentLocale
import uk.q3c.krail.i18n.Translate
import uk.q3c.krail.option.Option
import uk.q3c.util.guice.SerializationSupport

/**
 * Created by David Sowerby on 29 Aug 2018
 */
class TestAppSimpleUI @Inject constructor(navigator: Navigator,
                                          errorHandler: ErrorHandler,
                                          broadcaster: Broadcaster,
                                          pushMessageRouter: PushMessageRouter,
                                          applicationTitle: ApplicationTitle,
                                          translate: Translate,
                                          currentLocale: CurrentLocale,
                                          translator: I18NProcessor,
                                          serializationSupport: SerializationSupport,
                                          pushConfiguration: KrailPushConfiguration,
                                          navigationBar: NavigationBar,
                                          messageBus: MessageBus,
                                          val vaadinNotification: VaadinNotification,
                                          val option: Option) : SimpleUI(navigator, errorHandler, broadcaster, pushMessageRouter, applicationTitle, translate, currentLocale, translator, serializationSupport, pushConfiguration, navigationBar, messageBus), VaadinOptionContext {

    init {
        topBar.buttonStyle = "${ValoTheme.BUTTON_BORDERLESS_COLORED} ${ValoTheme.BUTTON_ICON_ONLY}"
        topBar.helpPath = "p/help"
    }

    override fun optionInstance(): Option {
        return option
    }
}