package com.dariopellegrini.declarativerecyclerdemo.rows

import android.graphics.Color
import android.text.format.DateFormat
import android.view.View
import com.dariopellegrini.declarativerecycler.Row
import com.dariopellegrini.declarativerecyclerdemo.R
import kotlinx.android.synthetic.main.layout_card_cell_right.view.*
import java.util.*

// UserRow implements Row interface and must conform to it.
class ActionRow(val message: String): Row {

    var itemView: View? = null

    // Mandatory
    override val layoutID: Int
        get() = R.layout.layout_card_cell_right

    // Optional
    override val configuration: ((View, Int) -> Unit)?
        get() = {
            itemView, _ ->
            this.itemView = itemView
            itemView.rightMessageTextView.text = message
            itemView.rightDateTextView.text = "${DateFormat.format("HH:mm:ss", Date())}"
        }
}