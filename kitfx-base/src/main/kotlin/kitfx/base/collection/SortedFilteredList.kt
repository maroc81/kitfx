package kitfx.base.collection

import javafx.beans.InvalidationListener
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.collections.transformation.SortedList
import javafx.scene.control.ListView
import javafx.scene.control.TableView
import java.util.function.Predicate

/**
 * A wrapper for an observable list of items that can be bound to a list control like TableView, ListView etc.
 *
 * The wrapper makes the data sortable and filterable. Configure a filter by setting the
 * predicate property or by calling filterWhen to automatically update the predicate when
 * an observable value changes.
 *
 ** Usage:
 *
 * ```kotlin
 * val table = TableView<Person>()
 * val data = SortedFilteredList(persons).bindTo(table)
 * ```
 *
 * Items can be updated by calling `data.items.setAll` or `data.items.addAll` at a later time.
 *
 * Copied from TornadoFX
 */
@Suppress("UNCHECKED_CAST")
class SortedFilteredList<T>(
    /** The original list of items to sort and filter */
    val items: ObservableList<T> = FXCollections.observableArrayList(),

    /** Initial filter predicate */
    initialPredicate: (T) -> Boolean = { true },

    /** The filtered list */
    val filteredItems: FilteredList<T> = FilteredList(items, initialPredicate),

    /** The sorted list */
    val sortedItems: SortedList<T> = SortedList(filteredItems)
) : ObservableList<T> {

    init {
        items.addListener(InvalidationListener { refilter() })
    }

    // Should setAll be forwarded to the underlying list? This might be needed for full editing capabilities,
    // but will affect the ordering of the underlying list
    var setAllPassThrough = false
    override val size: Int get() = sortedItems.size
    override fun contains(element: T) = element in sortedItems
    override fun containsAll(elements: Collection<T>) = sortedItems.containsAll(elements)
    override fun get(index: Int) = sortedItems[index]
    override fun indexOf(element: T) = sortedItems.indexOf(element)
    override fun isEmpty() = sortedItems.isEmpty()
    override fun iterator() = sortedItems.iterator()
    override fun lastIndexOf(element: T) = sortedItems.lastIndexOf(element)
    override fun add(element: T) = items.add(element)
    override fun add(index: Int, element: T) {
        val item = sortedItems[index]
        val backingIndex = items.indexOf(item)
        if (backingIndex > -1) {
            items.add(backingIndex, element)
        }
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val item = sortedItems[index]
        val backingIndex = items.indexOf(item)
        if (backingIndex > -1) {
            return items.addAll(backingIndex, elements)
        }
        return false
    }

    override fun addAll(elements: Collection<T>) = items.addAll(elements)

    override fun clear() = items.clear()
    override fun listIterator() = sortedItems.listIterator()
    override fun listIterator(index: Int) = sortedItems.listIterator(index)
    override fun remove(element: T) = items.remove(element)
    override fun removeAll(elements: Collection<T>) = items.removeAll(elements)
    override fun removeAt(index: Int): T? {
        val item = sortedItems[index]
        val backingIndex = items.indexOf(item)
        return if (backingIndex > -1) {
            items.removeAt(backingIndex)
        } else {
            null
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        val item = sortedItems[fromIndex]
        val backingFromIndex = items.indexOf(item)
        if (backingFromIndex > -1) {
            return items.subList(backingFromIndex, items.indexOf(sortedItems[toIndex - 1]))
        }
        return mutableListOf()
    }

    override fun removeAll(vararg elements: T) = items.removeAll(elements)

    override fun addAll(vararg elements: T) = items.addAll(elements)

    override fun remove(from: Int, to: Int) {
        val item = sortedItems[from]
        val backingFromIndex = items.indexOf(item)
        if (backingFromIndex > -1) {
            items.remove(backingFromIndex, items.indexOf(sortedItems[to]))
        }
    }

    override fun retainAll(vararg elements: T) = items.retainAll(elements)

    override fun retainAll(elements: Collection<T>) = items.retainAll(elements)

    override fun removeListener(listener: ListChangeListener<in T>?) {
        sortedItems.removeListener(listener)
    }

    override fun removeListener(listener: InvalidationListener?) {
        sortedItems.removeListener(listener)
    }

    override fun addListener(listener: ListChangeListener<in T>?) {
        sortedItems.addListener(listener)
    }

    override fun addListener(listener: InvalidationListener?) {
        sortedItems.addListener(listener)
    }

    override fun setAll(col: MutableCollection<out T>?) = if (setAllPassThrough) items.setAll(col) else false

    override fun setAll(vararg elements: T) = items.setAll(*elements)

    /**
     * Support editing of the sorted/filtered list. Useful to support editing support in ListView/TableView etc
     */
    override fun set(index: Int, element: T): T {
        val item = sortedItems[index]
        val backingIndex = items.indexOf(item)
        if (backingIndex > -1) {
            items[backingIndex] = element
        }
        return item
    }


    /**
     * Force the filtered list to refilter it's items based on the current predicate without having to configure a new predicate.
     * Avoid reassigning the property value as that would impede binding.
     */
    fun refilter() {
        val p = predicate
        if (p != null) {
            filteredItems.predicate = Predicate { p(it) }
        }
    }

    val predicateProperty: ObjectProperty<(T) -> Boolean> = object : SimpleObjectProperty<(T) -> Boolean>() {
        override fun set(newValue: ((T) -> Boolean)) {
            super.set(newValue)
            filteredItems.predicate = Predicate { newValue(it) }
        }


    }
    var predicate: ((T) -> Boolean); get() = predicateProperty.get(); set(value) = predicateProperty.set(value)

    /**
     * Bind this data object to the given TableView.
     *
     * The `tableView.items` is set to the underlying sortedItems.
     *
     * The underlying sortedItems.comparatorProperty` is automatically bound to `tableView.comparatorProperty`.
     */
    fun bindTo(tableView: TableView<T>): SortedFilteredList<T> = apply {
        tableView.items = this
        sortedItems.comparatorProperty().bind(tableView.comparatorProperty())
    }

    /**
     * Bind this data object to the given ListView.
     *
     * The `listView.items` is set to the underlying sortedItems.
     *
     */
    fun bindTo(listView: ListView<T>): SortedFilteredList<T> = apply { listView.items = this }

    /**
     * Update the filter predicate whenever the given observable changes. The filter expression
     * receives both the observable value and the current list item to evaluate.
     *
     * Convenient for filtering based on a TextField:
     *
     * <pre>
     * textfield {
     *     promptText = "Filtrering"
     *     data.filterWhen(textProperty(), { query, item -> item.matches(query) } )
     * }
     * </pre>
     */
    fun <Q> filterWhen(observable: ObservableValue<Q>, filterExpr: (Q, T) -> Boolean) {
        observable.addListener { observableValue, oldValue, newValue ->
            predicate = { filterExpr(newValue, it) }
        }
    }
}