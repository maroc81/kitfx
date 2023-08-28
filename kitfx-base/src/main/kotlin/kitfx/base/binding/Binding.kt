package kitfx.base.binding

import javafx.beans.Observable
import javafx.beans.binding.*
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

/**
 * Convenience function for creating a string binding from an observable value.
 */
fun <T> ObservableValue<T>.stringBinding(vararg dependencies: Observable, op: (T) -> String): StringBinding {
    return Bindings.createStringBinding({ op(this.value) }, this, *dependencies)
}

/**
 * Convenience function for creating a boolean binding from an observable value.
 */
fun <T> ObservableValue<T>.booleanBinding(vararg dependencies: Observable, op: (T) -> Boolean): BooleanBinding {
    return Bindings.createBooleanBinding({ op(this.value) }, this, *dependencies)
}

/**
 * Convenience function for creating an integer binding from an observable value.
 */
fun <T> ObservableValue<T>.integerBinding(vararg dependencies: Observable, op: (T?) -> Int): IntegerBinding {
    return Bindings.createIntegerBinding({ op(this.value) }, this, *dependencies)
}

/**
 * Convenience function for creating an integer binding from an observable value.
 */
fun <T> ObservableValue<T>.longBinding(vararg dependencies: Observable, op: (T?) -> Long): LongBinding {
    return Bindings.createLongBinding({ op(this.value) }, this, *dependencies)
}

/**
 * Convenience function for creating a double binding from an observable value.
 */
fun <T> ObservableValue<T>.doubleBinding(vararg dependencies: Observable, op: (T?) -> Double): DoubleBinding {
    return Bindings.createDoubleBinding({ op(this.value) }, this, *dependencies)
}

/**
 * Convenience function for creating a float binding from an observable value.
 */
fun <T> ObservableValue<T>.floatBinding(vararg dependencies: Observable, op: (T?) -> Float): FloatBinding {
    return Bindings.createFloatBinding({ op(this.value) }, this, *dependencies)
}
