package com.zst.pandora.ui

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import com.github.florent37.materialviewpager.header.HeaderDesign
import com.zst.pandora.R
import com.zst.pandora.setBackgroundColo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_logo.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.onClick
import org.jetbrains.anko.singleLine
import org.jetbrains.anko.sp

class MainActivity : AppCompatActivity() {

    private val fragements = arrayListOf(RecycleFragment("DEMO", "DEMO"),
            RecycleFragment("工具", "utils"),
            RecycleFragment("项目", "projects"),
            RecycleFragment("资源", "resource"))
    private var currentFragmentIndex = 0
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
            indicatorHeight = dip(5)
            underlineHeight = dip(1)
            underlineColor = Color.WHITE
            title = "Pandora"
        }

        materialViewPager.setMaterialViewPagerListener {
            currentFragmentIndex = it
            val bitmap = BitmapFactory.decodeResource(resources, headerPics[it])
            val lightVibrantColor = Palette.from(bitmap).generate().getLightVibrantColor(Color.CYAN)
            val vibrantColor = Palette.from(bitmap).generate().getVibrantColor(Color.CYAN)
            val darkMutedColor = Palette.from(bitmap).generate().getMutedColor(Color.TRANSPARENT)
            reload.apply {
                setBackgroundColo(darkMutedColor)
            }
            materialViewPager.pagerTitleStrip.apply {
                indicatorColor = lightVibrantColor
                describe.apply {
                    maxLines = 2
                    singleLine = false
                    text = "${fragements[it].desc}"
                    setTextColor(vibrantColor)
                    strokeColor = darkMutedColor
                    strokeWidth = 3.0f
                }
            }
            fragements[it].loadData()
            return@setMaterialViewPagerListener HeaderDesign.fromColorAndDrawable(darkMutedColor, BitmapDrawable(resources, bitmap))
        }
        reload.onClick {
            fragements[currentFragmentIndex].loadData(true)
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
}
