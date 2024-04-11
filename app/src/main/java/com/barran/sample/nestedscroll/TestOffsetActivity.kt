package com.barran.sample.nestedscroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View

import com.barran.sample.R
import com.barran.sample.layoutinflater.TestFactory2Activity

/**
 * view的移动
 *
 * Created by tanwei on 2018/5/10.
 */

class TestOffsetActivity : TestFactory2Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_test_offset)
    }

    fun scrollBy(v: View) {
        v.scrollBy(20, -20)
    }

    fun offset(v: View) {
        offsetView(v, 20, -20)
    }

    private fun offsetView(v: View, x: Int, y: Int) {
        v.offsetLeftAndRight(x)
        v.offsetTopAndBottom(y)
    }

    fun layout(v: View) {
        layoutView(v, 20, -20)
    }

    private fun layoutView(v: View, x: Int, y: Int) {
        v.layout(v.left + x, v.top + y, v.right + x, v.bottom + y)
    }

    fun scrollTo(v: View) {
        v.scrollTo(20, -20)
    }
}
