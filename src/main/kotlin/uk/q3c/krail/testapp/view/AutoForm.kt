package uk.q3c.krail.testapp.view

import com.google.inject.Inject
import com.vaadin.data.HasValue
import com.vaadin.ui.*
import uk.q3c.krail.core.form.EasyBinder
import uk.q3c.krail.core.view.ViewBase
import uk.q3c.krail.core.view.component.AfterViewChangeBusMessage
import uk.q3c.krail.core.view.component.ViewChangeBusMessage
import uk.q3c.krail.i18n.Translate
import javax.validation.constraints.Max

/**
 * Created by David Sowerby on 11 Feb 2018
 */
class AutoForm @Inject constructor(translate: Translate, private val binderBuilder: EasyBinder) : ViewBase(translate) {

    var layout: Layout = FormLayout()
    val validationMsg = Label()

    override fun doBuild(busMessage: ViewChangeBusMessage?) {
        rootComponent = layout
        layout.addComponent(validationMsg)
    }


    override fun loadData(busMessage: AfterViewChangeBusMessage?) {


        val binder = binderBuilder.auto(DummyForForm::class.java)
        val components = binder.buildAndBind()
        addComponentsToLayout(components)
        binder.bean = DummyForForm("who", 12)
        validationMsg.value = (components[1] as TextField).componentError.toString()
    }

    private fun addComponentsToLayout(components: Array<Component>) {
        for (c in components) {
            c.isEnabled = true
            if (c is HasValue<*>) {
                c.isReadOnly = false
            }
            layout.addComponent(c)
        }

    }
}

class DummyForForm(val name: String, @get:Max(5) val age: Int)