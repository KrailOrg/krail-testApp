package uk.q3c.krail.functest

import uk.q3c.krail.functest.coded.*
import uk.q3c.krail.functest.selenide.*
import kotlin.reflect.KProperty

/**
 * Created by David Sowerby on 05 Feb 2018
 */
fun createBrowser() {
    when (executionMode) {
        ExecutionMode.SELENIDE -> browser = SelenideBrowser()
        ExecutionMode.CODED -> browser = CodedBrowser()
    }
    browser.setup()
    println(" =============================== BROWSER SETUP FOR ${executionMode} =======================================")
}


open class TextField {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): TextFieldElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideTextFieldElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedTextFieldElement("${thisRef.id}-${property.name}")
        }
    }
}

class TextArea {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): TextAreaElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideTextAreaElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedTextAreaElement("${thisRef.id}-${property.name}")
        }
    }
}

class PasswordField : TextField()

class Button {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): ButtonElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideButtonElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedButtonElement("${thisRef.id}-${property.name}")
        }
    }
}

class Label {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): LabelElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideLabelElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedLabelElement("${thisRef.id}-${property.name}")
        }
    }
}

class Grid {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): GridElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideGridElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedGridElement("${thisRef.id}-${property.name}")
        }
    }
}

class TreeGrid {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): TreeGridElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideTreeGridElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedTreeGridElement("${thisRef.id}-${property.name}")
        }
    }
}

class CheckBox {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): CheckBoxElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideCheckBoxElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedCheckBoxElement("${thisRef.id}-${property.name}")
        }
    }
}

class MenuBar {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): MenuBarElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideMenuBarElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedMenuBarElement("${thisRef.id}-${property.name}")
        }
    }
}

class Breadcrumb {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): BreadcrumbElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideBreadcrumbElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedBreadcrumbElement("${thisRef.id}-${property.name}")
        }
    }
}

class Tree {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): TreeElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideTreeElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedTreeElement("${thisRef.id}-${property.name}")
        }
    }
}

class Image {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): ImageElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideImageElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedImageElement("${thisRef.id}-${property.name}")
        }
    }
}


class ComboBox {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): ComboBoxElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideComboBoxElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedComboBoxElement("${thisRef.id}-${property.name}")
        }
    }
}

class Spinner {
    operator fun getValue(thisRef: ViewObject, property: KProperty<*>): SpinnerElement {
        return when (executionMode) {
            ExecutionMode.SELENIDE -> SelenideSpinnerElement("${thisRef.id}-${property.name}")
            ExecutionMode.CODED -> CodedSpinnerElement("${thisRef.id}-${property.name}")
        }
    }
}