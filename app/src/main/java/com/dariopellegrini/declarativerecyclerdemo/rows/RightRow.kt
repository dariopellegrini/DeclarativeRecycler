package com.dariopellegrini.declarativerecyclerdemo.rows

import android.text.format.DateFormat
import android.view.View
import com.dariopellegrini.declarativerecycler.DiffRow
import com.dariopellegrini.declarativerecyclerdemo.R
import kotlinx.android.synthetic.main.layout_card_cell_right.view.*
import java.util.*

class RightRow(val title: String, val click: (RightRow) -> Unit): DiffRow {
    override val layoutID: Int
        get() = R.layout.layout_card_cell_right

    override val configuration: ((View, Int) -> Unit)?
        get() = {
            itemView, _ ->
            itemView.rightMessageTextView.text = title
            itemView.rightDateTextView.text = "${DateFormat.format("HH:mm:ss", Date())}"

            itemView.setOnClickListener {
                click(this)
            }
        }

    override val id = title
    override val content = title
}