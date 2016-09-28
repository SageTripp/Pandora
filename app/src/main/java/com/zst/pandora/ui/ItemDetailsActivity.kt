package com.zst.pandora.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.DownloadFileListener
import cn.bmob.v3.listener.QueryListener
import com.morgoo.droidplugin.pm.PluginManager
import com.yydcdut.rxmarkdown.RxMDConfiguration
import com.yydcdut.rxmarkdown.RxMarkdown
import com.yydcdut.rxmarkdown.factory.TextFactory
import com.zst.pandora.R
import com.zst.pandora.bean.Apk
import com.zst.pandora.bean.Item
import com.zst.pandora.utils.FILE_PATH
import com.zst.pandora.utils.loader.OkLoader
import kotlinx.android.synthetic.main.activity_item_details.*
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx
import org.jetbrains.anko.onClick
import org.jetbrains.anko.withAlpha
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by zst on 2016-09-23  0023.
 * 描述:
 */
class ItemDetailsActivity : AppCompatActivity() {

    private var isDownloaded = false
    private lateinit var item: Item
    private lateinit var apk: Apk
    private lateinit var apkPath: String

    var rxMDConfiguration = RxMDConfiguration.Builder(this)
            .setBlockQuotesColor(Color.GREEN.withAlpha(100))
            .setHeader1RelativeSize(2.2f)
            .setHeader2RelativeSize(2.0f)
            .setHeader3RelativeSize(1.8f)
            .setHeader4RelativeSize(1.6f)
            .setHeader5RelativeSize(1.4f)
            .setHeader6RelativeSize(1.2f)
            .setHorizontalRulesColor(0xff99cc00.toInt())
            .setInlineCodeBgColor(0xffff4444.toInt())
            .setCodeBgColor(0x33999999.toInt())
            .setTodoColor(0xffaa66cc.toInt())
            .setTodoDoneColor(0xffff8800.toInt())
            .setUnOrderListColor(0xff00ddff.toInt())
            .setRxMDImageLoader(OkLoader(ctx))
            .setLinkColor(Color.BLUE)
            .setLinkUnderline(false)
            .setOnLinkClickCallback({ view, link ->
                Toast.makeText(view.context, link, Toast.LENGTH_SHORT).show()
            }).build()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        item = intent.getSerializableExtra("item") as Item
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        supportActionBar?.apply {
            title = item.name
            setDisplayHomeAsUpEnabled(true)
            setDefaultDisplayHomeAsUpEnabled(true)
        }
        rxDesc.movementMethod = LinkMovementMethod.getInstance()
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
        val file = item.desc
        if (null == file) {
            desc.setMarkDownText("这里没有描述,还是直接运行程序看结果吧,哇咔咔咔咔咔~~~~~~")
            setMarkDown("这里没有描述,还是直接运行程序看结果吧,哇咔咔咔咔咔~~~~~~")
            return true
        } else {
            val saveFile = File("$FILE_PATH${File.separator}${item.name}", file.filename)
            val isExist = saveFile.exists()
            if (isExist) {
                desc.loadMarkdownFromFile(saveFile)
                setMarkDown(readFileSdcardFile(saveFile.path))
            }
            return isExist
        }
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
                desc.setMarkDownText("## 文档正在加载中" +
                        "" +
                        "进度:$progress    速度:$newWorkSpeed")
                setMarkDown("## 文档正在加载中" +
                        "" +
                        "进度:$progress    速度:$newWorkSpeed")
            }

            override fun done(savePath: String?, e: BmobException?) {
                println("savePath = [$savePath], e = [$e]")
                desc.loadMarkdownFromFile(File(savePath))
                setMarkDown(readFileSdcardFile(savePath!!))
            }

        })
    }

    fun loadApk() {
        download.isEnabled = false
        val file = item.apk
        val saveFile = File("$FILE_PATH${File.separator}${item.name}", file?.filename)
        file?.download(saveFile, object : DownloadFileListener() {
            override fun onProgress(progress: Int, newWorkSpeed: Long) {
                download.setProgress(progress.toFloat())
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

    fun setMarkDown(content: String) {
        RxMarkdown.with(content, ctx)
                .config(rxMDConfiguration)
                .factory(TextFactory.create())
                .intoObservable()
                .subscribe { text ->
                    rxDesc.setText(text, TextView.BufferType.SPANNABLE)
                    rxDesc.append("------")
                }
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
        val p = PluginManager.getInstance().getInstalledPackages(0).filter { it.packageName.equals(apk.packageName) }
        if (p.size == 0) {
            val i = PluginManager.getInstance().installPackage(apkPath, 0)
            Snackbar.make(desc, "$i", Snackbar.LENGTH_LONG).show()
        }
        val intent = act.packageManager.getLaunchIntentForPackage(apk.packageName)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    //读SD中的文件
    @Throws(IOException::class)
    fun readFileSdcardFile(fileName: String): String {
        var res = ""
        try {
            val fin = FileInputStream(fileName)

            val length = fin.available()

            val buffer = ByteArray(length)
            fin.read(buffer)

            res = kotlin.text.String(buffer, Charset.forName("UTF-8"))

            fin.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return res
    }

}