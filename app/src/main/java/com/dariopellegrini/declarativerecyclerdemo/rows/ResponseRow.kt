package com.dariopellegrini.declarativerecyclerdemo.rows

import android.text.format.DateFormat
import com.dariopellegrini.declarativerecycler.BasicRow
import com.dariopellegrini.declarativerecyclerdemo.R
import kotlinx.android.synthetic.main.layout_card_cell_left.view.*
import java.util.*

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