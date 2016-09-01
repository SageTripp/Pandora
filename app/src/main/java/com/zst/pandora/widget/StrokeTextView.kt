package com.zst.pandora.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by zst on 2016-09-01  0001.
 * 描述:
 */
class StrokeTextView : TextView {
    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(ctx, attrs, defStyleAttr, defStyleRes)
    constructor(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(ctx, attrs, defStyleAttr, 0)
    constructor(ctx: Context, attrs: AttributeSet?) : super(ctx, attrs)
    constructor(ctx: Context) : this(ctx, null)

    var strokeColor: Int = -1
    var strokeWidth: Float = -1.0f

    val strokePaint: Paint by lazy { Paint() }

    override fun onDraw(canvas: Canvas) {
        if (strokeColor != -1 && strokeWidth != -1.0f) {

            strokePaint.textSize = paint.textSize
            strokePaint.typeface = paint.typeface
            strokePaint.flags = paint.flags
            strokePaint.alpha = paint.alpha

            // 自定义描边效果
            strokePaint.style = Paint.Style.STROKE
            strokePaint.color = strokeColor
            strokePaint.strokeWidth = strokeWidth

            val text = text.toString()
            canvas.drawText(text, (width - strokePaint.measureText(text)) / 2, baseline.toFloat(), strokePaint)
        }
        super.onDraw(canvas)
    }

}