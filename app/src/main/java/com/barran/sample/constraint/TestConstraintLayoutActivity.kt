package com.barran.sample.constraint

import android.app.Activity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

import com.barran.sample.R

/**
 * Created by tanwei on 2017/3/6.
 */
class TestConstraintLayoutActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_constraint_layout)

        val layout = findViewById<ConstraintLayout>(R.id.temp_constraint_layout)

        // new
//        val set = ConstraintSet()
//        set.connect(ConstraintSet.LEFT, R.id.temp_text1, ConstraintSet.RIGHT, R.id.temp_text2)
//        set.applyTo(layout)

        // clone
//        set.clone(layout)
//        set.setHorizontalChainStyle(R.id.temp_text1, ConstraintSet.CHAIN_SPREAD_INSIDE)
//        set.applyTo(layout)
    }
}
