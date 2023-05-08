# KitFX
Simple kotlin library for working with JavaFX
## About
KitFX is yet another library to help simplify writing JavaFX applications using kotlin. Several libraries already provide similar functionality including:

 * [KotlinFX](https://github.com/eugenkiss/kotlinfx)
 * [KopperFX](https://github.com/gonozalviii/KopperFX)
 * [TornadoFX](https://github.com/edvin/tornadofx)

How is KitFX different?
 * Not (yet) abandoned
 * Simple/thin extensions to JavaFX
 * Designed to supplement JavaFX, not replace it

TornadoFX is the most popular library for kotlin and JavaFX applications. However, it's essentially an entirely new framework since it not only provides extensions to JavaFX, but also provides customizations that alter the language beyond the original design.  Additionally, the code backing some of the features that allow users to write simple application code is anything but simple. Since TornadoFX can't provide everything an application will need, eventually, a complex TornadoFX application will need to refer back to JavaFX documentation for customizations leaving the user to learn both TornadoFX and JavaFX. Finally, the project has been abandoned for years and is far behind the latest OpenJFX release.

## Usage
This project is in early stages and is not yet published to any maven or gradle repository.

For now, in order to use the library do the following:
1. Clone this respository
2. Edit your gradle.settings or gradle.settings.kt to include the cloned project
```kotlin
include ":kitfx-base"
project(":kitfx-base").projectDir = new File("/path/to/kitfx/kitfx-base") 
```
3. Edit your build.gradle or build.gradle.kts to include the project
```kotlin
implementation(project(":kitfx-base"))
```
4. Import the appropriate module as needed
```kotlin
import kitfx.base.binding.*
import kitfx.base.builder.*
import kitfx.base.collection.*
import kitfx.base.property.*
```

*NOTE*: Since KitFX is in the early stages of development, package paths, class names, etc, are subject to change.

## Builders

KitFX provides "thin" functions to get close to the builders provided by TornadoFX, without complex backing code and multiple overloaded versions of the same builder. Each builder is simply the JavaFX class name, all lowercase, prefixed with k.

### Kotlin
With plain kotlin and JavaFX you can instantiate each object and assign to containers. You can also instantiate each object and use "apply" to get the typical builder style pattern. 
```kotlin
// Java converted to Kotlin
val borderPane = BorderPane()
val toolbar = ToolBar()
val statusbar = HBox()
val appContent: Node = VBox()

val button = Button()
toolbar.items.add(button)

borderPane.top = toolbar
borderPane.center = appContent
borderPane.bottom = statusbar

// Builder type pattern using pure kotlin language 
val root = BorderPane().apply {
    top = ToolBar().apply {
        items += Button().apply {
        }
    }
    center = VBox().apply {
    }
    bottom = HBox().apply {
    }
}
```

### KitFX
KitFX allows you to eliminate "apply" and the parenthesis (if there are no arguments). It also provides operators so you can use "this +" before the builder to add to the parent container. No need to remember if you need to add to children, items, tabs, etc. You can also save a reference to the instances since the "+" operator returns the object.

```kotlin
var btnToSave: Button
val root = kborderpane {
    top = ktoolbar {
        btnToSave = this + kbutton {
        }
    }
    center = kvbox {
    }
    bottom = khbox {
    }
}
```
All the KitFX builders are one line functions with no overloads that require no object context (i.e. not an extension function). The only arguments are those available from the original construction. For example, the builder function for a label is simply:

```kotlin
fun klabel(text: String = "", graphic: Node? = null, op: Label.() -> Unit = {}) = Label(text, graphic).also(op)
```

So you may ask why even use KitFX if you only eliminate () and apply? For builders, the point of KitFX is to provide simple functions to give you a consistent clean syntax but without lots of backing complexity.  And if/when you need to get rid of this library, it's a simple search and replace.

### TornadoFX
For reference, the TornadoFX code looks like this:
```kotlin
var btnToSave: Button
val root = borderpane {
    top {
        toolbar {
        }
    }
    center {
        vbox {
            btnToSave = button {
            }
        }
    }
    bottom {
        hbox {
        }
    }
}
```

For this particular case, KitFX only requires "this +" to achieve similar simplicity. However, if you dive into the TornadoFX code for "center", you'll have to follow several functions, internal properties, new classes, etc, all so you can use "center {" instead of "center =".

### Complex KitFX Example

```kotlin

class MainWindow : BorderPane() {
    private lateinit var treeViewForModel: TreeView<TreeItem<ModelObject>>
    private lateinit var tableViewForObject: TableView<ModelSubObject>

    init {
        prefHeight = 1024.0
        prefWidth = 1280.0

        // Top is a toolbar with buttons
        top = ktoolbar {
            // TODO: Add items to tool bar
        }
        
        // Center is the main content
        center = kvbox {

            // Split pane with tree view on left and tab view on right
            this + ksplitpane {
                // Convenience function from KitFX for setting vgrow
                vgrow = Priority.ALWAYS

                // VBox on left side with toolbar above a tree view 
                this + kvbox {

                    // Toolbar above query tree
                    this + ktoolbar {
                        this + kbutton {
                            tooltip = Tooltip("Reload")
                            graphic = FontIcon("fas-sync-alt")
                            // In TornadoFX this would just be "action { }"
                            setOnAction { reload() }
                        }
                    }

                    // Create tree view and assign to variable
                    treeViewForModel= this + ktreeview {
                        vgrow = Priority.ALWAYS
                        isShowRoot = false
                        root = TreeView<TreeItem<ModelObject>>()

                        // In TornadoFX this is simply "onChange { }"
                        // A similar convenience function may be added later to KitFX 
                        selectionModel.selectedItemProperty().addListener { _, _, new ->
                            tableViewForObject.items = new.subObjectList
                        }
                    }
                }

                // Tab pane on right
                this + ktabpane {
                    // Add tab and set content from a custom component
                    this + ktab("Custom") {
                        content = CustomComponent()
                    }
                    // Add tab and create table view in tab
                    this + ktab("TableView") {
                        content = vbox {
                            tableViewForObject = this + ktableview<ModelSubObject> {
                                // Items are set by tree view selection

                                // Columns are always manually constructed in KitFX which is a bit more
                                // verbose but is consistent and less confusing than the many 
                                // "column()" builders in TornadoFX
                                this + ktablecolumn<ModelSubObject, Int>("ID") {
                                    setCellValueFactory { it.value.idProperty() }
                                }

                                // You could use the PropertyValueFactory but it turns out to be more
                                // verbose than above, plus you lose static checks 
                                this + ktablecolumn<ModelSubObject, Int>("ID") {
                                    cellValueFactory = PropertyValueFactory<ModelSubObject, Int>("idProperty")
                                }

                                // OpenJFX 19 introduced a well overdue function "flatMap" to bind/select sub properties
                                // In TornadoFX you used select: {it.value.subSubObjectProperty().select(SubSubObject::nameProperty)}
                                this + ktablecolumn<ModelSubObject, String>("SubSubObjectName") {
                                    // If subSubObjectPropertyChanges, the name shown in the table column will
                                    // also update
                                    setCellValueFactory { it.value.subSubObjectProperty().flatMap(SubSubObject::nameProperty) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

```

## Properties

### Java
In plain JavaFX, properties are typically in the form below.
```java
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
    public int getId() {return id.get();}
    public void setId(int id) {this.id.set(id);}
    public IntegerProperty idProperty() {return id ;}
```

### Kotlin
The Kotlin version is basically the same, except get and set could be put on a single line.  
```kotlin
    private val _id: ObjectProperty<Int> = SimpleObjectProperty(this, "id", 0)
    var id: Int; get() = _id.get(); set(value) = _id.set(value)
    fun idProperty() = _id
```

### KitFX
In KitFX fashion, the kotlin version is simplified slightly
```kotlin
    private val _id = objectProperty<Int>(this, "id", 0)
    var id: Int; get() = _id.get(); set(value) = _id.set(value)
    fun idProperty() = _id
```

### TornadoFX 
TornadoFX only requires 2 lines of code but a property delegate must be instantiated for every property which adds additional memory overhead.

```kotlin
    var id: Integer by property(0)
    fun idProperty() = getProperty(Obj::id)
```

## Bindings


## Collections
