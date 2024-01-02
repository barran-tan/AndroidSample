package com.barran.sample.jni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.barran.sample.R
import com.barran.sample.databinding.ActivityTestJniBinding
import com.example.nativelib.NativeLib

class TestJniActivity : AppCompatActivity() {

    private lateinit var bind: ActivityTestJniBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_jni)

        bind = ActivityTestJniBinding.bind(findViewById(R.id.cl_container))

        initView()
    }

    private fun initView() {
        val native = NativeLib()
        bind.apply {
            testJni.setOnClickListener {
                val str = native.stringFromJNI()
                appendLog("stringFromJNI:$str")
            }

            testAccessClassPath.setOnClickListener {
                val classLoader = this.javaClass.classLoader
                val field = try {
                    classLoader.javaClass.getField("classTable")
                } catch (e: Exception) {
                    appendLog("get field failed=${e.message}")
                    return@setOnClickListener
                }
                field.isAccessible = true

                val classTable = field.get(classLoader)
                appendLog("classTable=$classTable")
                if (classTable is Int) {
                    appendLog(
                        "check class loaded ${
                            native.checkClassLoaded(
                                classTable,
                                this@TestJniActivity.javaClass.name
                            )
                        }"
                    )
                }
            }

            testDynamicNativeFunc.setOnClickListener {
                val str = native.dynamicNativeFunc()
                appendLog("dynamicNativeFunc:$str")
            }
        }
    }

    private fun appendLog(log: String) {
        val tv = bind.tvLog
        val sb = StringBuilder()
        if (tv.text.isNotEmpty()) {
            sb.append(tv.text)
            sb.append("\n")
        }
        sb.append(log)

        tv.text = sb.toString()
    }
}