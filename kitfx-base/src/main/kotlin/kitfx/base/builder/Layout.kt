package kitfx.base.builder

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
fun ktilepane(op: TilePane.() -> Unit = {}) = TilePane().also(op)
fun ktitledpane(op: TitledPane.() -> Unit = {}) = TitledPane().also(op)
fun ktoolbar(op: ToolBar.() -> Unit = {}) = ToolBar().also(op)
fun kvbox(op: VBox.() -> Unit = {}) = VBox().also(op)

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
