# DeclarativeRecycler

[![](https://jitpack.io/v/dariopellegrini/DeclarativeRecycler.svg)](https://jitpack.io/#dariopellegrini/DeclarativeRecycler)

A declarative way to use recycler views.

## Example

Download or clone the repo and try the demo app.

## Installation

Edit your build.gradle file
``` groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Then add as dependency to your app/build.gradle
``` groovy
dependencies {
    ...
    implementation 'com.github.dariopellegrini:DeclarativeRecycler:v0.6.2'
}
```

## Usage

With this library you can populate your recycler view in a very declarative way, adding or removing rows and configuring action.

First of all instantiate a RecyclerManager

``` kotlin
recyclerManager = RecyclerManager(recyclerView, layoutManager)
```

Then it's possible to add a row very easily (Kotlin extension are used here)

``` kotlin
recyclerManager.push(BasicRow(
                        layoutID = R.layout.layout_card_cell_left, // The layout resource of the row
                        configuration = {
                            itemView, position ->
                            
                            // Configuration:
                            // itemView is the row view containing layout elements
                            // position is the row position
                            
                            itemView.leftMessageTextView.text = message
                            itemView.leftDateTextView.text = "${DateFormat.format("HH:mm:ss", Date())}"
                        },
                        onClick = { // Closure executed on row click
                            itemView, position ->
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        },
                        onLongClick = { // Closure executed on row long click
                            itemView, position ->
                            recyclerManager.remove(position, true, false)
                        }), animated = true, scroll = true) // Animations
```

Each row needs at least a layout resource id. Configuration onClick and onLongClick closures are optional.
In order to add rows to recycler manager the following methods are available both for a row and list of rows:
- push;
- append;
- add at position.

In order to remove rows to recycler manager the following methods are available both for a row and list of rows:
- pop;
- removeLast;
- remove Row;
- remove at position;
- remove with closure;
- remove rows;
- clear.

After any modification to the rows list it is necessary to call
``` kotlin
recyclerManager.reload()
```

## Animations
For each modification method, 2 flag are available:
- animation: if true the modification comes with an animation (default to false);
- scroll: if true and if animation is true, after the rows modification the recycler view will scroll to that added or removed row position (default to false).

If animation is true it's not necessary to call the reload method.

## Row implementation
Above example uses BasicRow class to configure a row. It comes with the library and implements Row interface, which can of course be implemented by a custom class.

``` kotlin
// UserRow implements Row interface and must conform to it.
class UserRow(val message: String, val clicked: () -> Unit, val longClicked: (Int) -> Unit): Row {

    // Mandatory
    override val layoutID: Int
        get() = R.layout.layout_card_cell_right

    // Optional
    override val configuration: ((View, Int) -> Unit)?
        get() = {
            itemView, _ ->
            itemView.rightMessageTextView.text = message
            itemView.rightDateTextView.text = "${DateFormat.format("HH:mm:ss", Date())}"
        }

    // Optional
    override val onClick: ((View, Int) -> Unit)?
        get() = {
            _, _ ->
            clicked()
        }

    // Optional
    override val onLongClick: ((View, Int) -> Unit)?
        get() = {
            _, position ->
            longClicked(position)
        }

}

// Than can be added to the recyclerManager
recyclerManager.push(UserRow(
                             message = message,
                             clicked = {
                                 Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                             },
                             longClicked = { position ->
                                 recyclerManager.remove(position, true, false)
                             })
                        , true, true)
```

## BasicRow subclassing
An alternative to Row interface implementation is a BasicRow subclass

``` kotlin
// ResponseRow inherits from BasicRow, which implements Row.
// Because ResponseRow inherits from BasicRow, all its attributes must be passed to BasicRow contructor.
class ResponseRow(val message: String, onClick: () -> Unit, onLongClick: (Int) -> Unit):
        BasicRow(
                layoutID = R.layout.layout_card_cell_left,
                configuration = {
                    itemView, position ->
                    itemView.leftMessageTextView.text = message
                    itemView.leftDateTextView.text = "${DateFormat.format("HH:mm:ss", Date())}"
                },
                onClick = {
                    _, _ ->
                    onClick()
                },
                onLongClick = {
                    _, position ->
                    onLongClick(position)
                }
        )
```

## TODO

- Tests.

## Author

Dario Pellegrini, pellegrini.dario.1303@gmail.com
