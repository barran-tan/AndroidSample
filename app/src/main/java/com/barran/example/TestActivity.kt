package com.barran.example

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.barran.example.constraint.TestConstraintLayout2Activity
import com.barran.example.constraint.TestConstraintLayoutActivity
import com.barran.example.hardware.HardwareTestAct
import com.barran.example.html.WebVideoActivity
import com.barran.example.mdtest.*
import com.barran.example.nestedscroll.TestOffsetActivity
import com.barran.example.view.TestPathActivity
import com.google.gson.GsonBuilder
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

        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

//        test()

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                Log.i("test", "schedule task run")

                testGson()
            }
        }, 5000)

        view.postDelayed({ getAlive(timer) }, 1000)

        view.postDelayed({ getAlive(timer) }, 10000)
        view.postDelayed({ getAlive(timer) }, 20000)
    }

    private fun getAlive(timer: Timer) {
        val thread =
            try {
                timer.javaClass.getDeclaredField("thread")
            } catch (e: NoSuchFieldException) {
                return
            }
        thread.isAccessible = true
        val threadObj = thread.get(timer) as Thread
        Log.i("test", "timer isAlive  ${threadObj.isAlive}")

//        val declaredFields = timer.javaClass.declaredFields
//        for(field in declaredFields){
//            Log.v("test", "filed : $field")
//        }
    }

    private fun testGson() {
        var gson: String? = ""
        var data:GsonData? = GsonBuilder().create().fromJson<GsonData>(gson, GsonData::class.java)
        Log.i("gson", "result fromJson($gson) is=$data")

        gson = null
        data = GsonBuilder().create().fromJson<GsonData>(gson as String?, GsonData::class.java)
        Log.i("gson", "result fromJson($gson) is=$data")
    }

    // 误用导致降低主线程优先级
    private fun test() {
        val t = Thread()
        t.start()
        t.priority = 3
        val startTime: Long =
            System.currentTimeMillis()
        Thread.sleep(10)
        // 结果为duration = 50ms
        Log.i(
            "Matrix",
            "duration = " + (System.currentTimeMillis() - startTime)
        )

    }

    val FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000

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
            }
        }
    }
}
