package uk.q3c.krail.functest

import org.vaadin.spinkit.shared.SpinnerType

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

    /**
     * Uses Selenide's setValue instead of sendKeys.  Using this sometimes causes timing issues with event handling.  With validation, for example,
     * Validators were receiving null values with [setValue], but work with [sendValue]
     *
     * @see setValue
     */

    fun setValue(value: T)

    /**
     * Uses Selenide's sendKeys instead of setValue.  This may be necessary sometimes to avoid timing issues with event handling.  With validation, for example,
     * Validators were receiving null values with [setValue], but work with [sendValue]
     *
     * @see setValue
     */
    fun sendValue(value: T)
}

interface LabelElement : BaseElement {
    fun valueShouldBe(expectedValue: String)
}

interface CheckBoxElement : BaseElement, ValueElement<Boolean>
interface ComboBoxElement : BaseElement, ValueElement<String>

interface ButtonElement : BaseElement {
    fun click()
}

interface GridElement : BaseElement
interface TreeGridElement : BaseElement
interface MenuBarElement : BaseElement {
    fun select(path: String)
}


interface TextBaseElement : ValueElement<String> {
    fun sendBackspace(times: Int)
    fun sendBackspace()
    fun sendBackspaceUntilClear()
    fun sendEnter()
}

interface TextFieldElement : BaseElement, TextBaseElement

interface TextAreaElement : BaseElement, TextBaseElement

interface TreeElement : BaseElement
interface ImageElement : BaseElement

interface CompositeElement {
    val id: String
}

interface ViewElement : CompositeElement

interface PageElement : CompositeElement

interface NotificationElement {

    fun shouldBeVisibleThenClose(level: NotificationLevel, text: String)
    fun shouldNotBeVisible()
}

interface BreadcrumbElement : BaseElement {
    fun select(index: Int)

}

interface SpinnerElement : BaseElement {
    fun shouldBeVisible()
    fun shouldBeOfType(type: SpinnerType)
}

