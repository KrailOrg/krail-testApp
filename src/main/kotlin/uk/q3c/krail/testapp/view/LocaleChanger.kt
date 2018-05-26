package uk.q3c.krail.testapp.view

import com.google.inject.Inject
import com.vaadin.ui.Button
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import uk.q3c.krail.core.view.ViewBase
import uk.q3c.krail.core.view.component.ViewChangeBusMessage
import uk.q3c.krail.i18n.Translate
import uk.q3c.krail.testapp.TestAppUI
import uk.q3c.krail.testapp.i18n.Caption
import uk.q3c.krail.testapp.i18n.DescriptionKey
import uk.q3c.krail.testapp.i18n.LabelKey
import uk.q3c.util.guice.SerializationSupport
import java.util.*

/**
 * Created by David Sowerby on 11 Feb 2018
 */
class LocaleChanger @Inject constructor(translate: Translate, serialisationSupport: SerializationSupport) : ViewBase(translate, serialisationSupport) {

    @Caption(caption = LabelKey.changeToUKEnglish, description = DescriptionKey.changeToUKEnglish)
    lateinit var changeToUK: Button
    @Caption(caption = LabelKey.changeToGerman, description = DescriptionKey.changeToGerman)
    lateinit var changeToGerman: Button
    lateinit var currentLocale: Label

    var layout = FormLayout()

    override fun doBuild(busMessage: ViewChangeBusMessage?) {
        layout = FormLayout()
        rootComponent = layout
        changeToUK = Button()
        changeToGerman = Button()
        currentLocale = Label()

        changeToUK.addClickListener({
            val ui: TestAppUI = UI.getCurrent() as TestAppUI
            ui.localeCombo.setSelectedItem(Locale.UK)
        })

        changeToGerman.addClickListener({
            val ui: TestAppUI = UI.getCurrent() as TestAppUI
            ui.localeCombo.setSelectedItem(Locale.GERMANY)
        })

        val ui: TestAppUI = UI.getCurrent() as TestAppUI
        ui.localeCombo.addValueChangeListener({
            currentLocale.value = ui.localeCombo.value.toLanguageTag()
        })

        layout.addComponents(currentLocale, changeToUK, changeToGerman)
    }


}

