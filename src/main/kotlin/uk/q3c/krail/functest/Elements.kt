package uk.q3c.krail.functest

import uk.q3c.krail.functest.coded.CodedTextFieldElement
import uk.q3c.krail.functest.objects.ViewObject
import uk.q3c.krail.functest.selenide.SelenideTextFieldElement
import kotlin.reflect.KProperty

/**
 * Created by David Sowerby on 23 Jan 2018
 */


interface BaseElement {
    val id: String
    //    val icon: Resource?
    fun captionShouldBe(expectedCaption: String)
//    fun descriptionShouldBe(expectedDescription: String)
    //    fun localeShouldBe(expectedLocale: Locale)
//    fun primaryStyleNameShouldBe(expectedPrimaryStyleName: String)
//    fun styleNameShouldBe(expectedStyleName: String)
//    fun shouldBeEnabled()
//    fun shouldNotBeEnabled()
//    fun shouldBeVisible()
//    fun shouldNotBeVisible()

    // may need others from Focusable
}

interface ValueElement<in T> {
    //    fun requiredIndicatorShouldBeVisible()
//    fun requiredIndicatorShouldNotBeVisible()
    fun valueShouldBe(expectedValue: T)

    fun setValue(value: T)
}

interface LabelElement : BaseElement

interface ButtonElement : BaseElement {
    fun click()
}

interface GridElement : BaseElement


interface TextFieldElement : BaseElement, ValueElement<String>

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
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): TextFieldElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideTextFieldElement(property.name)
            ExecutionMode.CODED -> CodedTextFieldElement(property.name)
        }
    }
}

