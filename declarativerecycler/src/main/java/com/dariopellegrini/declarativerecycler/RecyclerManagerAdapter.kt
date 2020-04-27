package com.dariopellegrini.declarativerecycler

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class RecyclerManagerAdapter(var rows: List<Row>): androidx.recyclerview.widget.RecyclerView.Adapter<RecyclerManagerAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return if (viewType < 0) {
            val view = rows[(viewType + 1) * -1].view!!
            Holder(view, this)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            Holder(view, this)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val row = rows[position]
        row.configuration?.invoke(holder.itemView, position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (rows[position].view != null) {
            -1 - position
        } else {
            rows[position].layoutID
        }
    }

    override fun getItemCount(): Int {
        return rows.size
    }

    class Holder(itemView: View, val adapter: RecyclerManagerAdapter): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        init {
//            this.itemView.setOnClickListener(this)
//            this.itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            if (adapterPosition > 0 && adapterPosition < adapter.rows.size) {
                adapter.rows[adapterPosition].onClick?.invoke(itemView, adapterPosition)
            }
        }

        override fun onLongClick(v: View): Boolean {
            val onLongClick = adapter.rows[adapterPosition].onLongClick
            if (onLongClick != null) {
                onLongClick(itemView, adapterPosition)
                return true
            }
            return false
        }
    }
}