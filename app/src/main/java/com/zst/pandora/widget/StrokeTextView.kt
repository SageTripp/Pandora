package com.zst.pandora.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.textColor

/**
 * Created by zst on 2016-09-01  0001.
 * 描述:
 */
class StrokeTextView : TextView {
    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(ctx, attrs, defStyleAttr, defStyleRes) {
        strokeText = TextView(context, attrs, defStyleAttr, defStyleRes)
    }

    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(ctx, attrs, defStyleAttr, 0)
    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs) {
        strokeText = TextView(context, attrs)
    }

    constructor(ctx: Context) : this(ctx, null)

    var strokeColor: Int = -1
    var strokeWidth: Float = -1.0f

    private lateinit var strokeText: TextView

    private fun init() {
        strokeText.apply {
            gravity = this@StrokeTextView.gravity
            textColor = strokeColor
            paint.apply {
                style = Paint.Style.STROKE
                strokeWidth = this@StrokeTextView.strokeWidth
            }
        }
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        strokeText.layoutParams = params
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!strokeText.text.equals(this@StrokeTextView.text)) {
            strokeText.text = ""
            strokeText.text = this@StrokeTextView.text
            postInvalidate()
        }
        strokeText.measure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        strokeText.layout(left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        init()
        if (!strokeText.text.equals(this@StrokeTextView.text)) {
            strokeText.text = ""
            strokeText.text = this@StrokeTextView.text
            postInvalidate()
        }
        strokeText.draw(canvas)
        super.onDraw(canvas)
    }

}