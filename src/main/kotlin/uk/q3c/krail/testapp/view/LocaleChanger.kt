package uk.q3c.krail.testapp.view

import com.google.inject.Inject
import com.vaadin.ui.Button
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Label
import net.engio.mbassy.listener.Handler
import net.engio.mbassy.listener.Listener
import uk.q3c.krail.core.eventbus.SessionBus
import uk.q3c.krail.core.view.ViewBase
import uk.q3c.krail.core.view.component.ViewChangeBusMessage
import uk.q3c.krail.eventbus.SubscribeTo
import uk.q3c.krail.i18n.CurrentLocale
import uk.q3c.krail.i18n.LocaleChangeBusMessage
import uk.q3c.krail.i18n.Translate
import uk.q3c.krail.testapp.i18n.Caption
import uk.q3c.krail.testapp.i18n.DescriptionKey
import uk.q3c.krail.testapp.i18n.LabelKey
import uk.q3c.util.guice.SerializationSupport
import java.util.*

/**
 * Created by David Sowerby on 11 Feb 2018
 */
@Listener
@SubscribeTo(SessionBus::class)
class LocaleChanger @Inject constructor(val locale: CurrentLocale, translate: Translate, serialisationSupport: SerializationSupport) : ViewBase(translate, serialisationSupport) {

    @Caption(caption = LabelKey.changeToUKEnglish, description = DescriptionKey.changeToUKEnglish)
    lateinit var changeToUK: Button
    @Caption(caption = LabelKey.changeToGerman, description = DescriptionKey.changeToGerman)
    lateinit var changeToGerman: Button
    lateinit var currentLocale: Label

    var layout = FormLayout()

    override fun doBuild(busMessage: ViewChangeBusMessage) {
        layout = FormLayout()
        rootComponent = layout
        changeToUK = Button()
        changeToGerman = Button()
        currentLocale = Label()

        changeToUK.addClickListener {
            locale.locale = Locale.UK
        }

        changeToGerman.addClickListener {
            locale.locale = Locale.GERMANY
        }

        layout.addComponents(currentLocale, changeToUK, changeToGerman)
    }

    @Handler
    fun localeChanged(msg: LocaleChangeBusMessage) {
        currentLocale.value = msg.newLocale.toLanguageTag()

    }


}

