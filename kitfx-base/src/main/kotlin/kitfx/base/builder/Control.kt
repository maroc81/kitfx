package kitfx.base.builder

import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.media.MediaView
import javafx.scene.web.WebView

/**
 * Builders for JavaFX controls
 */

fun kaccordion(op: Accordion.() -> Unit = {}) = Accordion().also(op)
fun kbutton(text: String = "", op: Button.() -> Unit = {}) = Button(text).also(op)
fun kbuttonbar(buttonOrder: String? = null, op: ButtonBar.() -> Unit = {}) = ButtonBar(buttonOrder).also(op)
fun kcheckbox(text: String = "", op: CheckBox.() -> Unit = {}) = CheckBox(text).also(op)
fun <T> kchoicebox(op: ChoiceBox<T>.() -> Unit = {}) = ChoiceBox<T>().also(op)
fun kcolorpicker(op: ColorPicker.() -> Unit = {}) = ColorPicker().also(op)
fun <T> kcombobox(op: ComboBox<T>.() -> Unit = {}) = ComboBox<T>().also(op)
fun kcontextmenu(op: ContextMenu.() -> Unit = {}) = ContextMenu().also(op)
fun kdatepicker(op: DatePicker.() -> Unit = {}) = DatePicker().also(op)
fun khyperlink(text: String = "", op: Hyperlink.() -> Unit = {}) = Hyperlink(text).also(op)
fun kimageview(op: ImageView.() -> Unit = {}) = ImageView().also(op)
fun klabel(text: String = "", graphic: Node? = null, op: Label.() -> Unit = {}) = Label(text, graphic).also(op)
fun <T> klistview(op: ListView<T>.() -> Unit = {}) = ListView<T>().also(op)
fun kmediaview(op: MediaView.() -> Unit = {}) = MediaView().also(op)
fun kmenu(text: String = "", graphic: Node? = null, op: Menu.() -> Unit = {}) = Menu(text, graphic).also(op)
fun kmenubar(op: MenuBar.() -> Unit = {}) = MenuBar().also(op)
fun kmenubutton(op: MenuButton.() -> Unit = {}) = MenuButton().also(op)
fun kmenuitem(text: String = "", op: MenuItem.() -> Unit = {}) = MenuItem(text).also(op)
fun kpagination(op: Pagination.() -> Unit = {}) = Pagination().also(op)
fun kpasswordfield(op: PasswordField.() -> Unit = {}) = PasswordField().also(op)
fun kprogressbar(op: ProgressBar.() -> Unit = {}) = ProgressBar().also(op)
fun kprogressindicator(op: ProgressIndicator.() -> Unit = {}) = ProgressIndicator().also(op)
fun kradiobutton(op: RadioButton.() -> Unit = {}) = RadioButton().also(op)
fun kscrollbar(op: ScrollBar.() -> Unit = {}) = ScrollBar().also(op)
fun kseparator(op: Separator.() -> Unit = {}) = Separator().also(op)
fun kslider(op: Slider.() -> Unit = {}) = Slider().also(op)
fun <T> kspinner(op: Spinner<T>.() -> Unit = {}) = Spinner<T>().also(op)
fun ksplitmenubutton(op: SplitMenuButton.() -> Unit = {}) = SplitMenuButton().also(op)
fun ksepratormenuitem(op: SeparatorMenuItem.() -> Unit = {}) = SeparatorMenuItem().also(op)
fun ktab(text: String = "", op: Tab.() -> Unit = {}) = Tab(text).also(op)
fun <S,T> ktablecolumn(text: String = "", op: TableColumn<S,T>.() -> Unit = {}) = TableColumn<S,T>(text).also(op)
fun <S> ktableview(op: TableView<S>.() -> Unit = {}) = TableView<S>().also(op)
fun ktextarea(op: TextArea.() -> Unit = {}) = TextArea().also(op)
fun ktextfield(text: String? = null, op: TextField.() -> Unit = {}) = TextField(text).also(op)
fun ktogglebutton(op: ToggleButton.() -> Unit = {}) = ToggleButton().also(op)
fun <S,T> ktreetablecolumn(op: TreeTableColumn<S,T>.() -> Unit = {}) = TreeTableColumn<S,T>().also(op)
fun <T> ktreeview(op: TreeView<T>.() -> Unit = {}) = TreeView<T>().also(op)
fun kwebview(op: WebView.() -> Unit = {}) = WebView().also(op)