package com.zst.pandora.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import com.zst.pandora.R
import com.zst.pandora.bean.Item
import com.zst.pandora.utils.PandoraGitUtils
import kotlinx.android.synthetic.main.fragment_recycle.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

/**
 * Created by zst on 2016-08-31  0031.
 * 描述:
 */
class RecycleFragment(val title: String, val path: String) : Fragment() {
    constructor() : this("", "")

    private val items = ArrayList<Item>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_recycle, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycle.apply {
            adapter = RecycleAdapter(activity, items)
            layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MaterialViewPagerHeaderDecorator())
        }

//        MaterialViewPagerHelper.registerScrollView(activity, noData, null)
//        doAsync {
//            try {
//                val github = GitHub.connectUsingPassword("SageTripp", "zhang0720")
//                val repo = github.getRepository("SageTripp/PandoraApks")
//                val files = repo.getDirectoryContent("source/$path")
//                if (files.size <= 0)
//                    flagNoData()
//                files.forEach { file ->
//                    println("文件:${file.downloadUrl}")
//                    items.add(Item("${file.downloadUrl}","","",""))
//                    uiThread {
//                        recycle.adapter.notifyItemInserted(0)
//                    }
//                }
//            } catch(e: IOException) {
//                println(e.message)
//            }
//        }

        doAsync {
            items.addAll(PandoraGitUtils.loadFiles("$path"))
            if (items.size > 0)
                uiThread {
                    recycle.adapter.notifyDataSetChanged()
                }
            else
                flagNoData()
        }

    }

    private fun flagNoData() {
        recycle.visibility = View.GONE
        noData.visibility = View.VISIBLE
    }
}