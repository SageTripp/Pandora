package com.zst.pandora

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import com.github.florent37.materialviewpager.header.HeaderDesign
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp

class MainActivity : AppCompatActivity() {

    private val fragements = arrayListOf(RecycleFragment("DEMO", ""),
            RecycleFragment("工具", ""),
            RecycleFragment("项目", ""),
            RecycleFragment("资源", ""))
    private val headerPics = arrayListOf(R.drawable.demo, R.drawable.utils, R.drawable.project, R.drawable.source)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = materialViewPager.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        materialViewPager.viewPager.apply {
            adapter = getAdapter(supportFragmentManager)
            offscreenPageLimit = adapter.count
        }
        materialViewPager.pagerTitleStrip.apply {
            textSize = sp(18)
            setIndicatorColorResource(R.color.blue)
            setViewPager(materialViewPager.viewPager)
        }

        materialViewPager.setMaterialViewPagerListener {

            val bitmap = BitmapFactory.decodeResource(resources, headerPics[it])
            val vibrantColor = Palette.from(bitmap).generate().getVibrantColor(Color.CYAN)
            val darkMutedColor = Palette.from(bitmap).generate().getDarkMutedColor(Color.TRANSPARENT)
            materialViewPager.pagerTitleStrip.apply {
                setTextColor(vibrantColor)
                indicatorColor = vibrantColor
                indicatorHeight = dip(12)
            }
            return@setMaterialViewPagerListener HeaderDesign.fromColorAndDrawable(darkMutedColor, BitmapDrawable(resources, bitmap))
//            return@setMaterialViewPagerListener when (it) {
//                0 -> HeaderDesign.fromColorResAndUrl(R.color.blue, "https://drivenlocal.com/wp-content/uploads/2015/10/Material-design.jpg")
//                1 -> HeaderDesign.fromColorResAndUrl(R.color.lime, "http://img2.imgtn.bdimg.com/it/u=2599205032,2477786774&fm=21&gp=0.jpg")
//                2 -> HeaderDesign.fromColorResAndUrl(R.color.colorPrimary, "http://p3.image.hiapk.com/uploads/allimg/141124/7730-141124100258.jpg")
//                3 -> HeaderDesign.fromColorResAndUrl(R.color.colorAccent, "http://i7.download.fd.pchome.net/t_960x600/g1/M00/0C/0F/ooYBAFRh0nWId0RAAAVrsGjvnfsAACEuQHpin0ABWvI634.jpg")
//                else -> null
//            }
        }
    }

    private fun getAdapter(fm: FragmentManager): FragmentStatePagerAdapter {
        return object : FragmentStatePagerAdapter(fm) {
            override fun getItem(position: Int): Fragment {
                return fragements[position]
            }

            override fun getCount(): Int {
                return fragements.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return fragements[position].title
            }
        }
    }

    private fun getPalette(@DrawableRes res: Int): Palette {
        val bitmap = BitmapFactory.decodeResource(resources, res)
        return Palette.from(bitmap).generate()
    }
}
