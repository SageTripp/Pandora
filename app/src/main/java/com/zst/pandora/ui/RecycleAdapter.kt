package com.zst.pandora.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.zst.pandora.R
import com.zst.pandora.bean.Item
import kotlinx.android.synthetic.main.adapter_recycle.view.*
import java.util.*

/**
 * Created by zst on 2016-08-31  0031.
 * 描述:
 */
class RecycleAdapter(val ctx: Context, val items: ArrayList<Item>) : RecyclerView.Adapter<RecycleAdapter.RecycleHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleHolder {
        Fresco.initialize(ctx)
        return RecycleHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_recycle, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecycleHolder, position: Int) {
        holder.apply {
            setTitle(items[position].name)
            setImg(items[position].imageUrl)
            setDesc(items[position].desc)
        }
    }


    inner class RecycleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setTitle(title: String) {
            itemView.itemTitle.text = title
        }

        fun setImg(imageUrl: String) {
            val control = Fresco.newDraweeControllerBuilder().apply {
                autoPlayAnimations = true
                setUri(imageUrl)
            }.build()
            itemView.itemImg.controller = control
        }

        fun setDesc(desc: String) {
            itemView.itemDesc.text = desc
        }
    }
}