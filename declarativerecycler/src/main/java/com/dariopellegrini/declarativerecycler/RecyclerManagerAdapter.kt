package com.dariopellegrini.declarativerecycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class RecyclerManagerAdapter(var rows: List<Row>): RecyclerView.Adapter<RecyclerManagerAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return Holder(view, this)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val row = rows[position]
        row.configuration?.invoke(holder.itemView, position)
    }

    override fun getItemViewType(position: Int): Int {
        return rows[position].layoutID
    }

    override fun getItemCount(): Int {
        return rows.size
    }

    class Holder(itemView: View, val adapter: RecyclerManagerAdapter): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

        init {
            this.itemView.setOnClickListener(this)
            this.itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            adapter.rows[adapterPosition].onClick?.invoke(itemView, adapterPosition)
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