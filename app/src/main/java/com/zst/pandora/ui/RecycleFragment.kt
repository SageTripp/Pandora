package com.zst.pandora.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator
import com.zst.pandora.R
import com.zst.pandora.bean.Item
import kotlinx.android.synthetic.main.fragment_recycle.*
import java.util.*

/**
 * Created by zst on 2016-08-31  0031.
 * 描述:
 */
class RecycleFragment(val title: String = "", val path: String = "", val desc: String = "这个人很懒\r\n\t描述都不留下") : Fragment() {

    private val items = ArrayList<Item>()
    private var isFirst = false//初次加载

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_recycle, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true
        recycle.apply {
            adapter = RecycleAdapter(items)
            layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MaterialViewPagerHeaderDecorator())
        }

//        val uri = Uri.parse("res:///" + R.drawable.load_pic)
        val request = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.load_pic).build()

        val control = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setImageRequest(request)
                .build()
        control.animatable?.start()
        loadPic.controller = control

    }

    /**
     * 加载数据,默认只在第一次调用时有效
     * @param force 是否强制加载,默认不强制加载
     */
    @Synchronized
    fun loadData(force: Boolean = false) {
        if (isFirst || force) {
            isFirst = false
            flagLoadData()
//            doAsync {
//                items.clear()
//                items.addAll(PandoraGitUtils.loadFiles("$path"))
//                uiThread {
//                    if (items.size > 0) {
//                        flagDataLoaded()
//                        recycle.adapter.notifyDataSetChanged()
//                    } else
//                        flagNoData()
//                }
//            }
            BmobQuery<Item>()
                    .addQueryKeys("objectId,name,preview,sketch,flag")
                    .addWhereEqualTo("flag", path)
                    .order("createdAt")
                    .findObjects(object : FindListener<Item>() {
                        override fun done(returnItems: List<Item>, e: BmobException?) {
                            items.clear()
                            items.addAll(returnItems)
                            if (items.size > 0) {
                                flagDataLoaded()
                                recycle.adapter.notifyDataSetChanged()
                            } else
                                flagNoData()
                        }
                    })

        }
    }

    private fun flagNoData() {
        recycle.visibility = View.GONE
        loadData.visibility = View.GONE
        noData.visibility = View.VISIBLE
    }

    private fun flagLoadData() {
        recycle.visibility = View.GONE
        noData.visibility = View.GONE
        loadData.visibility = View.VISIBLE
    }

    private fun flagDataLoaded() {
        noData.visibility = View.GONE
        loadData.visibility = View.GONE
        recycle.visibility = View.VISIBLE
    }
}