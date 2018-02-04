package uk.q3c.krail.functest.coded

import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.TextField
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import uk.q3c.krail.functest.*

abstract class CodedBaseElement<out T : Component>(override val id: String) : BaseElement {
    private val viewElement: CodedViewElement = browser.view as CodedViewElement
    val view = viewElement.view
    protected val nativeField: T = nativeField()


    override fun captionShouldBe(expectedCaption: String) {
        nativeField.caption.shouldNotBeNull()
        nativeField.caption.shouldBeEqualTo(expectedCaption)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T> nativeField(): T {
        val field = view.javaClass.getDeclaredField(id)
        field.isAccessible = true
        return field.get(view) as T
    }

}

class CodedTextFieldElement(id: String) : TextFieldElement, CodedBaseElement<TextField>(id) {

    override fun setValue(value: String) {
        nativeField.value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

}

class CodedTextAreaElement(id: String) : TextAreaElement, CodedBaseElement<TextField>(id) {

    override fun setValue(value: String) {
        nativeField.value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

}


class CodedLabelElement(id: String) : LabelElement, CodedBaseElement<TextField>(id) {

    override fun setValue(value: String) {
        nativeField.value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

}

class CodedButtonElement(id: String) : ButtonElement, CodedBaseElement<Button>(id) {
    override fun click() {
        nativeField.click()
    }
}


