package com.zst.pandora.utils.loader

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import com.yydcdut.rxmarkdown.loader.RxMDImageLoader
import com.yydcdut.rxmarkdown.loader.RxMDImageLoader.Scheme
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.*

/**
 * Created by zst on 2016-09-28  0028.
 * 描述:
 */
class OkLoader : RxMDImageLoader {
    private var mContext: Context

    constructor(context: Context) {
        mContext = context
    }

    @Nullable
    @Throws(IOException::class)
    override fun loadSync(@NonNull url: String): ByteArray {
        val bytes: ByteArray? = null
        when (Scheme.ofUri(url)) {
            Scheme.HTTP, Scheme.HTTPS -> return http(url)
            Scheme.FILE -> return local(url)
            Scheme.ASSETS -> return asserts(url)
            Scheme.DRAWABLE -> return drawable(url)
            Scheme.UNKNOWN -> return bytes!!
            else -> return bytes!!
        }
    }

    @Nullable
    @Throws(IOException::class)
    private fun http(@NonNull url: String): ByteArray {
        val out: ByteArrayOutputStream? = null
        var bytes: ByteArray? = null
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val `is` = response.body().byteStream()
        bytes = getBytes(`is`)
        closeStream(out)
        return bytes
    }

    @Nullable
    @Throws(IOException::class)
    private fun local(@NonNull url: String): ByteArray {
        val path = Scheme.FILE.crop(url)
        var inputStream: InputStream? = null
        inputStream = FileInputStream(path)
        val bytes = getBytes(inputStream)
        closeStream(inputStream)
        return bytes
    }

    @Nullable
    @Throws(IOException::class)
    private fun getBytes(@NonNull inputStream: InputStream): ByteArray {
        val out = ByteArrayOutputStream()
        var bytes: ByteArray? = null
        var i: Int = inputStream.read()
        while (i != -1) {
            out.write(i)
            i = inputStream.read()
        }
        bytes = out.toByteArray()
        closeStream(inputStream)
        return bytes
    }

    @Nullable
    @Throws(IOException::class)
    private fun asserts(@NonNull url: String): ByteArray {
        val filePath = Scheme.ASSETS.crop(url)
        val inputStream = mContext.assets.open(filePath)
        val bytes = getBytes(inputStream)
        closeStream(inputStream)
        return bytes
    }

    @Nullable
    @Throws(IOException::class)
    private fun drawable(@NonNull url: String): ByteArray {
        val drawableIdString = Scheme.DRAWABLE.crop(url)
        val drawableId = Integer.parseInt(drawableIdString)
        val inputStream = mContext.resources.openRawResource(drawableId)
        val bytes = getBytes(inputStream)
        closeStream(inputStream)
        return bytes
    }

    private fun closeStream(@Nullable closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}