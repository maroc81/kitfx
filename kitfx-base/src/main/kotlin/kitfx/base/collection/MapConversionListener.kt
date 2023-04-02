package kitfx.base.collection

import javafx.beans.WeakListener
import javafx.collections.MapChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import java.lang.ref.WeakReference
import java.util.HashMap


/**
 * Listens to changes on a Map of SourceTypeKey to SourceTypeValue and keeps the target list in sync by converting
 * each object into the TargetType via the supplied converter.
 *
 * Taken from tornadoFX source
 */
class MapConversionListener<SourceTypeKey, SourceTypeValue, TargetType>(
    /** The target list where converted objects are added and removed */
    targetList: MutableList<TargetType>,

    /** The operation for performing the conversion */
    val converter: (SourceTypeKey, SourceTypeValue) -> TargetType

) : MapChangeListener<SourceTypeKey, SourceTypeValue>, WeakListener {

    /** Weak reference to target list so it can be garbage collected */
    internal val targetRef: WeakReference<MutableList<TargetType>> = WeakReference(targetList)

    /** Map of source key/value pair to the converted item */
    internal val sourceToTarget = HashMap<Pair<SourceTypeKey, SourceTypeValue>, TargetType>()

    /**
     * When the source map changes, update target list
     */
    override fun onChanged(change: MapChangeListener.Change<out SourceTypeKey, out SourceTypeValue>) {
        val targetList = targetRef.get()
        if (targetList == null) {
            // Target list was garbage collected, remove our listener
            change.map.removeListener(this)
        } else {
            if (change.wasRemoved()) {
                // Change remove items from the map, get the converted item that
                // maps to the source key and value and remove from target list
                val removed = sourceToTarget.remove(Pair(change.key, change.valueRemoved))
                targetList.remove(removed)
            }
            if (change.wasAdded()) {
                // Change added items to the map, convert and add to target
                val converted = converter(change.key, change.valueAdded)
                sourceToTarget[Pair(change.key, change.valueAdded)] = converted
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

        // If other object is a map conversion listener, compare the target list references
        if (other is MapConversionListener<*, *, *>) {
            val otherList = other.targetRef.get()
            return ourList === otherList
        }
        return false
    }
}

/**
 * Extension function to bind the conversion to the target list
 */
fun <SourceTypeKey, SourceTypeValue, TargetType> MutableList<TargetType>.bindConversion(
    /** The source map that contains the objects to convert */
    sourceMap: ObservableMap<SourceTypeKey, SourceTypeValue>,

    /** The converter lambda */
    converter: (SourceTypeKey, SourceTypeValue) -> TargetType

): MapConversionListener<SourceTypeKey, SourceTypeValue, TargetType> {
    // Create the list conversion listener
    val listener = MapConversionListener(this, converter)
    sourceMap.forEach { (key, value) ->
        val converted = converter(key, value)
        listener.sourceToTarget[Pair(key, value)] = converted
    }
    if (this is ObservableList<TargetType>) {
        // Target list is observable, convert all source map items
        // and add them all to the target list
        setAll(listener.sourceToTarget.values)
    } else {
        // Target list is not observable, clear all items first
        // then add all
        clear()
        addAll(listener.sourceToTarget.values)
    }
    // Remove this listener from the source list if somehow it was already listening
    sourceMap.removeListener(listener)
    // Add the listener to the source map
    sourceMap.addListener(listener)
    return listener
}