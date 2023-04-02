package kitfx.base.collection

import javafx.beans.WeakListener
import javafx.collections.ObservableList
import javafx.collections.ObservableSet
import javafx.collections.SetChangeListener
import java.lang.ref.WeakReference
import java.util.HashMap


/**
 * Listens to changes on a set of SourceType and keeps the target list in sync by converting
 * each object into the TargetType via the supplied converter.
 *
 * Taken from tornadoFX source
 */
class SetConversionListener<SourceType, TargetType>(
    /** The target list where converted objects are added and removed */
    targetList: MutableList<TargetType>,

    /** The operation for performing the conversion */
    val converter: (SourceType) -> TargetType
) : SetChangeListener<SourceType>, WeakListener {

    /** Weak reference to target list so it can be garbage collected */
    internal val targetRef: WeakReference<MutableList<TargetType>> = WeakReference(targetList)

    /** Map of source item and the converted item */
    internal val sourceToTarget = HashMap<SourceType, TargetType>()

    /**
     * When the source list changes, update target list
     */
    override fun onChanged(change: SetChangeListener.Change<out SourceType>) {
        val targetList = targetRef.get()
        if (targetList == null) {
            // Target list was garbage collected, remove our listener
            change.set.removeListener(this)
            sourceToTarget.clear()
        } else {
            if (change.wasRemoved()) {
                // Change removed items, remove them from target
                targetList.remove(sourceToTarget[change.elementRemoved])
                sourceToTarget.remove(change.elementRemoved)
            }
            if (change.wasAdded()) {
                // Change added items, convert added items and add to target
                val converted = converter(change.elementAdded)
                sourceToTarget[change.elementAdded] = converted
                targetList.add(converted)
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

        // If other object is a set conversion listener, compare the target list references
        if (other is SetConversionListener<*, *>) {
            val otherList = other.targetRef.get()
            return ourList === otherList
        }
        return false
    }
}


/**
 * Bind this list to the given observable list by converting them into the correct type via the given converter.
 * Changes to the observable list are synced.
 */
fun <SourceType, TargetType> MutableList<TargetType>.bindConversion(
    /** The source list that contains the objects to convert */
    sourceSet: ObservableSet<SourceType>,

    /** The converter lambda */
    converter: (SourceType) -> TargetType
): SetConversionListener<SourceType, TargetType> {
    // Create the set conversion listener
    val listener = SetConversionListener(this, converter)
    if (this is ObservableList<*>) {
        sourceSet.forEach { source ->
            val converted = converter(source)
            listener.sourceToTarget[source] = converted
        }
        (this as ObservableList<TargetType>).setAll(listener.sourceToTarget.values)
    } else {
        clear()
        addAll(sourceSet.map(converter))
    }
    // Remove this listener from the source list if somehow it was already listening
    sourceSet.removeListener(listener)
    // Add the listener to the source list
    sourceSet.addListener(listener)
    return listener
}