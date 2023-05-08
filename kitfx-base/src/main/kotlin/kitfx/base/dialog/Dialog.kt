package kitfx.base.dialog

import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.stage.FileChooser
import javafx.stage.Window
import java.io.File

/**
 * Constructs a JavaFX alert
 *
 * Taken from tornadofx
 */
inline fun alert(
    type: Alert.AlertType,
    header: String,
    content: String? = null,
    vararg buttons: ButtonType,
    owner: Window? = null,
    title: String? = null,
    graphic: Node? = null,
    actionFn: Alert.(ButtonType) -> Unit = {}
): Alert {

    val alert = Alert(type, content ?: "", *buttons)
    title?.let { alert.title = it }
    graphic?.let { alert.graphic = it }
    alert.headerText = header
    owner?.also { alert.initOwner(it) }
    val buttonClicked = alert.showAndWait()
    if (buttonClicked.isPresent) {
        alert.actionFn(buttonClicked.get())
    }
    return alert
}

/**
 * Convenience function to construct an Alert with type set to warning
 */
inline fun warning(
    header: String,
    content: String? = null,
    vararg buttons: ButtonType,
    owner: Window? = null,
    title: String? = null,
    graphic: Node? = null,
    actionFn: Alert.(ButtonType) -> Unit = {}
) = alert(Alert.AlertType.WARNING, header, content, *buttons, owner = owner, title = title, graphic = graphic, actionFn = actionFn)

/**
 * Convenience function to construct an Alert with type set to error
 */
inline fun error(
    header: String,
    content: String? = null,
    vararg buttons: ButtonType,
    owner: Window? = null, title:
    String? = null, graphic:
    Node? = null,
    actionFn: Alert.(ButtonType) -> Unit = {}
) = alert(Alert.AlertType.ERROR, header, content, *buttons, owner = owner, title = title, graphic = graphic, actionFn = actionFn)

/**
 * Convenience function to construct an Alert with type set to information
 */
inline fun information(
    header: String,
    content: String? = null,
    vararg buttons: ButtonType,
    owner: Window? = null,
    title: String? = null,
    graphic: Node? = null,
    actionFn: Alert.(ButtonType) -> Unit = {}
) = alert(Alert.AlertType.INFORMATION, header, content, *buttons, owner = owner, title = title, graphic = graphic, actionFn = actionFn)

/**
 * Convenience function to construct an Alert with type set to confirmation
 */
inline fun confirmation(
    header: String,
    content: String? = null,
    vararg buttons: ButtonType,
    owner: Window? = null,
    title: String? = null,
    graphic: Node? = null,
    actionFn: Alert.(ButtonType) -> Unit = {}
) = alert(Alert.AlertType.CONFIRMATION, header, content, *buttons, owner = owner, title = title, graphic = graphic, actionFn = actionFn)

/**
 * Convenience function to construct an Alert with type set to confirmation and optional
 * button types for confirm and cancel which default to standard buttons
 */
inline fun confirmationOkCancel(
    header: String,
    content: String = "",
    confirmButton: ButtonType = ButtonType.OK,
    cancelButton: ButtonType = ButtonType.CANCEL,
    owner: Window? = null,
    title: String? = null,
    graphic: Node? = null,
    actionFn: () -> Unit
) {
    alert(Alert.AlertType.CONFIRMATION, header, content, confirmButton, cancelButton, owner = owner, title = title, graphic = graphic) {
        if (it == confirmButton) actionFn()
    }
}

enum class FileChooserMode { None, Single, Multi, Save }

/**
 * Convenience function to construct a FileChooser
 */
fun chooseFile(title: String? = null,
               filters: Array<out FileChooser.ExtensionFilter>,
               initialDirectory: File? = null,
               initialFileName: String? = null,
               mode: FileChooserMode = FileChooserMode.Single,
               owner: Window? = null,
               op: FileChooser.() -> Unit = {}
): List<File> {
    val chooser = FileChooser()
    if (title != null) chooser.title = title
    chooser.extensionFilters.addAll(filters)
    chooser.initialDirectory = initialDirectory
    chooser.initialFileName = initialFileName
    op(chooser)
    return when (mode) {
        FileChooserMode.Single -> {
            val result = chooser.showOpenDialog(owner)
            if (result == null) emptyList() else listOf(result)
        }
        FileChooserMode.Multi -> chooser.showOpenMultipleDialog(owner) ?: emptyList()
        FileChooserMode.Save -> {
            val result = chooser.showSaveDialog(owner)
            if (result == null) emptyList() else listOf(result)
        }
        else -> emptyList()
    }
}