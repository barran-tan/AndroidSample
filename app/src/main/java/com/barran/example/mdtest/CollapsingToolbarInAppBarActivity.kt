package com.barran.example.mdtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View

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
    }
}
