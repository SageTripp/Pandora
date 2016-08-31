package com.zst.pandora

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import kotlinx.android.synthetic.main.fragment_recycle.*
import java.util.*

/**
 * Created by zst on 2016-08-31  0031.
 * 描述:
 */
class RecycleFragment(val title: String, val path: String) : Fragment() {
    constructor() : this("", "")

    private val items = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_recycle, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycle.apply {
            adapter = RecycleAdapter(items)
            layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MaterialViewPagerHeaderDecorator())
        }
        for (i in 0..100) {
            items.add("$title----item$i")
            recycle.adapter.notifyItemInserted(i)
        }
    }
}