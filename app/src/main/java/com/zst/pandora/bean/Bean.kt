package com.zst.pandora.bean

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BmobFile

/**
 * Created by zst on 2016-09-18  0018.
 * 描述:
 */

class Item(var name: String = "", var sketch: String = "", var preview: BmobFile?, var desc: BmobFile?, var apk: BmobFile?, var flag: String = "DEMO") : BmobObject()

class Apk(val ctx: Context, val path: String = "") {

    private var info: PackageInfo? = null
    private var appInfo: ApplicationInfo? = null

    val appName: String by lazy { if (null == info) "" else ctx.packageManager.getApplicationLabel(appInfo).toString() }
    val packageName: String by lazy { if (null == info) "" else info?.packageName!! }
    val versionName: String by lazy { if (null == info) "" else info?.versionName!! }
    val icon: Drawable? by lazy { if (null == info) null else ctx.packageManager.getApplicationIcon(appInfo) }

    init {
        with(ctx.packageManager) {
            info = getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
            appInfo = info?.applicationInfo
            appInfo?.sourceDir = path
            appInfo?.publicSourceDir = path
            println("${appInfo?.permission}")
        }
    }
}