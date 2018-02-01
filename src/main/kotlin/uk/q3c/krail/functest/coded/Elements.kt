package uk.q3c.krail.functest.coded

import com.vaadin.ui.TextField
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import uk.q3c.krail.functest.TextFieldElement
import uk.q3c.krail.functest.browser

class CodedTextFieldElement(override val id: String) : TextFieldElement {

    private val viewElement: CodedViewElement = browser.view as CodedViewElement
    val view = viewElement.view
    private val nativeField = nativeField()

    override fun setValue(value: String) {
        nativeField.value = value
    }




    override fun captionShouldBe(expectedCaption: String) {
        nativeField.caption.shouldNotBeNull()
        nativeField.caption.shouldBeEqualTo(expectedCaption)
    }

    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

    private fun nativeField(): TextField {
        val field = view.javaClass.getDeclaredField(id)
        field.isAccessible = true
        return field.get(view) as TextField
    }


}