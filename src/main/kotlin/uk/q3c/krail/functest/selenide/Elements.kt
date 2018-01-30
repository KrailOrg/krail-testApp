package uk.q3c.krail.functest.selenide

import uk.q3c.krail.functest.TextFieldElement2

/**
 * Created by David Sowerby on 30 Jan 2018
 */

class SelenideTextFieldElement2(val parent: SelenideViewElement, override val id: String) : TextFieldElement2 {
    override fun captionShouldBe(expectedValue: String) {
        TODO()
    }

}