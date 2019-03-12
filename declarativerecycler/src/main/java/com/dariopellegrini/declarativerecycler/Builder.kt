package com.dariopellegrini.declarativerecycler

import android.view.View

fun buildRow(layoutID: Int, closure: RowBuilder.() -> Unit): Row {
    val rowBuilder = RowBuilder(layoutID)
    rowBuilder.closure()
    return rowBuilder.buildRow()
}

class RowBuilder(val layoutID: Int) {
    var configuration: ((View, Int) -> Unit)? = null
    var onClick: ((View, Int) -> Unit)? = null
    var onLongClick: ((View, Int) -> Unit)? = null

    fun buildRow(): Row {
        return object : Row {
            override val layoutID: Int
                get() = this@RowBuilder.layoutID
            override val configuration: ((View, Int) -> Unit)?
                get() = this@RowBuilder.configuration
            override val onClick: ((View, Int) -> Unit)?
                get() = this@RowBuilder.onClick
            override val onLongClick: ((View, Int) -> Unit)?
                get() = this@RowBuilder.onLongClick
        }
    }
}