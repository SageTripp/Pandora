package com.zst.pandora

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.adapter_recycle.view.*
import java.util.*

/**
 * Created by zst on 2016-08-31  0031.
 * 描述:
 */
class RecycleAdapter(val items: ArrayList<String>) : RecyclerView.Adapter<RecycleAdapter.RecycleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleHolder {
        return RecycleHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_recycle, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecycleHolder, position: Int) {
        holder.setTitle(items[position])
    }


    inner class RecycleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setTitle(title: String) {
            itemView.itemTitle.text = title
        }
    }
}