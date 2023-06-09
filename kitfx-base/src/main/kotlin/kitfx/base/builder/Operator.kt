package kitfx.base.builder

import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.Pane



/**
 * Adds a menu item of type [T] to the context menu's list of items
 */
operator fun <T: MenuItem> ContextMenu.plus(item: T): T {
    items.add(item)
    return item
}

/**
 * Adds a node of type [T] to the pane's list of children
 *
 * kvbox {
 *     this + klabel("Label")
 * }
 */
operator fun <T: Node> Pane.plus(node: T): T {
    children.add(node)
    return node
}

/**
 * Adds a node of type [T] to the split pane's list of items
 */
operator fun <T: Node> SplitPane.plus(node: T): T {
    items.add(node)
    return node
}

/**
 * Adds a table column of type [S] [T] to the table view's list of columns
 */
operator fun <S, T> TableView<S>.plus(column: TableColumn<S, T>): TableColumn<S, T> {
    columns.add(column)
    return column
}

/**
 * Adds a tab of type [T] to the tab pane's list of tabs
 */
operator fun <T: Tab> TabPane.plus(tab: T): T {
    tabs.add(tab)
    return tab
}

/**
 * Adds a node of type [T] to the toolbar's list of items
 */
operator fun <T: Node> ToolBar.plus(node: T): T {
    items.add(node)
    return node
}