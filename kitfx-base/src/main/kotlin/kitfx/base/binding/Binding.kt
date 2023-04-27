package kitfx.base.binding

import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import kitfx.base.property.objectProperty
import kotlin.reflect.KProperty1

/**
 * Extension function to extract a property from a parent observable value using a KProperty
 */
fun <T, R> ObservableValue<T>.select(property: KProperty1<T, ObservableValue<R>>): Property<R> {
    return NestedProperty(this, { property.get(it) })
}

/**
 * Extension function to create a property that updates when the parent observable value changes.
 * When the observable value changes, the specified [nested] lambda or function is executed to
 * extract the nested property, bind to it and update this property
 *
 */
fun <T, R> ObservableValue<T>.select(nested: (T) -> ObservableValue<R>): Property<R> {
    return NestedProperty(this, nested)
}

/**
 * Extension function to extract a property from a parent observable value using a lambda
 * or return a property with null as the value
 *
 * This is best used when T is nullable but a property must always be returned even when
 * the parent value is null.  For example:
 *
 * val personProperty = SimpleObjectProperty<Person?>(null)
 * val personName: Property<String?> = personProperty.selectOrNull { it?.nameProperty() }
 *
 * This will return a string property with value set to null when personProperty value is set to null
 *
 * This extension function is equivalent to observable.select { it?.nestedProperty() ?: objectProperty(null) }
 */
fun <T, R> ObservableValue<T>.selectOrNull(nested: (T) -> ObservableValue<R>?): Property<R> {
    return NestedProperty(this) {
        nested.invoke(it) ?: objectProperty(null)
    }
}

/**
 * Extension function to extract a property from a parent observable value using a lambda
 * or return a property with [default] value if lambda returns null.
 *
 * This is best used when T is nullable but a property must always be returned even when
 * the parent value is null.  For example:
 *
 * val personProperty = SimpleObjectProperty<Person?>(null)
 * val personName: Property<String> = personProperty.selectOrDefault("Person Is Null") { it?.nameProperty() }
 *
 * This will return a string property with the string value "Person Is Null" when personProperty value is set to null
 *
 * This extension function is equivalent to observable.select { it?.nestedProperty() ?: objectProperty(defaultValue) }
 */
fun <T, R> ObservableValue<T>.selectOrDefault(default: R, nested: (T) -> ObservableValue<R>?): Property<R> {
    return NestedProperty(this) {
        nested.invoke(it) ?: objectProperty(default)
    }
}
