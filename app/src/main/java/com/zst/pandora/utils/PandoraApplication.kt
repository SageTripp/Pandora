package com.zst.pandora.utils

import android.app.Application
import android.content.Context
import cn.bmob.v3.Bmob
import com.facebook.drawee.backends.pipeline.Fresco

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
        Bmob.initialize(this, "96b2bc4a1b94ded32b8c1dc568d430cc")
        Fresco.initialize(this)
    }

    override fun attachBaseContext(base: Context) {
        PluginHelper.getInstance().applicationAttachBaseContext(base)
        super.attachBaseContext(base)
    }
}
