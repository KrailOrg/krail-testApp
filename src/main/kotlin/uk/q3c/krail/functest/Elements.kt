package uk.q3c.krail.functest

import uk.q3c.krail.functest.coded.CodedButtonElement
import uk.q3c.krail.functest.coded.CodedLabelElement
import uk.q3c.krail.functest.coded.CodedTextAreaElement
import uk.q3c.krail.functest.coded.CodedTextFieldElement
import uk.q3c.krail.functest.selenide.SelenideButtonElement
import uk.q3c.krail.functest.selenide.SelenideLabelElement
import uk.q3c.krail.functest.selenide.SelenideTextAreaElement
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

interface LabelElement : BaseElement {
    fun setValue(value: String)
    fun valueShouldBe(expectedValue: String)
}

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


open class TextField {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): TextFieldElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideTextFieldElement(property.name)
            ExecutionMode.CODED -> CodedTextFieldElement(property.name)
        }
    }
}

class TextArea {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): TextAreaElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideTextAreaElement(property.name)
            ExecutionMode.CODED -> CodedTextAreaElement(property.name)
        }
    }
}

class PasswordField : TextField()

class Button {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): ButtonElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideButtonElement(property.name)
            ExecutionMode.CODED -> CodedButtonElement(property.name)
        }
    }
}

class Label {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): LabelElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideLabelElement(property.name)
            ExecutionMode.CODED -> CodedLabelElement(property.name)
        }
    }
}
