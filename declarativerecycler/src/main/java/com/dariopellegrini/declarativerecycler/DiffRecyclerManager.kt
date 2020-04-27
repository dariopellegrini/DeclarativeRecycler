package com.dariopellegrini.declarativerecycler

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log

class DiffRecyclerManager<T>(val recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) where T : Row, T : Differetiable<T> {
    val rows = mutableListOf<T>()
    private val adapter: RecyclerManagerAdapter

    init {
        adapter = RecyclerManagerAdapter(rows)
        recyclerView.layoutManager = layoutManager
        Log.i("RecyclerAdapter", "Adapter assigned")
        recyclerView.adapter = adapter
    }

    // Add
    fun add(row: T, position: Int, scroll: Boolean = false) {
        reload(rows.toMutableList().apply { add(position, row) })
        if (scroll) {
            recyclerView.scrollToPosition(position)
        }
    }

    fun push(row: T, scroll: Boolean = false) {
        add(row, 0, scroll)
    }

    fun append(row: T, scroll: Boolean = false) {
        add(row, rowsSize, scroll)
    }

    // Add lists
    fun add(rowsList: List<T>, position: Int, animated: Boolean = false, scroll: Boolean = false) {
        reload(rows.toMutableList().apply {
            addAll(position, rowsList)
        })
        if (scroll) {
            recyclerView.scrollToPosition(position)
        }
    }

    fun push(rowsList: List<T>, scroll: Boolean = false) {
        add(rowsList, 0, scroll)
    }

    fun append(rowsList: List<T>, scroll: Boolean = false) {
        add(rowsList, rowsSize, scroll)
    }

    // Remove
    fun remove(row: T, scroll: Boolean = false) {
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

    fun remove(position: Int, scroll: Boolean = false) {
        if (rows.size > position) {
            reload(rows.toMutableList().apply {
                removeAt(position)
            })
            if (scroll) {
                recyclerView.scrollToPosition(position)
            }
        }
    }

    fun pop(scroll: Boolean = false) {
        remove(0, scroll)
    }

    fun removeLast(scroll: Boolean = false) {
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
    fun clear(animated: Boolean = false) {
        reload(listOf())
    }

    // Reload
    fun reload(newRows: List<T>) {
        val diffResult = DiffUtil.calculateDiff(RecyclerDiffCallback(rows, newRows))
        rows.clear()
        rows.addAll(newRows)
        diffResult.dispatchUpdatesTo(adapter)

        rows.clear()
        rows.addAll(newRows)
    }

    // Properties
    val rowsSize: Int
        get() = rows.size

    val lastPosition: Int
        get() = rowsSize - 1
}