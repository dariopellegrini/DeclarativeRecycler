package com.dariopellegrini.declarativerecyclerdemo.rows

import com.dariopellegrini.declarativerecycler.Row
import com.dariopellegrini.declarativerecyclerdemo.R

class ProgressRow(): Row {
    override val layoutID: Int
        get() = R.layout.layout_progress_cell
}