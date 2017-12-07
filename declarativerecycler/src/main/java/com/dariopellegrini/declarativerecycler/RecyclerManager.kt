package com.dariopellegrini.declarativerecycler

import android.support.v7.widget.RecyclerView

class RecyclerManager(val recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
    val rows = mutableListOf<Row>()
    val adapter: RecyclerManagerAdapter

    init {
        adapter = RecyclerManagerAdapter(rows)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    // Add
    fun add(row: Row, position: Int, animated: Boolean = false, scroll: Boolean = false) {
        rows.add(position, row)
        if (animated) {
            adapter.notifyItemInserted(position)
            if (scroll) {
                recyclerView.scrollToPosition(position)
            }
        }
    }

    fun push(row: Row, animated: Boolean = false, scroll: Boolean = false) {
        add(row, 0, animated, scroll)
    }

    fun append(row: Row, animated: Boolean = false, scroll: Boolean = false) {
        add(row, rowsSize, animated, scroll)
    }

    // Remove
    fun remove(row: Row, animated: Boolean = false, scroll: Boolean = false) {
        if (animated) {
            val position = rows.indexOf(row)
            if (position > -1) {
                rows.removeAt(position)
                adapter.notifyItemRemoved(position)
                if (scroll) {
                    recyclerView.scrollToPosition(position)
                }
            }
        } else {
            rows.remove(row)
        }
    }

    fun remove(position: Int, animated: Boolean = false, scroll: Boolean = false) {
        if (rows.size > position) {
            rows.removeAt(position)
            if (animated) {
                adapter.notifyItemRemoved(position)
                if (scroll) {
                    recyclerView.scrollToPosition(position)
                }
            }
        }
    }

    fun pop(animated: Boolean = false, scroll: Boolean = false) {
        remove(0, animated, scroll)
    }

    fun removeLast(animated: Boolean = false, scroll: Boolean = false) {
        if (lastPosition > -1) {
            remove(lastPosition, animated, scroll)
        }
    }

    // Clear
    fun clear(animated: Boolean = false) {
        if (rowsSize > 0) {
            if (animated) {
                val size = rowsSize
                rows.clear()
                adapter.notifyItemRangeRemoved(0, size)
            } else {
                rows.clear()
            }
        }
    }

    // Reload
    fun reload() {
        adapter.notifyDataSetChanged()
    }

    // Properties
    val rowsSize: Int
        get() = rows.size

    val lastPosition: Int
        get() = rowsSize - 1
}