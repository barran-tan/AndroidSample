package com.barran.sample

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import com.barran.sample.constraint.TestConstraintLayout2Activity
import com.barran.sample.constraint.TestConstraintLayoutActivity
import com.barran.sample.hardware.HardwareTestAct
import com.barran.sample.html.WebVideoActivity
import com.barran.sample.mdtest.*
import com.barran.sample.nestedscroll.TestOffsetActivity
import com.barran.sample.other.OtherTestAct
import com.barran.sample.photopicker.PhotoPickerActivity
import com.barran.sample.view.TestPathActivity
import java.util.*


/**
 * 测试入口界面
 *
 * Created by tanwei on 2016/9/7.
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = ClickListener()
        val view = findViewById<View>(R.id.test_goto_tab)
        view.setOnClickListener(listener)
        findViewById<View>(R.id.test_goto_fab).setOnClickListener(listener)
        findViewById<View>(R.id.test_goto_collapse_tool_bar).setOnClickListener(listener)
        findViewById<View>(R.id.test_swipe_dismiss).setOnClickListener(listener)
        findViewById<View>(R.id.test_swipe_delete).setOnClickListener(listener)
        findViewById<View>(R.id.test_constraint_activity).setOnClickListener(listener)
        findViewById<View>(R.id.test_path).setOnClickListener(listener)
        findViewById<View>(R.id.test_navigation_view).setOnClickListener(listener)
        findViewById<View>(R.id.test_web_video).setOnClickListener(listener)
        findViewById<View>(R.id.test_offset).setOnClickListener(listener)
        findViewById<View>(R.id.test_constraint_1_1).setOnClickListener(listener)
        findViewById<View>(R.id.test_hardware_accelerated).setOnClickListener(listener)
        findViewById<View>(R.id.test_other).setOnClickListener(listener)
        findViewById<View>(R.id.test_photo_picker).setOnClickListener(listener)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

//        TestUnit.test()
        //        testPermission()
    }

    private fun testPermission(){
        val permission = PermissionChecker.checkSelfPermission(
            this@TestActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        Log.v(
            "permission",
            "has write_external_storage $permission"
        )

        if (permission != PermissionChecker.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1111)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1111) {
            val size = permissions.size
            for (i in 0 until size) {
                Log.v(
                    "permission",
                    "req ${permissions[i]} result ${grantResults[i]}"
                )
            }
        }
    }

    internal inner class ClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            val intent: Intent
            when (v?.id) {
                R.id.test_goto_tab -> {
                    intent = Intent(this@TestActivity, TabInAppBarActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_goto_fab -> {
                    intent = Intent(this@TestActivity,
                            FABInCoordinateLayoutActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_goto_collapse_tool_bar -> {
                    intent = Intent(this@TestActivity,
                            CollapsingToolbarInAppBarActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_swipe_dismiss -> {
                    intent = Intent(this@TestActivity, SwipeDismissActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_swipe_delete -> {
                    intent = Intent(this@TestActivity, SwipeDeleteActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_constraint_activity -> {
                    intent = Intent(this@TestActivity,
                            TestConstraintLayoutActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_path -> {
                    intent = Intent(this@TestActivity, TestPathActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_navigation_view -> {
                    intent = Intent(this@TestActivity, NavigationViewActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_web_video -> {
                    intent = Intent(this@TestActivity, WebVideoActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_offset -> {
                    intent = Intent(this@TestActivity, TestOffsetActivity::class.java)
                    startActivity(intent)
                }

                R.id.test_constraint_1_1 -> {
                    intent = Intent(
                        this@TestActivity,
                        TestConstraintLayout2Activity::class.java
                    )
                    startActivity(intent)
                }

                R.id.test_hardware_accelerated -> {
                    startActivity(
                        Intent(
                            this@TestActivity,
                            HardwareTestAct::class.java
                        )
                    )
                }

                R.id.test_other -> {
                    startActivity(
                        Intent(
                            this@TestActivity,
                            OtherTestAct::class.java
                        )
                    )
                }

                R.id.test_photo_picker->{
                    startActivity(
                        Intent(
                            this@TestActivity,
                            PhotoPickerActivity::class.java
                        )
                    )
                }
            }
        }
    }
}
