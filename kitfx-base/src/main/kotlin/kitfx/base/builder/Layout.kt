package kitfx.base.builder

import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.text.TextFlow

/**
 * Builders for JavaFX layouts
 */

fun kanchorpane(op: AnchorPane.() -> Unit = {}) = AnchorPane().also(op)
fun kborderpane(op: BorderPane.() -> Unit = {}) = BorderPane().also(op)
fun kdialogpane(op: DialogPane.() -> Unit = {}) = DialogPane().also(op)
fun kflowpane(op: FlowPane.() -> Unit = {}) = FlowPane().also(op)
fun kgridpane(op: GridPane.() -> Unit = {}) = GridPane().also(op)
fun khbox(op: HBox.() -> Unit = {}) = HBox().also(op)
fun kpane(op: Pane.() -> Unit = {}) = Pane().also(op)
fun kscrollpane(op: ScrollPane.() -> Unit = {}) = ScrollPane().also(op)
fun ksplitpane(op: SplitPane.() -> Unit = {}) = SplitPane().also(op)
fun kstackpane(op: StackPane.() -> Unit = {}) = StackPane().also(op)
fun ktabpane(op: TabPane.() -> Unit = {}) = TabPane().also(op)
fun ktextflow(op: TextFlow.() -> Unit = {}) = TextFlow().also(op)
fun ktilepane( op: TilePane.() -> Unit = {}) = TilePane().also(op)
fun ktitledpane(title: String = "", op: TitledPane.() -> Unit = {}) = TitledPane(title, null).also(op)
fun ktoolbar(op: ToolBar.() -> Unit = {}) = ToolBar().also(op)
fun kvbox(op: VBox.() -> Unit = {}) = VBox().also(op)

/**
 * Start a grid pane row by adding a new row to the grid pane
 */
fun GridPane.kgridpanerow(rowOp: GridPane.() -> Unit) {
    // The only way to add a row without adding a node is to add default/empty constraints
    rowConstraints.add(RowConstraints())
    this.rowOp()
}

/**
 * Adds a node to the current grid pane row and column
 */
fun <T: Node> GridPane.kgridpanenode(column: Int, colspan: Int = 1, rowspan: Int = 1, rowOp: GridPane.() -> T): T {
    val node = rowOp()
    val rowIndex = (rowCount - 1).coerceAtLeast(0)
    this.add(node, column, rowIndex, colspan, rowspan)
    return node
}

fun Node.gridpaneconstraints(
    columnIndex: Int,
    rowIndex: Int,
    columnspan: Int = 1,
    rowspan: Int = 1,
    halignment: HPos = HPos.LEFT,
    valignment: VPos = VPos.BASELINE,
    hgrow: Priority = Priority.SOMETIMES,
    vgrow: Priority = Priority.SOMETIMES,
    margin: Insets = Insets(0.0)
) {
    GridPane.setConstraints(this, columnIndex, rowIndex, columnspan, rowspan, halignment, valignment, hgrow, vgrow, margin)
}

/**
 * Adds a pane to an HBox set to always grow
 */
fun HBox.kspacer(priority: Priority = Priority.ALWAYS, op: Pane.() -> Unit = {}) = kpane {
    HBox.setHgrow(this, priority)
    op()
}

/**
 * Adds a pane to an HBox set to always grow
 */
fun VBox.kspacer(priority: Priority = Priority.ALWAYS, op: Pane.() -> Unit = {}) = kpane {
    VBox.setVgrow(this, priority)
    op()
}

/**
 * Property for setting HBox hgrow for a node.
 *
 * khbox {
 *     this + ktextfield {
 *         hgrow = Priority.ALWAYS
 *     }
 * }
 */
var Node.hboxgrow: Priority
    get() = HBox.getHgrow(this)
    set(value) = HBox.setHgrow(this, value)

/**
 * Property for setting VBox vgrow for a node.
 *
 * kvbox {
 *     this + ktableview {
 *         vgrow = Priority.ALWAYS
 *     }
 * }
 */
var Node.vboxgrow: Priority
    get() = VBox.getVgrow(this)
    set(value) = VBox.setVgrow(this, value)

/**
 * Convenience function to set HBox hgrow to always for a node
 */
fun Node.hboxgrowAlways() = HBox.setHgrow(this, Priority.ALWAYS)

/**
 * Convenience function to set VBox vgrow to always for a node
 */
fun Node.vboxgrowAlways() = VBox.setVgrow(this, Priority.ALWAYS)
