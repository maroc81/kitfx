package kitfx.base.builder

import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.text.TextFlow
import kotlin.math.max

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
    addRow(rowCount)
    this.rowOp()
}

/**
 * Adds a node
 */
fun GridPane.kgridpanenode(colspan: Int, rowspan: Int, rowOp: GridPane.() -> Node) {
    val rowIndex = max(0, rowCount - 1)
    val colIndex = max( 0, columnCount - 1)
    this.add(rowOp(), rowIndex, colIndex, colspan, rowspan)
}


/**
 * Property for setting hgrow for a node.
 *
 * khbox {
 *     this + ktextfield {
 *         hgrow = Priority.ALWAYS
 *     }
 * }
 */
var Node.hgrow: Priority
    get() = HBox.getHgrow(this)
    set(value) = HBox.setHgrow(this, value)

/**
 * Property for setting vgrow for a node.
 *
 * kvbox {
 *     this + ktableview {
 *         vgrow = Priority.ALWAYS
 *     }
 * }
 */
var Node.vgrow: Priority
    get() = VBox.getVgrow(this)
    set(value) = VBox.setVgrow(this, value)

/**
 * Sets vgrow to always
 */
fun Node.vgrowAlways() = VBox.setVgrow(this, Priority.ALWAYS)
