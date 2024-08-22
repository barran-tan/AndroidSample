package com.barran.sample.constraint

import android.content.Context
import android.util.AttributeSet
import android.view.ViewAnimationUtils
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.hypot

class CircularRevealHelper @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintHelper(context, attrs, defStyleAttr) {

    override fun updatePostLayout(container: ConstraintLayout?) {
        super.updatePostLayout(container)
        getViews(container).forEach { view ->
            ViewAnimationUtils.createCircularReveal(
                view, view.width / 2,
                view.height / 2, 0f,
                hypot((view.height / 2.0), (view.width / 2.0)).toFloat()
            ).apply {
                duration = 1000
                start()
            }
        }
    }
}
