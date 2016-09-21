package com.zst.pandora.prism

import android.support.annotation.ColorInt
import com.stylingandroid.prism.filter.Filter
import com.stylingandroid.prism.setter.BaseSetter
import com.zst.pandora.widget.StrokeTextView

/**
 * Created by zst on 2016-09-21  0021.
 * 描述:
 */
class StrokeSetter(val strokeTextView: StrokeTextView, filter: Filter?) : BaseSetter(filter) {
    override fun onSetColour(@ColorInt colour: Int) {
        strokeTextView.strokeColor = colour
    }
}