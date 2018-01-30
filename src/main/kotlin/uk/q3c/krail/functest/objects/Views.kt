package uk.q3c.krail.functest.objects

import uk.q3c.krail.functest.ExecutionMode
import uk.q3c.krail.functest.ViewElement
import uk.q3c.krail.functest.coded.CodedViewElement
import uk.q3c.krail.functest.executionMode
import uk.q3c.krail.functest.selenide.SelenideViewElement
import kotlin.reflect.KProperty

/**
 * Created by David Sowerby on 30 Jan 2018
 */
class LoginViewObject(override val id: String) : ViewElement {
    val password by TextField()
}


interface TextFieldElement2 {
    val id: String
    fun captionShouldBe(expectedValue: String)

}

class CodedTextFieldElement2(val parent: CodedViewElement, override val id: String) : TextFieldElement2 {
    override fun captionShouldBe(expectedValue: String) {
        TODO()
    }

}

class SelenideTextFieldElement2(val parent: SelenideViewElement, override val id: String) : TextFieldElement2 {
    override fun captionShouldBe(expectedValue: String) {
        TODO()
    }

}


class TextField {
    operator fun getValue(thisRef: ViewElement, property: KProperty<*>): TextFieldElement2 {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideTextFieldElement2(thisRef as SelenideViewElement, property.name)
            ExecutionMode.CODED -> CodedTextFieldElement2(thisRef as CodedViewElement, property.name)
        }
    }
}



