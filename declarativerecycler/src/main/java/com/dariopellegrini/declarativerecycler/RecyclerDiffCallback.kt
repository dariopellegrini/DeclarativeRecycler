package com.dariopellegrini.declarativerecycler

import androidx.recyclerview.widget.DiffUtil

public class RecyclerDiffCallback<T: Differentiable<T>>(val oldRows: List<T>, val newRows: List<T>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRows[oldItemPosition].isTheSame(newRows[newItemPosition])
//        return oldRows[oldItemPosition] == newRows[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldRows.size
    }

    override fun getNewListSize(): Int {
        return newRows.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRows[oldItemPosition].hasSameContent(newRows[newItemPosition])
    }
}
