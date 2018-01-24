package uk.q3c.krail.testapp.functest.coded

import com.vaadin.ui.Component
import com.vaadin.ui.HasComponents
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.testapp.functest.*
import java.util.*

/**
 * Created by David Sowerby on 23 Jan 2018
 */
class CodedButtonElement : ButtonElement

class CodedLabelElement : LabelElement
class CodedGridElement : GridElement
class CodedTextFieldElement : TextFieldElement
class CodedTextBoxElement : TextBoxElement

class AbstractCodedElement {
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