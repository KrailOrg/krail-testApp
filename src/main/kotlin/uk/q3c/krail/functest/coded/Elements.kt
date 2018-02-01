package uk.q3c.krail.functest.coded

import com.vaadin.ui.Component
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import uk.q3c.krail.functest.TextFieldElement
import uk.q3c.krail.functest.browser

class CodedTextFieldElement(override val id: String) : TextFieldElement {
    private val viewElement: CodedViewElement = browser.view as CodedViewElement
    val view = viewElement.view


    override fun captionShouldBe(expectedCaption: String) {
        val field = view.javaClass.getDeclaredField(id)
        field.isAccessible = true
        val c = field.get(view) as Component
        c.caption.shouldNotBeNull()
        c.caption.shouldBeEqualTo(expectedCaption)
    }

    override fun valueShouldBe(expectedValue: String) {
        TODO()
    }


}