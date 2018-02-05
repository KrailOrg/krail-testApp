package uk.q3c.krail.functest.coded

import com.vaadin.ui.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import uk.q3c.krail.functest.*

abstract class AbstractCodedElement<out T : Component>(override val id: String) : BaseElement {
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

class CodedGridElement(id: String) : GridElement, AbstractCodedElement<Grid<Any>>(id)
class CodedTreeGridElement(id: String) : TreeGridElement, AbstractCodedElement<TreeGrid<Any>>(id)

class CodedTextFieldElement(id: String) : TextFieldElement, AbstractCodedElement<TextField>(id) {

    override fun setValue(value: String) {
        nativeField.value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

}

class CodedCheckBoxElement(id: String) : CheckBoxElement, AbstractCodedElement<CheckBox>(id) {

    override fun setValue(value: Boolean) {
        nativeField.value = value
    }


    override fun valueShouldBe(expectedValue: Boolean) {
        nativeField.value.shouldBe(expectedValue)
    }

}

class CodedTextAreaElement(id: String) : TextAreaElement, AbstractCodedElement<TextField>(id) {

    override fun setValue(value: String) {
        nativeField.value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

}


class CodedLabelElement(id: String) : LabelElement, AbstractCodedElement<TextField>(id) {

    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

}

class CodedButtonElement(id: String) : ButtonElement, AbstractCodedElement<Button>(id) {
    override fun click() {
        nativeField.click()
    }
}


