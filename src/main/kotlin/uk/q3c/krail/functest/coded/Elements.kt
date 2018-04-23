package uk.q3c.krail.functest.coded

import com.google.common.base.Splitter
import com.vaadin.ui.*
import com.vaadin.ui.Button
import com.vaadin.ui.CheckBox
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Grid
import com.vaadin.ui.Image
import com.vaadin.ui.Label
import com.vaadin.ui.MenuBar
import com.vaadin.ui.TextArea
import com.vaadin.ui.TextField
import com.vaadin.ui.TreeGrid
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldNotBeNull
import org.openqa.selenium.InvalidArgumentException
import org.slf4j.LoggerFactory
import org.vaadin.spinkit.Spinner
import org.vaadin.spinkit.shared.SpinnerType
import uk.q3c.krail.core.ui.ScopedUI
import uk.q3c.krail.core.view.KrailView
import uk.q3c.krail.core.view.component.Breadcrumb
import uk.q3c.krail.functest.*
import java.lang.reflect.Field

abstract class AbstractCodedElement<T : Component>(final override val id: String) : BaseElement {
    private val log = LoggerFactory.getLogger(this.javaClass.name)
    private val viewElement: CompositeElement
    val view: Any
    protected var nativeField: T

    init {
        val rootId = id.split("-").first()
        if (browser.view.id == rootId) {
            viewElement = browser.view as CodedViewElement
            view = viewElement.view
        } else if (browser.page.id == rootId) {
            viewElement = browser.page as CodedPageElement
            view = viewElement.ui
        } else {
            throw NoSuchElementException("Root id of '$rootId' must be either the view or page element from the browser")
        }

        val segments = id.split("-")

        var obj: Any = view


        val iter = segments.iterator()
        iter.next() // ignore the first segment, it is the view / page

        while (iter.hasNext()) {
            val segment = iter.next()
            val fields = collectAllComponents(obj.javaClass, obj)
            val selectedField = fields.find({ f ->
                f.isAccessible = true
                val fieldContent = f.get(obj)
                if (fieldContent != null && (fieldContent is Component)) {
                    f.name == segment
                } else {
                    false
                }
            })
            if (selectedField != null) {
                selectedField.isAccessible = true
                val fieldContent = selectedField.get(obj)
                obj = fieldContent
            } else {
                throw NoSuchElementException("Could not find Coded component with an id of $id")
            }

        }

        @Suppress("UNCHECKED_CAST")
        nativeField = obj as T
    }

    override fun captionShouldBe(expectedCaption: String) {
        nativeField.caption.shouldNotBeNull()
        nativeField.caption.shouldBeEqualTo(expectedCaption)
    }

    // TODO duplicate of method in ComponentIdGenerator
    private fun <T> collectAllComponents(clazz: Class<out T>, obj: Any): List<Field> {
        var classToScan: Class<out Any?> = clazz
        log.debug("scanning '{}' to generate component ids", classToScan.name)
        val fields: MutableList<Field> = mutableListOf()
        try {
            var done = false
            while (!done) {
                fields.addAll(classToScan.declaredFields.filter({ f -> componentFilter(f, obj) }))
                done = (classToScan == Any::class.java) || (classToScan.superclass == null)
                if (!done) {
                    classToScan = classToScan.superclass
                }
            }
        } catch (iae: IllegalAccessException) {
            log.error("reflective scan of components failed", iae)
        }
        log.debug("${fields.size} fields selected")
        return fields
    }

    // TODO duplicate of method in ComponentIdGenerator
    private fun componentFilter(field: Field, obj: Any): Boolean {
        if (field.name == "parent") {
            return false
        }
        if (obj is KrailView) {
            if (field.name == "rootComponent") {
                return false
            }
        }
        if (obj is ScopedUI) {
            if (field.name == "screenLayout") {
                return false
            }
            if (field.name == "viewDisplayPanel") {
                return false
            }
            if (field.name == "scrollIntoView") {
                return false
            }
            if (field.name == "content") {
                return false
            }
            if (field.name == "pendingFocus") {
                return false
            }
        }

        return (Component::class.java.isAssignableFrom(field.type))
    }


}

class CodedGridElement(id: String) : GridElement, AbstractCodedElement<Grid<Any>>(id)
class CodedTreeGridElement(id: String) : TreeGridElement, AbstractCodedElement<TreeGrid<Any>>(id)

class CodedTextFieldElement(id: String) : TextFieldElement, AbstractCodedElement<TextField>(id) {

    override fun setValue(value: String) {
        nativeField.value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

}

class CodedCheckBoxElement(id: String) : CheckBoxElement, AbstractCodedElement<CheckBox>(id) {

    override fun setValue(value: Boolean) {
        nativeField.value = value
    }


    override fun valueShouldBe(expectedValue: Boolean) {
        nativeField.value.shouldBe(expectedValue)
    }

}

class CodedTextAreaElement(id: String) : TextAreaElement, AbstractCodedElement<TextArea>(id) {

    override fun setValue(value: String) {
        nativeField.value = value
    }


    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

}


class CodedLabelElement(id: String) : LabelElement, AbstractCodedElement<Label>(id) {

    override fun valueShouldBe(expectedValue: String) {
        nativeField.value.shouldBeEqualTo(expectedValue)
    }

}

class CodedButtonElement(id: String) : ButtonElement, AbstractCodedElement<Button>(id) {
    override fun click() {
        nativeField.click()
        (browser as CodedBrowser).update() // click may cause navigation change
    }
}

class CodedMenuBarElement(id: String) : MenuBarElement, AbstractCodedElement<MenuBar>(id) {
    override fun select(path: String) {
        if (path.isBlank()) {
            throw InvalidArgumentException("Menu path cannot be blank")
        }
        val segments = Splitter.on("/").split(path)
        val iter = segments.iterator()

        var selectedItem: MenuBar.MenuItem? = null
        var menuItems = nativeField.items
        while (iter.hasNext()) {
            val segment = iter.next()
            selectedItem = menuItems.find({ item ->
                item.text == segment
            }) ?: throw InvalidArgumentException("$path is not a menu item")
            menuItems = selectedItem.children
        }
        if (selectedItem == null) {
            throw InvalidArgumentException("$path is not a menu item")
        }
        if (selectedItem.hasChildren() || selectedItem.command == null) {
            throw InvalidArgumentException("The menu path $path must end with a MenuItem which has a command, and no children")
        }
        selectedItem.command.menuSelected(selectedItem)
        (browser as CodedBrowser).update()
    }
//        while (iter.hasNext()) {
//            currentItem = currentItem.children.find({ item -> item.text == iter.next() }) ?: throw InvalidArgumentException("${segments.first()} is menu item at this level")
//            currentItem.command.menuSelected(currentItem)
//        }
}


class CodedImageElement(id: String) : ImageElement, AbstractCodedElement<Image>(id)

class CodedComboBoxElement(id: String) : ComboBoxElement, AbstractCodedElement<ComboBox<*>>(id) {
    override fun valueShouldBe(expectedValue: String) {
        TODO()
    }

    override fun setValue(value: String) {
        TODO()
    }

}


class CodedTreeElement(id: String) : TreeElement, AbstractCodedElement<com.vaadin.ui.Tree<Any>>(id)

class CodedNotificationElement : NotificationElement {
    override fun shouldNotBeVisible() {
        TODO()
    }

    /**
     * Will always pass for coded see https://github.com/davidsowerby/krail-functest/issues/3
     */
    override fun shouldBeVisibleThenClose(level: NotificationLevel, text: String) {
        // TODO
    }
}

class CodedBreadcrumbElement(id: String) : BreadcrumbElement, AbstractCodedElement<Breadcrumb>(id) {
    override fun select(index: Int) {
        TODO()
    }

}
class CodedSpinnerElement(id: String): SpinnerElement, AbstractCodedElement<Spinner>(id) {

    override fun shouldBeVisible() {
        nativeField.isVisible.shouldBeTrue()
    }

    override fun shouldBeOfType(type: SpinnerType) {
        nativeField.type.shouldBe(type)
    }
}