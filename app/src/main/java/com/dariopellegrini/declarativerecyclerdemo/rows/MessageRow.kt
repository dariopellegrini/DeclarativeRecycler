package com.dariopellegrini.declarativerecyclerdemo.rows

import android.view.View
import com.dariopellegrini.declarativerecycler.Differentiable
import com.dariopellegrini.declarativerecycler.Row
import com.dariopellegrini.declarativerecyclerdemo.R
import kotlinx.android.synthetic.main.layout_card_cell_left.view.*

class MessageRow(val message: String, val click: (MessageRow) -> Unit): Row, Differentiable<MessageRow> {
    override val layoutID = R.layout.layout_card_cell_left
    override val configuration: ((View, Int) -> Unit)? = { itemView, position ->
        itemView.leftMessageTextView.text = message
        itemView.leftDateTextView.text = "${android.text.format.DateFormat.format("HH:mm:ss", java.util.Date())}"

        itemView.setOnClickListener {
            click(this)
        }
    }

    override fun isTheSame(new: MessageRow): Boolean {
        return message == new.message
    }

    override fun hasSameContent(new: MessageRow): Boolean {
        return message == new.message
    }

}