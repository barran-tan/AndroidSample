package com.barran.sample.hardware

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView

/**
 *  ha test
 * on 2022/5/17
 */
class HAImageView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    style: Int = 0
) : AppCompatImageView(context, attributes, style) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw: canvas=${canvas.isHardwareAccelerated} view=${isHardwareAccelerated}")
    }
}