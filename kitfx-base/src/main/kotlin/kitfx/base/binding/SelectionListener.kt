package kitfx.base.binding

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableValue

/**
 * Class that binds to a nested observable property of a parent observable property.
 */
class SelectionListener<T, N>(
    /** The parent observable value to select the nested value */
    private val parent: ObservableValue<T?>,

    /** The extractor for the nested value */
    private val nested: (T?) -> ObservableValue<N?>?
) : SimpleObjectProperty<N?>() {

    /** The current nested observable */
    var currentNested: ObservableValue<N?>? = extractNested()

    /** Listener to invalidate */
    val changeListener = ChangeListener<Any?> { _, _, _ ->
        invalidated()
        // Fire change event since binding was invalidated
        fireValueChangedEvent()
    }

    init {
        // Add listeners to change the
        currentNested?.addListener(changeListener)
        parent.addListener(changeListener)
    }

    /**
     * Extracts the nested observable from the parent observable
     */
    private fun extractNested(): ObservableValue<N?>? = parent.value?.let(nested)

    /**
     * Removes the change listener from the previous observable and
     * binds to the new observable
     */
    override fun invalidated() {
        currentNested?.removeListener(changeListener)
        currentNested = extractNested()
        currentNested?.addListener(changeListener)
    }

    /**
     * Returns the value of the nested observable which is
     * of type [N]
     */
    override fun get(): N? = currentNested?.value

    /**
     * Sets the value of the nest observable of type [N]
     */
    override fun set(v: N?) {
        (currentNested as WritableValue<*>).value = v
        super.set(v)
    }

}