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
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.PermissionChecker
import androidx.core.view.doOnAttach
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.barran.sample.compose.TestCompostActivity
import com.barran.sample.constraint.CarouselHelperActivity
import com.barran.sample.constraint.TestConstraintLayout2Activity
import com.barran.sample.constraint.TestConstraintLayout3Activity
import com.barran.sample.constraint.TestConstraintLayout4Activity
import com.barran.sample.constraint.TestConstraintLayoutActivity
import com.barran.sample.constraint.TestMotionCarouselActivity
import com.barran.sample.constraint.carouselTypeList
import com.barran.sample.hardware.HardwareTestAct
import com.barran.sample.html.WebVideoActivity
import com.barran.sample.jni.TestJniActivity
import com.barran.sample.layoutinflater.TestFactory2Activity
import com.barran.sample.mdtest.*
import com.barran.sample.nestedscroll.TestOffsetActivity
import com.barran.sample.other.OtherTestAct
import com.barran.sample.photopicker.PhotoPickerActivity
import com.barran.sample.reflect.TestReflect
import com.barran.sample.utils.dp
import com.barran.sample.view.TestPathActivity


/**
 * 测试入口界面
 *
 * Created by tanwei on 2016/9/7.
 */
class TestActivity : TestFactory2Activity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var carouselTypeSpinner: Spinner

    private var carouselTestType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v(App.TAG, "TestActivity onCreate")
        recyclerView = findViewById(R.id.entry_list)
        carouselTypeSpinner = findViewById(R.id.carousel_test_type)
        setupEntries()
        recyclerView.post {
            Log.v(
                App.TAG,
                "view post run width ${recyclerView.width} height ${recyclerView.height}"
            )
        }
        recyclerView.doOnAttach {
            Log.v(
                App.TAG,
                "view doOnAttach width ${recyclerView.width} height ${recyclerView.height}"
            )
        }
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            Log.v(
                App.TAG,
                "view global layout width ${recyclerView.width} height ${recyclerView.height}"
            )
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        test()
    }

    private fun test() {

        //        TestUnit.test()
        //        testPermission()

        //        setupNotifyChannel()

//        val dataTest = TestDataClass()
//        dataTest.test()

//        val drawable = NewBitmapDrawable((getDrawable(R.drawable.delete) as BitmapDrawable).bitmap)
//        val drawable2 = drawable.constantState?.newDrawable()
//        Log.v("bitmap", "state=${drawable.constantState} newDrawable=${drawable2}")

        val id = R.drawable.delete
        val value = TestReflect.getValue(id)
        Log.i(
            App.TAG,
            "get value of ${Integer.toHexString(id)} : $value  den=${value?.density ?: 0}"
        )
    }

    override fun onResume() {
        super.onResume()

        Log.v(App.TAG, "TestActivity onResume")
        Log.v(App.TAG, "view width ${recyclerView.width} height ${recyclerView.height}")

        // test log
//        TestActivity onResume
//        view width 0 height 0
//        view doOnAttach width 0 height 0
//        view global layout width 287 height 126
//        view post run width 287 height 126

//        InvokeTest.testInvoke()
        InvokeTest2().innerTestDynamicProxy()
    }

    private fun testPermission() {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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

    fun setupEntries() {

        // entry data
        val entries = listOf(
            "test_goto_tab" to Intent(this@TestActivity, TabInAppBarActivity::class.java),
            "test_goto_fab" to Intent(
                this@TestActivity,
                FABInCoordinateLayoutActivity::class.java
            ),
            "test_goto_collapse_tool_bar" to Intent(
                this@TestActivity,
                CollapsingToolbarInAppBarActivity::class.java
            ),
            "test_swipe_dismiss" to Intent(this@TestActivity, SwipeDismissActivity::class.java),
            "test_swipe_delete" to Intent(this@TestActivity, SwipeDeleteActivity::class.java),
            "test_constraint_activity" to Intent(
                this@TestActivity,
                TestConstraintLayoutActivity::class.java
            ),
            "test_path" to Intent(this@TestActivity, TestPathActivity::class.java),
            "test_navigation_view" to Intent(
                this@TestActivity,
                NavigationViewActivity::class.java
            ),
            "test_web_video" to Intent(this@TestActivity, WebVideoActivity::class.java),
            "test_offset" to Intent(this@TestActivity, TestOffsetActivity::class.java),
            "test_constraint_1_1" to Intent(
                this@TestActivity,
                TestConstraintLayout2Activity::class.java
            ),
            "test_hardware_accelerated" to Intent(
                this@TestActivity,
                HardwareTestAct::class.java
            ),
            "test_other" to Intent(
                this@TestActivity,
                OtherTestAct::class.java
            ),
            "test_photo_picker" to Intent(
                this@TestActivity,
                PhotoPickerActivity::class.java
            ),
            "test_compose" to Intent(
                this@TestActivity,
                TestCompostActivity::class.java
            ),
            "test_native" to Intent(
                this@TestActivity,
                TestJniActivity::class.java
            ),
            "test_compat" to
                    // FATAL EXCEPTION: main
                    //     Process: com.barran.androidsample, PID: 8420
                    //     android.content.ActivityNotFoundException: No Activity found to handle Intent { act=test.compat }
                    //     	at android.app.Instrumentation.checkStartActivityResult(Instrumentation.java:2430)
                    //     	at android.app.Instrumentation.execStartActivity(Instrumentation.java:2005)
                    //     	at android.app.Activity.startActivityForResult(Activity.java:5840)
                    //     	at androidx.activity.ComponentActivity.startActivityForResult(ComponentActivity.java:728)
                    //     	at android.app.Activity.startActivityForResult(Activity.java:5798)
                    //     	at androidx.activity.ComponentActivity.startActivityForResult(ComponentActivity.java:709)
                    //     	at android.app.Activity.startActivity(Activity.java:6295)
                    //     	at android.app.Activity.startActivity(Activity.java:6262)
                    //     	at com.barran.sample.TestActivity$ClickListener.onClick(TestActivity.kt:303)
//                        Intent("test.compat")

                    Intent("test.compat").setPackage(applicationContext.packageName),
            "test_constraint_2" to Intent(
                this@TestActivity,
                TestConstraintLayout3Activity::class.java
            ),
            "test_constraint_2_filter" to Intent(
                this@TestActivity,
                TestConstraintLayout4Activity::class.java
            ),
            "test_constraint_2_motion_carousel" to Intent(
                this@TestActivity,
                TestMotionCarouselActivity::class.java
            ),
            "android_sample_carousel" to Intent(
                this@TestActivity,
                CarouselHelperActivity::class.java
            )
        )

        recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        recyclerView.adapter = EntryAdapter(entries.asSequence().map { it.first }.toList()) {
            val intent = entries[it].second
            if (entries[it].first == "android_sample_carousel") {
                intent.putExtra("testType", carouselTestType)
                startActivity(intent)
            } else {
                startActivity(intent)
            }
        }

        carouselTypeSpinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, carouselTypeList)
        carouselTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    carouselTestType = carouselTypeList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    carouselTestType = 0
                }
            }
    }
}

private class EntryAdapter(
    private val entryList: List<String>,
    private val listener: (Int) -> Unit
) : RecyclerView.Adapter<EntryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryHolder {
        val text = TextView(parent.context)
        text.setPadding(12.dp, 8.dp, 12.dp, 8.dp)
        val holder = EntryHolder(text)
        text.setOnClickListener {
            listener.invoke(holder.bindingAdapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: EntryHolder, position: Int) {
        holder.textView.text = entryList[position]
    }

    override fun getItemCount(): Int {
        return entryList.size
    }
}

private class EntryHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)