package kitfx.base.collection

import javafx.beans.WeakListener
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import java.lang.ref.WeakReference

/**
 * Listener for converting objects from a list of [SourceType] to a list of [TargetType]
 * as they are added, removed, or mutated in source list
 *
 * Taken from tornadoFX source
 */
class ListConversionListener<SourceType, TargetType>(
    /** The target list where converted objects are added and removed */
    targetList: MutableList<TargetType>,

    /** The operation for performing the conversion */
    val converter: (SourceType) -> TargetType

) : ListChangeListener<SourceType>, WeakListener {

    /** Weak reference to target list so it can be garbage collected */
    private val targetRef: WeakReference<MutableList<TargetType>> = WeakReference(targetList)

    /**
     * When the source list changes, update target list
     */
    override fun onChanged(change: ListChangeListener.Change<out SourceType>) {
        val targetList = targetRef.get()
        if (targetList == null) {
            // Target list was garbage collected, remove our listener
            change.list.removeListener(this)
        } else {
            while (change.next()) {
                if (change.wasPermutated()) {
                    // Source list order was changed, clear the changed range form target,
                    // convert the changed ranged from source, and add back to target
                    targetList.subList(change.from, change.to).clear()
                    targetList.addAll(change.from, change.list.subList(change.from, change.to).map(converter))
                } else {
                    if (change.wasRemoved()) {
                        // Change removed items, remove them from target
                        targetList.subList(change.from, change.from + change.removedSize).clear()
                    }
                    if (change.wasAdded()) {
                        // Change added items, convert added items and add to target
                        targetList.addAll(change.from, change.addedSubList.map(converter))
                    }
                }
            }
        }
    }

    /**
     * When garbage collected, release the target list reference
     */
    override fun wasGarbageCollected() = targetRef.get() == null

    /**
     * Hashcode using target list reference hash code
     */
    override fun hashCode() = targetRef.get().hashCode()

    /**
     * Equals comparing this class instance and the target list reference
     */
    override fun equals(other: Any?): Boolean {
        // If same instance of this class, no other checks needed
        if (this === other) {
            return true
        }

        // Save our list reference or return false if target reference is null
        val ourList = targetRef.get() ?: return false

        // If other object is a list conversion listener, compare the target list references
        if (other is ListConversionListener<*, *>) {
            val otherList = other.targetRef.get()
            return ourList === otherList
        }
        return false
    }
}

/**
 * Extension function to bind the conversion to the target list
 */
fun <SourceType, TargetType> MutableList<TargetType>.bindConversion(
    /** The source list that contains the objects to convert */
    sourceList: ObservableList<SourceType>,

    /** The converter lambda */
    converter: (SourceType) -> TargetType

): ListConversionListener<SourceType, TargetType> {
    // Create the list conversion listener
    val listener = ListConversionListener(this, converter)
    if (this is ObservableList<TargetType>) {
        // Target list is observable, so use "setAll" function
        setAll(sourceList.map(converter))
    } else {
        // Target list is not observable, clear all items first
        // then add all
        // Note: This uses "run" I think so this code blocks if called from a coroutine
        // so that code doesn't yield between clearing and populating???
        run {
            clear()
            addAll(sourceList.map(converter))
        }
    }
    // Remove this listener from the source list if somehow it was already listening
    sourceList.removeListener(listener)
    // Add the listener to the source list
    sourceList.addListener(listener)
    return listener
}
