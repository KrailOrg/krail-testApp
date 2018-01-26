package uk.q3c.krail.functest.coded

import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import uk.q3c.krail.functest.Browser
import uk.q3c.krail.functest.LabelElement
import uk.q3c.krail.functest.TextFieldElement

class CodedBrowser : Browser {
    override fun setup() {
    }

    override fun element(label: Label): LabelElement {
        return CodedLabelElement(label)
    }

    override fun element(textField: TextField): TextFieldElement {
        return CodedTextFieldElement(textField)
    }

    override fun currentFragmentShouldBe(desiredFragment: String) {
        TODO()
    }

    override fun currentFragmentShouldNotBe(desiredFragment: String) {
        TODO()
    }

    override fun back() {
        TODO()
    }

    fun navigateTo(fragment: String) {

    }

}