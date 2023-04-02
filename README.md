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

TornadoFX is the most popular library for kotlin and JavaFX applications. However, it's essentially an entirely new framework since it not only provides extensions to JavaFX, but also provides customizations that alter the language beyond the original design.  Additionally, the code backing some of the features that allow users to write simple application code is anything but simple. Also, TornadoFX can't provide everything an application will need. Eventually, a complex TornadoFX application will need to refer back to JavaFX documentation for customizations leaving the user to learn both TornadoFX and JavaFX. Finally, the project has been abandoned for years and is far behind the latest OpenJFX release.

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
For reference, the TornadoFX code would look like this:
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
    private val _id: ObjectProperty<String> = SimpleObjectProperty(this, "id", 0)
    var id: String; get() = _id.get(); set(value) = _id.set(value)
    fun idProperty() = _id
```

### KitFX
In KitFX fashion, the kotlin version is simplified slightly
```kotlin
    private val _id = objectProperty<Int>(this, "id", 0)
    var id: String; get() = _id.get(); set(value) = _id.set(value)
    fun idProperty() = _id
```
