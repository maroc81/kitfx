package kitfx.base.binding

import javafx.beans.binding.Bindings
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.value.ObservableValue
import kitfx.base.binding.SelectionListener
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1

/**
 * Extension function to extract an observable value from a parent observable value using a KFunction.
 */
fun <T, R> ObservableValue<T?>.select(function: KFunction<ObservableValue<R?>>): ObjectProperty<R?> {
    return SelectionListener(this, { function.call(it) })
}

fun <T, R> ObservableValue<T?>.select(property: KProperty1<T?, ObservableValue<R?>>): ObjectProperty<R?> {
    return SelectionListener(this, { property.get(it) })
}

/**
 *
 */
fun <T, R> ObservableValue<T?>.selectBinding(property: KProperty1<T, R>): ObjectBinding<R?> {
    return Bindings.select<R>(this, property.name)
}

fun <T, R> ObservableValue<T?>.select(nested: (T?) -> ObservableValue<R?>?): ObjectProperty<R?> {
    return SelectionListener(this, nested)
}

