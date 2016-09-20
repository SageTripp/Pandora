package com.zst.pandora

import android.support.design.widget.FloatingActionButton
import org.jetbrains.anko.backgroundColor
import org.kohsuke.github.GHContent
import java.io.File

/**
 * 是否是图片 支持格式:gif/jpg/png
 */
val File.isImg: Boolean get() = name.endsWith(".gif", true) || name.endsWith(".jpg", true) || name.endsWith(".png", true)

/**
 * 是否是APK
 */
val File.isApk: Boolean get() = name.endsWith(".apk", true)

/**
 * 是否是MD文件
 */
val File.isMarkDown: Boolean get() = name.endsWith(".md", true)

/**
 * 是否是图片 支持格式:gif/jpg/png
 */
val GHContent.isImg: Boolean get() = name.endsWith(".gif", true) || name.endsWith(".jpg", true) || name.endsWith(".png", true)

/**
 * 是否是APK
 */
val GHContent.isApk: Boolean get() = name.endsWith(".apk", true)

/**
 * 是否是MD文件
 */
val GHContent.isMarkDown: Boolean get() = name.endsWith(".md", true)

internal fun FloatingActionButton.setBackgroundColo(color: Int) {
    backgroundColor = color
}