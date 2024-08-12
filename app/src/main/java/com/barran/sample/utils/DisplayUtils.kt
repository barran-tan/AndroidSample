package com.barran.sample.utils

import com.barran.sample.App


val Number.dp: Int
    get() =
        (App.context.resources.displayMetrics.density * this.toFloat() + 0.5f).toInt()

fun dp2px(dpValue: Float): Int {
    val scale: Float = App.context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}