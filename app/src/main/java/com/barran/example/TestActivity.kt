package com.barran.example

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import com.barran.example.asmtest.AsmTest
import com.barran.example.constraint.TestConstraintLayout2Activity
import com.barran.example.constraint.TestConstraintLayoutActivity
import com.barran.example.hardware.HardwareTestAct
import com.barran.example.html.WebVideoActivity
import com.barran.example.mdtest.*
import com.barran.example.nestedscroll.TestOffsetActivity
import com.barran.example.other.OtherTestAct
import com.barran.example.view.TestPathActivity
import com.google.gson.GsonBuilder
import java.math.RoundingMode
import java.text.DecimalFormat
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

        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )

//        test()
        testDecimal()

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                Log.i("test", "schedule task run")

                testGson()
            }
        }, 5000)

//        view.postDelayed({ getAlive(timer) }, 1000)
//
//        view.postDelayed({ getAlive(timer) }, 10000)
//        view.postDelayed({ getAlive(timer) }, 20000)

        testPermission()
        testAsm()
    }

    private fun testAsm() {
        val test = AsmTest()
        test.test()
        test.test2()
        test.test3()
        test.test4()
        test.test5()
        test.test6(Runnable {
            Log.v(
                AsmTest.TAG,
                "runnable 6 run on " + Thread.currentThread().name
            )
        })
        test.test7(Runnable {
            Log.v(
                AsmTest.TAG,
                "runnable 7 run on " + Thread.currentThread().name
            )
        })
        test.newThread(Runnable {
            Log.v(
                AsmTest.TAG,
                "newThread run on " + Thread.currentThread().name
            )
        }).start()
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


    private fun testDecimal(){
        val num = 474.769
        val format1 = DecimalFormat(".#")
        format1.setRoundingMode(RoundingMode.HALF_DOWN)
        Log.v("test", "format1 $num ${format1.format(num)}")

        val format2 = DecimalFormat("#")
        format2.setRoundingMode(RoundingMode.HALF_DOWN)
        Log.v("test", "format2 $num ${format2.format(num)}")


        Log.v("test", "474769948 ${formatNumberNoDot(474769948 )}")

        Log.v("test", "47474 ${formatNumberNoDot(47474 )}")


        Log.v("test", "474.769 0 ${formatWithDot("474.769000",0)}")
        Log.v("test", "474.769 1 ${formatWithDot("474.769000",1)}")
        Log.v("test", "474.769 2 ${formatWithDot("474.769000",2)}")
        Log.v("test", "474.769 3 ${formatWithDot("474.769000",3)}")
        Log.v("test", "474.769 4 ${formatWithDot("474.769000",4)}")
        Log.v("test", "474.769 5 ${formatWithDot("474.769000",5)}")

        Log.v("test", "474.0 0 ${formatWithDot("474.0",0)}")
        Log.v("test", "474.0 1 ${formatWithDot("474.0",1)}")
        Log.v("test", "474.0 2 ${formatWithDot("474.0",2)}")
    }

    fun formatNumberNoDot(number: Long): String? {
        if (number <= 0) {
            return "0"
        }
        return if (number < Math.pow(10.0, 4.0)) {
            number.toString() + ""
        } else if (number < Math.pow(10.0, 6.0)) {
            (number / Math.pow(10.0, 3.0)).toInt().toString() + "K"
        } else if (number < Math.pow(10.0, 9.0)) {
            (number / Math.pow(10.0, 6.0)).toInt().toString() + "M"
        } else if (number < Math.pow(10.0, 12.0)) {
            (number / Math.pow(10.0, 9.0)).toInt().toString() + "B"
        } else {
            "999B+"
        }
    }

    private fun formatWithDot(value: String, dot: Int): String? {
        val index = value.indexOf(".")
        return if (index >= 0) {
            if (dot <= 0) {
                value.substring(0, index)
            } else {
                // 先按小数位数截取
                val end = Math.min(value.length, index + dot + 1)

                // 再剔除末尾的0
                var zeroIndex = 0
                for (i in end - 1 downTo index) {
                    zeroIndex = if (value[i] == '0') {
                        i
                    } else {
                        break
                    }
                }
                if (zeroIndex == 0) {
                    return value.substring(0, end)
                } else if (zeroIndex > index + 1) {
                    value.substring(0, zeroIndex)
                } else {
                    value.substring(0, index)
                }
            }
        } else {
            value
        }
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

                R.id.test_other -> {
                    startActivity(
                        Intent(
                            this@TestActivity,
                            OtherTestAct::class.java
                        )
                    )
                }
            }
        }
    }
}
