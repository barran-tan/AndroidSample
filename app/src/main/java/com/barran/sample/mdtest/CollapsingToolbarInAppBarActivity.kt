package com.barran.sample.mdtest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.lang.ref.WeakReference
import com.barran.sample.R

/**
 * CollapsingToolbarLayout包装AppBarLayout实现可缩放的标题栏
 *
 * Created by tanwei on 2016/9/7.
 */
class CollapsingToolbarInAppBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collapsing_toolbar_in_app_bar)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //        setSupportActionBar(toolbar)// 点击无效？
        //        getSupportActionBar().setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        Log.i("tanwei", "decorView ${window.decorView}")
        window.decorView.viewTreeObserver.addOnScrollChangedListener {
            val viewRoot = window.decorView.parent
            Log.i("tanwei", "root $viewRoot")

            try {

//                val fields = viewRoot.javaClass.getDeclaredFields()

//                for(f in fields){
//                    Log.i("tanwei", "filed ${f.name}")
//                }

                val field = viewRoot.javaClass.getDeclaredField("mLastScrolledFocus")
                field.isAccessible = true
                val scrollView = field.get(viewRoot) as WeakReference<View?>?
                Log.i("tanwei", "mLastScrolledFocus ${scrollView?.get()}")
            } catch (e: Exception) {
                Log.i("tanwei", "get focus view failed ${e.message}")
            }
        }
    }
}
