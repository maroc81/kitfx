package kitfx.base.property

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty


fun <T> objectProperty(initial: T? = null): ObjectProperty<T> = SimpleObjectProperty(initial)

fun <T> objectProperty(bean: Any? = null, name: String = "", initial: T? = null ): ObjectProperty<T> {
    return SimpleObjectProperty(bean, name, initial)
}