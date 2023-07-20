package com.barran.sample.mdtest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.barran.sample.R

/**
 * 测试fab在CoordinatorLayout的表现&SnackBar
 *
 * Created by tanwei on 2016/9/7.
 */
class FABInCoordinateLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fab_in_coordinate_layout)

        findViewById<View>(R.id.fab).setOnClickListener { v ->
            Snackbar.make(v, "you have clicked fab", Snackbar.LENGTH_LONG)
                    .setAction("i know") {
                        Toast.makeText(this,
                                "you have reviewed the tip", Toast.LENGTH_SHORT)
                                .show()
                        finish()
                    }.show()
        }
    }
}
