package uk.q3c.krail.functest

import uk.q3c.krail.functest.coded.CodedTextFieldElement2
import uk.q3c.krail.functest.coded.CodedViewElement
import uk.q3c.krail.functest.selenide.SelenideTextFieldElement2
import uk.q3c.krail.functest.selenide.SelenideViewElement
import kotlin.reflect.KProperty

/**
 * Created by David Sowerby on 23 Jan 2018
 */


interface BaseElement {
    //    val icon: Resource?
    fun captionShouldBe(expectedCaption: String?)
    fun descriptionShouldBe(expectedDescription: String)
    //    fun localeShouldBe(expectedLocale: Locale)
//    fun primaryStyleNameShouldBe(expectedPrimaryStyleName: String)
//    fun styleNameShouldBe(expectedStyleName: String)
    fun shouldBeEnabled()
    fun shouldNotBeEnabled()
    fun shouldBeVisible()
    fun shouldNotBeVisible()

    // may need others from Focusable
}

interface ValueElement<in T> {
    fun requiredIndicatorShouldBeVisible()
    fun requiredIndicatorShouldNotBeVisible()
    fun valueShouldBe(expectedValue: T)
}

interface LabelElement : BaseElement

interface ButtonElement : BaseElement {
    fun click()
}

interface GridElement : BaseElement

interface TextFieldElement : BaseElement, ValueElement<String>

interface TextFieldElement2 {
    val id: String
    fun captionShouldBe(expectedValue: String)

}

interface TextAreaElement : BaseElement, ValueElement<String>

interface ViewElement {
    val id: String
}

interface UIElement

interface PageObject {
    val view: ViewElement
    val ui: UIElement
}


class TextField {
    operator fun getValue(thisRef: ViewElement, property: KProperty<*>): TextFieldElement2 {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideTextFieldElement2(thisRef as SelenideViewElement, property.name)
            ExecutionMode.CODED -> CodedTextFieldElement2(thisRef as CodedViewElement, property.name)
        }
    }
}

