package uk.q3c.krail.functest.coded

import com.vaadin.data.HasValue
import com.vaadin.ui.*
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeNull
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.functest.*
import java.util.*

/**
 * Created by David Sowerby on 23 Jan 2018
 */
class CodedButtonElement(button: Button) : AbstractCodedElement(button), ButtonElement {
    override fun click() {
        (component as Button).click()
    }

}

class CodedLabelElement(label: Label) : AbstractCodedElement(label), LabelElement
class CodedGridElement<T>(grid: Grid<T>) : AbstractCodedElement(grid), GridElement

class CodedTextFieldElement(private val textField: TextField) : CodedValueStringElement(textField), TextFieldElement

class CodedTextBoxElement(private val textArea: TextArea) : CodedValueStringElement(textArea), TextAreaElement


@Suppress("UNCHECKED_CAST")
abstract class CodedValueElement<T>(private val hasValue: HasValue<T>) : AbstractCodedElement(hasValue as Component), ValueElement<T> {
    override fun requiredIndicatorShouldBeVisible() {
        TODO()
    }

    override fun requiredIndicatorShouldNotBeVisible() {
        TODO()
    }

}

abstract class CodedValueStringElement(private val hasValue: HasValue<String>) : CodedValueElement<String>(hasValue) {
    override fun valueShouldBe(expectedValue: String) {
        hasValue.value.shouldBeEmpty()
    }
}

abstract class AbstractCodedElement(val component: Component) : BaseElement {
    override fun captionShouldBe(expectedCaption: String?) {
        if (expectedCaption == null) {
            component.caption.shouldBeNull()
        } else {
            component.caption.shouldBe(expectedCaption)
        }
    }

    override fun descriptionShouldBe(expectedDescription: String) {
        TODO()
    }



    override fun shouldBeEnabled() {
        TODO()
    }

    override fun shouldNotBeEnabled() {
        TODO()
    }

    override fun shouldBeVisible() {
        TODO()
    }

    override fun shouldNotBeVisible() {
        TODO()
    }
}


inline fun <reified T> locateComponent(view: KrailView, id: String): T {
    val walker = VaadinComponentTreeWalker(view.rootComponent)
    val selected = walker.selectById(id)
    if (selected == null) {
        throw ComponentNotFoundException(id, view)
    } else {
        if (selected is T) {
            return selected
        } else {
            throw ComponentUnexpectedTypeException(id, selected.javaClass)
        }
    }
}


class ComponentUnexpectedTypeException(id: String, actualClass: Class<Component>) : RuntimeException("Component with id=$id was not expected to be a $actualClass")

class ComponentNotFoundException(id: String, view: KrailView) : RuntimeException("Component with $id not found in ${view.javaClass}")


class VaadinComponentTreeWalker(private val rootComponent: Component) {
    private val stack: Stack<Component> = Stack()

    fun selectById(id: String): Component? {
        stack.add(rootComponent)
        while (stack.isNotEmpty()) {
            val c = stack.pop()
            if (c.id == id) {
                return c
            }

            if (c is HasComponents) {
                c.iterator().forEach { i -> stack.push(i) }
            }

        }
        return null
    }
}

interface ComponentVisitable {
    fun accept(visitor: ComponentVisitor<ComponentVisitable>)
}

interface ComponentVisitor<in T : ComponentVisitable> {
    fun visit(node: T)
}

