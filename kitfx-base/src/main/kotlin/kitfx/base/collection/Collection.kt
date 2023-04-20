package kitfx.base.collection

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TableView

/**
 * Convenience function for creating an empty observable array list
 */
fun <T> observableList(): ObservableList<T> = FXCollections.observableArrayList<T>()

/**
 * Convenience function for creating an observable hash map
 */
fun <K,V> observableMap() = FXCollections.observableHashMap<K,V>()

/**
 * Convenience function for creating an empty observable set
 */
fun <E> observableSet() = FXCollections.observableSet<E>()

/**
 * Moves the given element at specified index up the **MutableList** by one increment
 * unless it is at the top already which will result in no movement
 */
fun <T> MutableList<T>.moveUpAt(index: Int): Int {
    if (index == 0) return index
    check(index in indices, { "Invalid index $index for MutableList of size $size" })
    val newIndex = index - 1
    val item = this[index]
    removeAt(index)
    add(newIndex, item)
    return newIndex
}

/**
 * Moves the given element **T** up the **MutableList** by one increment
 * unless it is at the bottom already which will result in no movement
 */
fun <T> MutableList<T>.moveDownAt(index: Int): Int {
    if (index == size - 1) return index
    check(index in indices, { "Invalid index $index for MutableList of size $size" })
    val newIndex = index + 1
    val item = this[index]
    removeAt(index)
    if (newIndex == size) {
        add(item)
    } else {
        add(newIndex, item)
    }
    return newIndex
}

/**
 * Moves the given element **T** up the **MutableList** by an index increment
 * unless it is at the top already which will result in no movement.
 * Returns a `Boolean` indicating if move was successful
 */
fun <T> MutableList<T>.moveUp(item: T): Boolean {
    val currentIndex = indexOf(item)
    if (currentIndex == -1) return false
    val newIndex = (currentIndex - 1)
    if (currentIndex <= 0) return false
    remove(item)
    add(newIndex, item)
    return true
}

/**
 * Moves the given element **T** up the **MutableList** by an index increment
 * unless it is at the bottom already which will result in no movement.
 * Returns a `Boolean` indicating if move was successful
 */
fun <T> MutableList<T>.moveDown(item: T): Boolean {
    val currentIndex = indexOf(item)
    if (currentIndex == -1) return false
    val newIndex = (currentIndex + 1)
    if (newIndex >= size) return false
    remove(item)
    if (newIndex == size) {
        add(item)
    } else {
        add(newIndex, item)
    }
    return true
}

/**
 * Moves a selection in a table up and then selects the item again
 */
fun <T> TableView<T>.moveSelectionUp() {
    val item = selectionModel.selectedItem
    if (items.moveUp(item)) {
        selectionModel.clearSelection()
        selectionModel.select(item)
    }
}

/**
 * Moves a selection in a table up and then selects the item again
 */
fun <T> TableView<T>.moveSelectionsUp() {
    val selectedIndices = selectionModel.selectedIndices.toList().sorted()
    if (selectedIndices.isEmpty()) return
    val newSelection = IntArray(selectedIndices.size)
    // Don't move lower entries higher than the ones above it
    var min = -1
    selectedIndices.forEachIndexed { index, curIndex ->
        val newIndex = curIndex - 1
        min = if (newIndex > min) items.moveUpAt(curIndex) else curIndex
        newSelection[index] = min
    }
    selectionModel.clearSelection()
    newSelection.forEach { selectionModel.select(it) }
}

/**
 * Moves a selection in a table down and then selects the item again
 */
fun <T> TableView<T>.moveSelectionDown() {
    val item = selectionModel.selectedItem
    if (items.moveDown(item)) {
        selectionModel.clearSelection()
        selectionModel.select(item)
    }
}

/**
 * Moves selections in a table down and then selects the items again
 */
fun <T> TableView<T>.moveSelectionsDown() {
    val selectedIndices = selectionModel.selectedIndices.toList().sorted()
    if (selectedIndices.isEmpty()) return
    val newSelection = IntArray(selectedIndices.size)
    // Don't move higher entries lower than the ones below it
    var max = items.size
    for (index in selectedIndices.lastIndex downTo 0) {
        val curIndex = selectedIndices[index]
        val newIndex = curIndex + 1
        max = if (newIndex < max) items.moveDownAt(curIndex) else curIndex
        newSelection[index] = max
    }
    selectionModel.clearSelection()
    newSelection.forEach { selectionModel.select(it) }
}