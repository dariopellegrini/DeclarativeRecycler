package com.dariopellegrini.declarativerecycler

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.w3c.dom.Text
import java.util.*

fun buildRow(layoutID: Int, closure: RowBuilder.() -> Unit): Row {
    val rowBuilder = RowBuilder(layoutID)
    rowBuilder.closure()
    return rowBuilder.buildRow()
}

class RowBuilder(val layoutID: Int) {
    var view: View? = null
    var configuration: ((View, Int) -> Unit)? = null
    var onClick: ((View, Int) -> Unit)? = null
    var onLongClick: ((View, Int) -> Unit)? = null

    fun buildRow(): Row {
        return object : Row {
            override val layoutID: Int
                get() = this@RowBuilder.layoutID
            override val view: View?
                get() = this@RowBuilder.view
            override val configuration: ((View, Int) -> Unit)?
                get() = this@RowBuilder.configuration
            override val onClick: ((View, Int) -> Unit)?
                get() = this@RowBuilder.onClick
            override val onLongClick: ((View, Int) -> Unit)?
                get() = this@RowBuilder.onLongClick
        }
    }
}

fun row(layoutID: Int, closure: (View, Int) -> Unit): Row {
    val rowBuilder = RowBuilder(layoutID)
    rowBuilder.configuration = closure
    return rowBuilder.buildRow()
}

fun rowFromView(view: View): Row {
    return object: Row {
        override val layoutID: Int
            get() = -1
        override val view: View?
            get() = view
    }
}

fun androidx.recyclerview.widget.RecyclerView.build(layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context),
                                                    closure: DeclarativeBuilder.() -> Unit): RecyclerManager {
    val declarativeBuilder = DeclarativeBuilder(this, layoutManager)
    declarativeBuilder.closure()
    return declarativeBuilder.recyclerManager
}

class DeclarativeBuilder(recyclerView: androidx.recyclerview.widget.RecyclerView,
                         layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager): ViewAdding {
    override val context = recyclerView.context
    val recyclerManager = RecyclerManager(recyclerView, layoutManager)

    override fun appendView(view: View) {
        this.recyclerManager.append(rowFromView(view), true, false)
    }
}

class ViewGroupBuilder(val viewGroup: ViewGroup): ViewAdding {
    override val context: Context
        get() = viewGroup.context

    override fun appendView(view: View) {
        viewGroup.addView(view)
    }
}

infix fun DeclarativeBuilder.add(row: Row) {
    this.recyclerManager.append(row, true, true)
}

infix fun DeclarativeBuilder.add(rows: List<Row>) {
    this.recyclerManager.append(rows, true, true)
}


infix fun DeclarativeBuilder.addView(view: View) {
    this.appendView(view)
}

fun ViewAdding.customView(closure: () -> View) {
    this.appendView(closure())
}

interface ViewAdding {
    val context: Context
    fun appendView(view: View)
}

fun ViewAdding.textView(text: String, closure: TextView.() -> Unit = {}): TextView {
    val textView = TextView(context)
    textView.text = text
    textView.closure()
    this.appendView(textView)
    return textView
}

fun ViewAdding.button(action: () -> Unit, closure: Button.() -> Unit = {}): Button {
    val button = Button(context)
    button.setOnClickListener {
        action()
    }
    button.closure()
    this.appendView(button)
    return button
}

fun ViewAdding.view(closure: View.() -> Unit): View {
    val view = View(context)
    view.closure()
    this.appendView(view)
    return view
}

fun ViewAdding.verticalLayout(closure: ViewGroupBuilder.() -> Unit): LinearLayout {
    val verticalLayout = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
    val builder = ViewGroupBuilder(verticalLayout)
    builder.closure()
    this.appendView(verticalLayout)
    return verticalLayout
}

fun ViewAdding.horizontalLayout(closure: ViewGroupBuilder.() -> Unit): LinearLayout {
    val horizontalLayout = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
    val builder = ViewGroupBuilder(horizontalLayout)
    builder.closure()
    this.appendView(horizontalLayout)
    return horizontalLayout
}

fun ViewAdding.zLayout(closure: ViewGroupBuilder.() -> Unit): FrameLayout {
    val frameLayout = FrameLayout(context)
    val builder = ViewGroupBuilder(frameLayout)
    builder.closure()
    this.appendView(frameLayout)
    return frameLayout
}

fun ViewAdding.scrollView(closure: ViewGroupBuilder.() -> Unit): FrameLayout {
    val scrollView =  ScrollView(context)
    val verticalLayout = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
    scrollView.addView(verticalLayout)
    val builder = ViewGroupBuilder(verticalLayout)
    builder.closure()
    this.appendView(scrollView)
    return scrollView
}

fun ViewAdding.horizontalScrollView(closure: ViewGroupBuilder.() -> Unit): FrameLayout {
    val scrollView =  HorizontalScrollView(context)
    val horizontalLayout = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
    scrollView.addView(horizontalLayout)
    val builder = ViewGroupBuilder(horizontalLayout)
    builder.closure()
    this.appendView(scrollView)
    return scrollView
}

fun ViewAdding.recyclerView(layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context),
                            closure: DeclarativeBuilder.() -> Unit): androidx.recyclerview.widget.RecyclerView {
    val recyclerView = androidx.recyclerview.widget.RecyclerView(context)
    recyclerView.build(layoutManager, closure)
    this.appendView(recyclerView)
    return recyclerView
}

fun body(inside: ViewGroup, closure: ViewGroupBuilder.() -> View) {
    val builder = ViewGroupBuilder(inside)
    builder.closure()
}

fun <T: View> T.margin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null): T {
    val params = this.layoutParams
    when(params) {
        is LinearLayout.LayoutParams -> params.setMargins(
                left ?: params.leftMargin,
                top ?: params.topMargin,
                right ?: params.rightMargin,
                bottom ?: params.bottomMargin)
        is FrameLayout.LayoutParams -> params.setMargins(
                left ?: params.leftMargin,
                top ?: params.topMargin,
                right ?: params.rightMargin,
                bottom ?: params.bottomMargin)
        is androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams -> params.setMargins(
                left ?: params.leftMargin,
                top ?: params.topMargin,
                right ?: params.rightMargin,
                bottom ?: params.bottomMargin)
        is RelativeLayout.LayoutParams -> params.setMargins(
                left ?: params.leftMargin,
                top ?: params.topMargin,
                right ?: params.rightMargin,
                bottom ?: params.bottomMargin)
    }
    if (params != null) {
        this.layoutParams = params
    }
    return this
}

enum class Dimensions(val value: Int) {
    MatchParent(ViewGroup.LayoutParams.MATCH_PARENT),
    WrapContent(ViewGroup.LayoutParams.WRAP_CONTENT)
}

val matchParent = ViewGroup.LayoutParams.MATCH_PARENT
val wrapContent = ViewGroup.LayoutParams.WRAP_CONTENT

fun <T: View>T.width(value: Int): T {
    val params = this.layoutParams
    if (params != null) {
        params.width = value
        this.layoutParams = params
    }
    return this
}

fun <T: View>T.height(value: Int): T {
    val params = this.layoutParams
    if (params != null) {
        params.height = value
        this.layoutParams = params
    }
    return this
}
