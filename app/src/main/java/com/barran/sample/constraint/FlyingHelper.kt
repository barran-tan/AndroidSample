package com.barran.sample.constraint

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import androidx.constraintlayout.helper.widget.Layer
import androidx.constraintlayout.widget.ConstraintLayout

class FlyingHelper @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Layer(context, attrs, defStyleAttr) {

    override fun updatePostLayout(container: ConstraintLayout) {
        super.updatePostLayout(container)
        val centerPoint = PointF(((left + right) / 2).toFloat(), ((top + bottom) / 2).toFloat())
        ValueAnimator.ofFloat(0f, 1f).setDuration(1000).apply {
            addUpdateListener { animation ->
                val animatedFraction = animation.animatedFraction
                updateTranslation(centerPoint, animatedFraction, container)
            }
            start()
        }
    }

    private fun updateTranslation(centerPoint: PointF, animatedFraction: Float, container: ConstraintLayout) {
        val views = getViews(container)
        for (view in views) {
            val viewCenterX = (view.left + view.right) / 2
            val viewCenterY = (view.top + view.bottom) / 2
            val startTranslationX = if (viewCenterX < centerPoint.x) -2000f else 2000f
            val startTranslationY = if (viewCenterY < centerPoint.y) -2000f else 2000f
            view.translationX = (1 - animatedFraction) * startTranslationX
            view.translationY = (1 - animatedFraction) * startTranslationY
        }
    }
}