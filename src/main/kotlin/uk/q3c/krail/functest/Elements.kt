package uk.q3c.krail.functest

import java.util.*

/**
 * Created by David Sowerby on 23 Jan 2018
 */


interface BaseElement {
    //    val icon: Resource?
    fun captionShouldBe(expectedCaption: String)

    fun descriptionShouldBe(expectedDescription: String)
    fun localeShouldBe(expectedLocale: Locale)
    fun primaryStyleNameShouldBe(expectedPrimaryStyleName: String)
    fun styleNameShouldBe(expectedStyleName: String)
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

interface TextAreaElement : BaseElement, ValueElement<String>

