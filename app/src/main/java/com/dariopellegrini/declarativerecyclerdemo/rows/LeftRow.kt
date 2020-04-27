package com.dariopellegrini.declarativerecyclerdemo.rows

import android.text.format.DateFormat
import android.view.View
import com.dariopellegrini.declarativerecycler.DiffRow
import com.dariopellegrini.declarativerecyclerdemo.R
import kotlinx.android.synthetic.main.layout_card_cell_left.view.*
import java.util.*

class LeftRow(val title: String, val click: (LeftRow) -> Unit): DiffRow {
    override val layoutID: Int
        get() = R.layout.layout_card_cell_left

    override val configuration: ((View, Int) -> Unit)?
        get() = {
            itemView, _ ->
            itemView.leftMessageTextView.text = title
            itemView.leftDateTextView.text = "${DateFormat.format("HH:mm:ss", Date())}"

            itemView.setOnClickListener {
                click(this)
            }
        }

    override val id = title
    override val content = title
}