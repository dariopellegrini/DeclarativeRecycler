package com.dariopellegrini.declarativerecycler

import androidx.recyclerview.widget.DiffUtil
import android.util.Log
import androidx.recyclerview.widget.RecyclerView

class DiffRecyclerManager<T>(val recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) where T : Row, T : Differentiable<T> {
    val rows = mutableListOf<T>()
    var scrollEnabled = false

    private val adapter: RecyclerManagerAdapter

    init {
        adapter = RecyclerManagerAdapter(rows)
        recyclerView.layoutManager = layoutManager
        Log.i("RecyclerAdapter", "Adapter assigned")
        recyclerView.adapter = adapter
    }

    // Add
    fun add(row: T, position: Int, scroll: Boolean = scrollEnabled) {
        reload(rows.toMutableList().apply { add(position, row) })
        if (scroll) {
            recyclerView.scrollToPosition(position)
        }
    }

    fun push(row: T, scroll: Boolean = scrollEnabled) {
        add(row, 0, scroll)
    }

    fun append(row: T, scroll: Boolean = scrollEnabled) {
        add(row, rowsSize, scroll)
    }

    // Add lists
    fun add(rowsList: List<T>, position: Int, scroll: Boolean = scrollEnabled) {
        reload(rows.toMutableList().apply {
            addAll(position, rowsList)
        })
        if (scroll) {
            recyclerView.scrollToPosition(position)
        }
    }

    fun push(rowsList: List<T>, scroll: Boolean = scrollEnabled) {
        add(rowsList, 0, scroll)
    }

    fun append(rowsList: List<T>, scroll: Boolean = scrollEnabled) {
        add(rowsList, rowsSize, scroll)
    }

    // Remove
    fun remove(row: T, scroll: Boolean = scrollEnabled) {
        val position = rows.indexOf(row)
        if (position > -1) {
            reload(rows.toMutableList().apply {
                removeAt(position)
            })
            if (scroll) {
                recyclerView.scrollToPosition(position)
            }
        }
    }

    fun remove(position: Int, scroll: Boolean = scrollEnabled) {
        if (rows.size > position) {
            reload(rows.toMutableList().apply {
                removeAt(position)
            })
            if (scroll) {
                recyclerView.scrollToPosition(position)
            }
        }
    }

    fun pop(scroll: Boolean = scrollEnabled) {
        remove(0, scroll)
    }

    fun removeLast(scroll: Boolean = scrollEnabled) {
        if (lastPosition > -1) {
            remove(lastPosition, scroll)
        }
    }


    // Remove list
    fun remove(closure: (T) -> Boolean) {
        reload(rows.filter(closure))
    }

    fun remove(rowsList: List<T>) {
        reload(rows.filter { !rowsList.contains(it) })
    }

    fun remove(from: Int, to: Int) {
        if (rows.size > from && rows.size > to) {
            reload(rows.subList(from, to))
        } else {
            Log.e("DeclarativeRecycler", "Check range size and row size. Out of bounds")
        }
    }

    // Clear
    fun clear() {
        reload(listOf())
    }

    // Reload
    fun reload(newRows: List<T>) {
        val diffResult = DiffUtil.calculateDiff(RecyclerDiffCallback(rows, newRows))
        rows.clear()
        rows.addAll(newRows)
        diffResult.dispatchUpdatesTo(adapter)
    }

    // Properties
    val rowsSize: Int
        get() = rows.size

    val lastPosition: Int
        get() = rowsSize - 1
}