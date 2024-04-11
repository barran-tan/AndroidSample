package com.barran.sample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()

    private val text = "CustomView"

    private var textWidth = 0f

    init {
        paint.textSize = 50f
        paint.color = Color.GREEN

        textWidth = paint.measureText(text)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            drawText(text, (width - textWidth) / 2, height / 2f, paint)
        }
    }
}