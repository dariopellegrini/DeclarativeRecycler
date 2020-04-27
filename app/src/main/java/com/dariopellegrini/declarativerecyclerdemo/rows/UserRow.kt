package com.dariopellegrini.declarativerecyclerdemo.rows

import android.graphics.Color
import android.text.format.DateFormat
import android.view.View
import com.dariopellegrini.declarativerecycler.Row
import com.dariopellegrini.declarativerecyclerdemo.R
import kotlinx.android.synthetic.main.layout_card_cell_right.view.*
import java.util.*

// UserRow implements Row interface and must conform to it.
class UserRow(val message: String, val clicked: () -> Boolean, val longClicked: () -> Unit): Row {

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
            itemView.setBackgroundColor( if (selected) Color.parseColor("#FF4081") else Color.TRANSPARENT)
        }

    // Optional
    override val onClick: ((View, Int) -> Unit)?
        get() = {
            itemView, position ->
            if (clicked()) {
                selected = !selected
                configuration?.invoke(itemView, position)
            }
        }

    // Optional
    override val onLongClick: ((View, Int) -> Unit)?
        get() = {
            itemView, position ->
            selected = !selected
//            configuration?.invoke(itemView, position)

            longClicked()
        }

    var selected: Boolean = false
        get
        set(value) {
            view?.setBackgroundColor( if (value) Color.parseColor("#FF4081") else Color.TRANSPARENT)
            field = value
        }


}