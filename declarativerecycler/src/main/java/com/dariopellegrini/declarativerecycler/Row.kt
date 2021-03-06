package com.dariopellegrini.declarativerecycler

import android.view.View

interface Row {
    val layoutID: Int
    val view: View?
        get() = null
    val configuration: ((View, Int) -> Unit)?
        get() = null
    val onClick: ((View, Int) -> Unit)?
        get() = null
    val onLongClick: ((View, Int) -> Unit)?
        get() = null
}