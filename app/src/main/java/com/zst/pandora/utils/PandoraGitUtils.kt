//package com.zst.pandora.utils
//
//import com.zst.pandora.bean.Item
//import com.zst.pandora.isApk
//import com.zst.pandora.isImg
//import com.zst.pandora.isMarkDown
//import org.kohsuke.github.GHRepository
//import org.kohsuke.github.GitHub
//import java.io.File
//import java.util.*
//
///**
// * Created by zst on 2016-09-18  0018.
// * 描述:
// */
//object PandoraGitUtils {
//
//    private const val USER = "SageTripp"
//    private const val PASS = "zhang0720"
//    private val github: GitHub by lazy { GitHub.connectUsingPassword(USER, PASS) }
//    private val repo: GHRepository by lazy { github.getRepository("${USER}/PandoraApks") }
//    private const val SAVE_PATH = ""
//
//    fun loadFiles(path: String): ArrayList<Item> {
//        val list = ArrayList<Item>()
//        repo.getDirectoryContent("source/$path").forEach { dir ->
//            val item = loadFileFromDisk("$SAVE_PATH${File.separator}$path${File.separator}${dir.name}")
//            if (item.name.isEmpty()) {
//                if (dir.isDirectory) {
//                    item.name = dir.name
//                    repo.getDirectoryContent(dir.path).forEach { file ->
//                        if (file.isImg) {
//                            item.preview = file.path
//                        } else if (file.isApk) {
//                            item.apk = file.path
//                        } else if (file.isMarkDown) {
//                            item.sketch = file.path
//                        }
//                    }
//                }
//            }
//            if (item.name.isNotEmpty())
//                list.add(item)
//        }
//        return list
//    }
//
//    private fun loadFilesFromDisk(path: String): ArrayList<Item> {
//        val items = ArrayList<Item>()
//        val file = File("$SAVE_PATH${File.separator}$path")
//        if (file.exists()) {
//            file.listFiles().forEach { dir ->
//                val item = loadFileFromDisk(dir.path)
//                if (item.name.isNotEmpty())
//                    items.add(item)
//            }
//        }
//        return items
//    }
//
//    private fun loadFileFromDisk(path: String): Item {
//        val file = File(path)
//        if (file.exists() && file.isDirectory) {
//            val item = Item(name = "${file.name}")
//            file.listFiles().forEach { file ->
//                if (file.isImg) {
//                    item.preview = file.path
//                } else if (file.isApk) {
//                    item.apk = file.path
//                } else if (file.isMarkDown) {
//                    item.sketch = file.path
//                }
//            }
//            return item
//        } else
//            return Item()
//    }
//}