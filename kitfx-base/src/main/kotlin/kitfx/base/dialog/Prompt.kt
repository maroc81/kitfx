package kitfx.base.dialog

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.layout.Region
import javafx.stage.Modality
import javafx.util.StringConverter
import javafx.util.converter.DefaultStringConverter
import javafx.util.converter.FloatStringConverter
import javafx.util.converter.IntegerStringConverter
import kitfx.base.builder.*


/**
 * Show a dialog to input a T value and execute the given operation.
 */
inline fun <reified T : Any> Region.promptFor(
    /** The dialog title */
    title: String = "",

    /** The label text shown next to the input text field */
    field: String = "",

    /** The initial value in the input text field */
    initialValue: T,

    /** True if the text field should be multiline (i.e. text area) */
    multiline: Boolean = false,

    converter: StringConverter<T?>,

    /** The operation to execute when a  */
    crossinline onDone: (T) -> Unit
) {
    val property = SimpleObjectProperty<T?>(initialValue)
    val inputProperty = SimpleStringProperty()

    val dialog = Dialog<T>().apply {
        prefWidth = 500.0
        this.title = title
        initModality(Modality.APPLICATION_MODAL)
        initOwner(this@promptFor.scene?.window)

        dialogPane.content = khbox {
            prefWidth = 500.0
            alignment = Pos.BASELINE_CENTER
            spacing = 10.0
            padding = Insets(10.0)

            this + klabel(field)
            if (multiline) {
                val text = this + ktextarea() {
                    hboxgrowAlways()
                    prefRowCount = 10
                    textProperty().bindBidirectional(inputProperty)
                    inputProperty.bindBidirectional(property, converter)
                }
                setOnShown { text.requestFocus() }
            } else {
                val text = this + ktextfield() {
                    hboxgrowAlways()
                    textProperty().bindBidirectional(inputProperty)
                    inputProperty.bindBidirectional(property, converter)
                }
                setOnShown { text.requestFocus() }
            }
        }

        // Add Ok and Cancel buttons
        dialogPane.buttonTypes.add(ButtonType.OK)
        dialogPane.buttonTypes.add(ButtonType.CANCEL)

        // Verify input is valid before accepting ok
        dialogPane.lookupButton(ButtonType.OK)?.let {
            addEventFilter(ActionEvent.ACTION) { event ->
                if (converter.fromString(inputProperty.value) != property.get()) {
                    event.consume()
                }
            }
        }

        // Create converter for the dialog result
        setResultConverter {
            if (it == ButtonType.OK) {
                property.get()
            } else initialValue
        }
    }

    dialog.showAndWait().ifPresent { onDone(it) }
}

/**
 * Show a dialog to input an integer value and execute the given operation.
 *
 * @param title The dialog title
 * @param field The text to show before the input text field
 * @param onDone The operation to execute when a valid integer value is input
 */
fun Region.promptForInt(title: String = "", field: String = "", initialValue: Int = 0, onDone: (Int) -> Unit) {
    promptFor(title, field, initialValue, false, IntegerStringConverter(), onDone)
}

/**
 * Show a dialog to input a floating point value and execute the given operation.
 *
 * @param title The dialog title
 * @param field The text to show before the input text field
 * @param onDone The operation to execute when a valid float value is input
 */
fun Region.promptForFloat(title: String = "", field: String = "", initialValue: Float = 0.0f,  onDone: (Float) -> Unit) {
    promptFor(title, field, initialValue, false, FloatStringConverter(), onDone)
}

/**
 * Show a dialog to input a string value and execute the given operation.
 *
 * @param title The dialog title
 * @param field The text to show before the input text field
 * @param onDone The operation to execute when a valid string is input
 */
fun Region.promptForString(title: String = "", field: String = "", initialValue: String = "", onDone: (String) -> Unit) {
    promptFor(title, field, initialValue, false, DefaultStringConverter(), onDone)
}

/**
 * Show a dialog to input a multiline string value and execute the given operation.
 *
 * @param title The dialog title
 * @param field The text to show before the input text field
 * @param onDone The operation to execute when a valid string is input
 */
fun Region.promptForMultiString(title: String = "", field: String = "", initialValue: String = "", onDone: (String) -> Unit) {
    promptFor(title, field, initialValue, true, DefaultStringConverter(), onDone)
}
