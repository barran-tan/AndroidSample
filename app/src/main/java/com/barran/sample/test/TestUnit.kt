package com.barran.sample.test

import android.util.Log
import com.barran.sample.asmtest.AsmTest
import com.google.gson.GsonBuilder
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

object TestUnit {

    fun test() {
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

//        testAsm()
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
        var data: GsonData? = GsonBuilder().create().fromJson<GsonData>(gson, GsonData::class.java)
        Log.i("gson", "result fromJson($gson) is=$data")

        gson = null
        data = GsonBuilder().create().fromJson<GsonData>(gson as String?, GsonData::class.java)
        Log.i("gson", "result fromJson($gson) is=$data")
    }

    // 误用导致降低主线程优先级
    private fun testThread() {
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

    private fun testDecimal() {
        val num = 474.769
        val format1 = DecimalFormat(".#")
        format1.setRoundingMode(RoundingMode.HALF_DOWN)
        Log.v("test", "format1 $num ${format1.format(num)}")

        val format2 = DecimalFormat("#")
        format2.setRoundingMode(RoundingMode.HALF_DOWN)
        Log.v("test", "format2 $num ${format2.format(num)}")


        Log.v("test", "474769948 ${formatNumberNoDot(474769948)}")

        Log.v("test", "47474 ${formatNumberNoDot(47474)}")


        Log.v("test", "474.769 0 ${formatWithDot("474.769000", 0)}")
        Log.v("test", "474.769 1 ${formatWithDot("474.769000", 1)}")
        Log.v("test", "474.769 2 ${formatWithDot("474.769000", 2)}")
        Log.v("test", "474.769 3 ${formatWithDot("474.769000", 3)}")
        Log.v("test", "474.769 4 ${formatWithDot("474.769000", 4)}")
        Log.v("test", "474.769 5 ${formatWithDot("474.769000", 5)}")

        Log.v("test", "474.0 0 ${formatWithDot("474.0", 0)}")
        Log.v("test", "474.0 1 ${formatWithDot("474.0", 1)}")
        Log.v("test", "474.0 2 ${formatWithDot("474.0", 2)}")
    }

    private fun formatNumberNoDot(number: Long): String? {
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
}