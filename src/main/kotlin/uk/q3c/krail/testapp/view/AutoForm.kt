package uk.q3c.krail.testapp.view

import com.google.common.collect.ImmutableList
import com.google.inject.Inject
import com.vaadin.data.converter.StringToIntegerConverter
import com.vaadin.ui.Button
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import uk.q3c.krail.core.form.KrailBeanValidationBinder
import uk.q3c.krail.core.form.KrailBeanValidationBinderFactory
import uk.q3c.krail.core.view.ViewBase
import uk.q3c.krail.core.view.component.AfterViewChangeBusMessage
import uk.q3c.krail.core.view.component.ViewChangeBusMessage
import uk.q3c.krail.i18n.Translate
import uk.q3c.krail.testapp.i18n.Caption
import uk.q3c.krail.testapp.i18n.DescriptionKey
import uk.q3c.krail.testapp.i18n.LabelKey
import uk.q3c.util.guice.SerializationSupport


/**
 * Created by David Sowerby on 11 Feb 2018
 */
class AutoForm @Inject
protected constructor(translate: Translate, serialisationSupport: SerializationSupport, @field:Transient private val binderFactory: KrailBeanValidationBinderFactory) : ViewBase(translate, serialisationSupport) {

    @Caption(caption = LabelKey.title, description = DescriptionKey.title)
    private val titleField = TextField()
    @Caption(caption = LabelKey.name, description = DescriptionKey.name)
    private val nameField = TextField()
    @Caption(caption = LabelKey.age, description = DescriptionKey.age)
    private val ageField = TextField()

    private val layout = FormLayout()
    private val validationMsg = Label()
    private var validateButton = Button("validate")

    @Transient
    private lateinit var binder: KrailBeanValidationBinder<Person>

    init {
        serializationSupport.excludedFieldNames = ImmutableList.of("binder")
    }

    override fun doBuild(busMessage: ViewChangeBusMessage) {
        rootComponent = layout
        layout.addComponent(titleField)
        layout.addComponent(nameField)
        layout.addComponent(ageField)
        layout.addComponent(validationMsg)
        layout.addComponent(validateButton)
        validateButton.addClickListener({
            validationMsg.value = if (ageField.componentError == null) {
                "No errors"
            } else {
                ageField.componentError.toString()
            }
        })
    }

    /**
     * Binder may not be initialised - it certainly won't be after deserialisation
     */
    private fun checkBinder() {
        try {
            binder.bean
        } catch (e: UninitializedPropertyAccessException) {
            binder = binderFactory.create(Person::class)
            bindFields()
        }
    }

    protected fun bindFields() {

        binder.bind(titleField, "title")
        binder.bind(nameField, "name")
        binder.forField(ageField).withConverter(StringToIntegerConverter("Rubbish error message"))
                .bind("age")
    }

    override fun loadData(busMessage: AfterViewChangeBusMessage?) {
        checkBinder()
        binder.bean = Person(title = "Dr", name = "Who", age = 12)
    }


}




















