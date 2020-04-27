package com.dariopellegrini.declarativerecycler

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.text.method.TextKeyListener.clear



open class RecyclerManager(val recyclerView: androidx.recyclerview.widget.RecyclerView, layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager) {
    open val rows = mutableListOf<Row>()
    private val adapter: RecyclerManagerAdapter

    init {
        adapter = RecyclerManagerAdapter(rows)
        recyclerView.layoutManager = layoutManager
        Log.i("RecyclerAdapter", "Adapter assigned")
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

    // Add lists
    fun add(rowsList: List<Row>, position: Int, animated: Boolean = false, scroll: Boolean = false) {
        rows.addAll(position, rowsList)
        if (animated) {
            adapter.notifyItemRangeInserted(position, position + rowsList.size)
            if (scroll) {
                recyclerView.scrollToPosition(position)
            }
        }
    }

    fun push(rowsList: List<Row>, animated: Boolean = false, scroll: Boolean = false) {
        add(rowsList, 0, animated, scroll)
    }

    fun append(rowsList: List<Row>, animated: Boolean = false, scroll: Boolean = false) {
        add(rowsList, rowsSize, animated, scroll)
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


    // Remove list
    fun remove(closure: (Row) -> Boolean, animated: Boolean = false, scroll: Boolean = false) {
        if (animated) {
            var scrollPosition = -1
            rows.removeAll {
                row ->
                if (scroll && scrollPosition == -1) {
                    scrollPosition = 0
                    recyclerView.scrollToPosition(scrollPosition)
                }
                if (closure(row) && rows.indexOf(row) >= 0) {
                    adapter.notifyItemRemoved(rows.indexOf(row))
                }
                closure(row)
            }
        } else {
            rows.removeAll(closure)
        }
    }

    fun remove(rowsList: List<Row>, animated: Boolean = false, scroll: Boolean = false) {
        if (animated) {
            var scrollPosition = -1
            rowsList.forEach {
                row ->
                if (animated) {
                    adapter.notifyItemRemoved(rows.indexOf(row))
                    if (scroll && scrollPosition == -1) {
                        scrollPosition = rows.indexOf(row)
                        recyclerView.scrollToPosition(scrollPosition)
                    }
                }
                rows.remove(row)
            }
        } else {
            rows.removeAll(rowsList)
        }
    }

    fun remove(from: Int, to: Int, animated: Boolean = false, scroll: Boolean = false) {
        if (rows.size > from && rows.size > to) {
            rows.subList(from, to).clear() // from is inclusive and to is exclusive
            if (animated) {
                adapter.notifyItemRangeRemoved(from, to - from)
                if (scroll) {
                    recyclerView.scrollToPosition(from)
                }
            }
        } else {
            Log.e("DeclarativeRecycler", "Check range size and row size. Out of bounds")
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