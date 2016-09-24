package com.zst.pandora.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.DownloadFileListener
import cn.bmob.v3.listener.QueryListener
import com.morgoo.droidplugin.pm.PluginManager
import com.zst.pandora.R
import com.zst.pandora.bean.Apk
import com.zst.pandora.bean.Item
import com.zst.pandora.utils.FILE_PATH
import kotlinx.android.synthetic.main.activity_item_details.*
import org.jetbrains.anko.act
import org.jetbrains.anko.onClick
import java.io.File

/**
 * Created by zst on 2016-09-23  0023.
 * 描述:
 */
class ItemDetailsActivity : AppCompatActivity() {

    private var isDownloaded = false
    private lateinit var item: Item
    private lateinit var apk: Apk
    private lateinit var apkPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        setSupportActionBar(toolbar)
        toolbar.title = "标题"
        toolbar.setNavigationOnClickListener {
            finish()
        }

        supportActionBar?.apply {
            title = "标题"
            setIcon(R.mipmap.ic_launcher)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
            setDisplayUseLogoEnabled(true)
        }
        item = intent.getSerializableExtra("item") as Item
        download.apply {
            hideProgressOnComplete(true)
            onClick {
                if (isDownloaded) {
                    Snackbar.make(appbar, "开始运行", Snackbar.LENGTH_LONG).show()
                    playApk()
                } else {
                    Snackbar.make(appbar, "开始下载", Snackbar.LENGTH_LONG).show()
                    resetIcon()
                    showProgress(true)
                    loadApk()
                }

            }
        }
        updateItem()
    }

    /**
     * 更新Item信息,主要是取得desc和apk
     */
    fun updateItem() {
        val query = BmobQuery<Item>()
        query.getObject(item.objectId, object : QueryListener<Item>() {
            override fun done(returnItem: Item?, e: BmobException?) {
                if (null != returnItem)
                    item = returnItem
                if (!checkDescExist())
                    loadDesc()
                checkApkExist()
            }

        })

    }

    fun checkDescExist(): Boolean {
        val saveFile = File("$FILE_PATH${File.separator}${item.name}", item.desc?.filename)
        val isExist = saveFile.exists()
        if (isExist)
            desc.loadMDFile(saveFile)
        return isExist
    }

    fun checkApkExist() {
        val saveFile = File("$FILE_PATH${File.separator}${item.name}", item.apk?.filename)
        isDownloaded = saveFile.exists()
        if (isDownloaded) {
            apkPath = saveFile.path
            flagDownloaded()
        }
    }

    fun loadDesc() {
        val file = item.desc
        val saveFile = File("$FILE_PATH${File.separator}${item.name}", file?.filename)
        file?.download(saveFile, object : DownloadFileListener() {
            override fun onProgress(progress: Int?, newWorkSpeed: Long) {
                println("progress = [$progress], newWorkSpeed = [$newWorkSpeed]")
                desc.setMDText("## 文档正在加载中" +
                        "" +
                        "进度:$progress    速度:$newWorkSpeed")
            }

            override fun done(savePath: String?, e: BmobException?) {
                println("savePath = [$savePath], e = [$e]")
                desc.loadMDFilePath(savePath)
            }

        })
    }

    fun loadApk() {
        download.isEnabled = false
        val file = item.apk
        val saveFile = File("$FILE_PATH${File.separator}${item.name}", file?.filename)
        file?.download(saveFile, object : DownloadFileListener() {
            override fun onProgress(progress: Int?, newWorkSpeed: Long) {
                download.setProgress(progress?.toFloat()!!)
                println("progress = [$progress], newWorkSpeed = [$newWorkSpeed]")
            }

            override fun done(savePath: String?, e: BmobException?) {
                apkPath = savePath!!
                flagDownloaded()
                download.isEnabled = true
                println("savePath = [$savePath], e = [$e]")
            }

        })
    }

    fun flagDownloaded() {
        download.apply {
            showProgress(false)
            setColor(Color.GREEN)
            onProgressCompleted()
        }
        isDownloaded = true
        apk = Apk(act, apkPath)
        appName.text = apk.appName
        appVersion.text = apk.versionName
        appPackageName.text = apk.packageName
        appIcon.setImageDrawable(apk.icon)
    }

    fun playApk() {
        PluginManager.getInstance().installPackage(apkPath, 0)
        val intent = act.packageManager.getLaunchIntentForPackage(apk.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}