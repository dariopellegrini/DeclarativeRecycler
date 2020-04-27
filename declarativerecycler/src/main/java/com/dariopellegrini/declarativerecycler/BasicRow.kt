package com.dariopellegrini.declarativerecycler

import android.view.View

open class BasicRow(override val layoutID: Int,
                    override val view: View? = null,
                    override val configuration: ((View, Int) -> Unit)? = null,
                    override val onClick: ((View, Int) -> Unit)? = null,
                    override val onLongClick: ((View, Int) -> Unit)? = null): Row