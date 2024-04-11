package com.barran.sample

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.content.PermissionChecker
import androidx.core.view.doOnAttach
import com.barran.sample.asmtest.dataclass.TestDataClass
import com.barran.sample.compose.TestCompostActivity
import com.barran.sample.constraint.TestConstraintLayout2Activity
import com.barran.sample.constraint.TestConstraintLayoutActivity
import com.barran.sample.hardware.HardwareTestAct
import com.barran.sample.html.WebVideoActivity
import com.barran.sample.jni.TestJniActivity
import com.barran.sample.layoutinflater.TestFactory2Activity
import com.barran.sample.mdtest.*
import com.barran.sample.nestedscroll.TestOffsetActivity
import com.barran.sample.other.OtherTestAct
import com.barran.sample.photopicker.PhotoPickerActivity
import com.barran.sample.view.TestPathActivity


/**
 * 测试入口界面
 *
 * Created by tanwei on 2016/9/7.
 */
class TestActivity : TestFactory2Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v(App.TAG, "TestActivity onCreate")
        val listener = ClickListener()
        val view = findViewById<View>(R.id.test_goto_tab)
        view.post {
            Log.v(App.TAG, "view post run width ${view.width} height ${view.height}")
        }
        view.doOnAttach {
            Log.v(App.TAG, "view doOnAttach width ${view.width} height ${view.height}")
        }
        view.viewTreeObserver.addOnGlobalLayoutListener {
            Log.v(App.TAG, "view global layout width ${view.width} height ${view.height}")
        }
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
        findViewById<View>(R.id.test_compose).setOnClickListener(listener)
        findViewById<View>(R.id.test_native).setOnClickListener(listener)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        test()
    }

    private fun test(){

        //        TestUnit.test()
        //        testPermission()

        //        setupNotifyChannel()

        val dataTest = TestDataClass()
        dataTest.test()
    }

    override fun onResume() {
        super.onResume()

        Log.v(App.TAG, "TestActivity onResume")
        val view = findViewById<View>(R.id.test_goto_fab)
        Log.v(App.TAG, "view width ${view.width} height ${view.height}")

        // test log
//        TestActivity onResume
//        view width 0 height 0
//        view doOnAttach width 0 height 0
//        view global layout width 287 height 126
//        view post run width 287 height 126
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

    private fun setupNotifyChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val context = applicationContext
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelImportance: Int =
            NotificationManager.IMPORTANCE_HIGH
        val liveChannel = NotificationChannel(
            "message",
            "Message", channelImportance
        )
        liveChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        liveChannel.setShowBadge(true)
        manager.createNotificationChannel(liveChannel)
        val chatChannel = NotificationChannel(
            "event",
            "Event", channelImportance
        )
        chatChannel.enableLights(true)
        chatChannel.lightColor = Color.GREEN
        chatChannel.enableVibration(true)
        chatChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        chatChannel.setShowBadge(true)
        manager.createNotificationChannel(chatChannel)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 2222)
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
        } else if (requestCode == 2222) {
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

                R.id.test_compose -> {
                    startActivity(
                        Intent(
                            this@TestActivity,
                            TestCompostActivity::class.java
                        )
                    )
                }

                R.id.test_native -> {
                    startActivity(
                        Intent(
                            this@TestActivity,
                            TestJniActivity::class.java
                        )
                    )
                }
            }
        }
    }
}
