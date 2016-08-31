package com.zst.pandora.utils

import android.app.Application
import android.content.Context

import com.morgoo.droidplugin.PluginHelper

/**
 * Created by zst on 2016-08-31  0031.
 * 描述:
 */

class PandoraApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //这里必须在super.onCreate方法之后，顺序不能变
        PluginHelper.getInstance().applicationOnCreate(baseContext)
    }

    override fun attachBaseContext(base: Context) {
        PluginHelper.getInstance().applicationAttachBaseContext(base)
        super.attachBaseContext(base)
    }
}
