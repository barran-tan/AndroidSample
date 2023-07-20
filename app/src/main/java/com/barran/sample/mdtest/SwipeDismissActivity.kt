package com.barran.sample.mdtest

import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.SwipeDismissBehavior
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView

import com.barran.sample.R

/**
 *
 *
 * Created by tanwei on 2016/10/31.
 */

class SwipeDismissActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_swipe_dismiss)

        val tv = findViewById<TextView>(R.id.swipe_dismiss_text)

        val behavior = SwipeDismissBehavior<View>()
        behavior.setStartAlphaSwipeDistance(0.1f)
        behavior.setEndAlphaSwipeDistance(0.6f)
        //        behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
        behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY)
        behavior.setListener(object : SwipeDismissBehavior.OnDismissListener {
            override fun onDismiss(view: View) {
                Log.v("swipe", "onDismiss")
                view.visibility = View.GONE
            }

            override fun onDragStateChanged(state: Int) {
                Log.v("swipe", "onDragStateChanged  $state")
            }
        })

        (tv.layoutParams as CoordinatorLayout.LayoutParams).behavior = behavior
    }
}
