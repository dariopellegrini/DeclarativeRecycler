package com.dariopellegrini.declarativerecycler

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dariopellegrini.declarativerecycler.decorator.HeaderItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DiffRecyclerManager<T>(val recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) where T : Row, T : Differentiable<T> {
    val rows = mutableListOf<T>()
    var shouldScroll = false

    private val adapter: RecyclerManagerAdapter

    init {
        adapter = RecyclerManagerAdapter(rows)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    // Add
    fun add(row: T, position: Int, scroll: Boolean = shouldScroll) {
        reload(rows.toMutableList().apply { add(position, row) })
        if (scroll) {
            recyclerView.scrollToPosition(position)
        }
    }

    fun push(row: T, scroll: Boolean = shouldScroll) {
        add(row, 0, scroll)
    }

    fun append(row: T, scroll: Boolean = shouldScroll) {
        add(row, rowsSize, scroll)
    }

    // Add lists
    fun add(rowsList: List<T>, position: Int, scroll: Boolean = shouldScroll) {
        reload(rows.toMutableList().apply {
            addAll(position, rowsList)
        })
        if (scroll) {
            recyclerView.scrollToPosition(position)
        }
    }

    fun push(rowsList: List<T>, scroll: Boolean = shouldScroll) {
        add(rowsList, 0, scroll)
    }

    fun append(rowsList: List<T>, scroll: Boolean = shouldScroll) {
        add(rowsList, rowsSize, scroll)
    }

    // Remove
    fun remove(row: T, scroll: Boolean = shouldScroll) {
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

    fun remove(position: Int, scroll: Boolean = shouldScroll) {
        if (rows.size > position) {
            reload(rows.toMutableList().apply {
                removeAt(position)
            })
            if (scroll) {
                recyclerView.scrollToPosition(position)
            }
        }
    }

    fun pop(scroll: Boolean = shouldScroll) {
        remove(0, scroll)
    }

    fun removeLast(scroll: Boolean = shouldScroll) {
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
    fun reload(newRows: List<T>, scroll: ScrollType? = null) {
        val scrollIfNecessary = scroll != null && newRows.size > rows.size
        val diffResult = DiffUtil.calculateDiff(RecyclerDiffCallback(rows, newRows))
        rows.clear()
        rows.addAll(newRows)

        try {
            diffResult.dispatchUpdatesTo(adapter)
            if (scrollIfNecessary) {
                when (scroll) {
                    ScrollType.SMOOTH -> recyclerView.smoothScrollToPosition(0)
                    ScrollType.INSTANT -> recyclerView.scrollToPosition(0)
                }
            }
        } catch (e: Exception) {
            Log.e("DiffRecyclerManager", "$e")
        }
    }

    suspend fun suspendReload(newRows: List<T>) {
        val diffResult = withContext(Dispatchers.Default) {
            DiffUtil.calculateDiff(RecyclerDiffCallback(rows, newRows))
        }
        rows.clear()
        rows.addAll(newRows)

        try {
            diffResult.dispatchUpdatesTo(adapter)
        } catch (e: Exception) {
            Log.e("DiffRecyclerManager", "$e")
        }
    }

    fun reload(vararg newRows: T) {
        reload(newRows.toList())
    }

    // Properties
    val rowsSize: Int
        get() = rows.size

    val lastPosition: Int
        get() = rowsSize - 1

    // Sticky Header
    fun configureStickyHeaders() {
        val decorator = HeaderItemDecoration(recyclerView) {
            rows.size > it && (rows[it] as? StickyHeader)?.isStickyHeader == true
        }
        recyclerView.addItemDecoration(decorator)
    }

    // Decoration
    fun addDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        recyclerView.addItemDecoration(itemDecoration)
    }
}