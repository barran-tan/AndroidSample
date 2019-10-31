package com.barran.example.mdtest

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.Toast

/**
 * NavigationView的使用
 *
 * Created by tanwei on 2017/8/15.
 */

class NavigationViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_view)

        // DrawerLayout在侧边栏可以通过手势滑动显示
        val drawerLayout = findViewById<DrawerLayout>(
                R.id.activity_navigation_view_drawer_layout)

        // 也可通过代码来控制显示
        // 对应的关闭则调用closeDrawer(Gravity.LEFT)
        findViewById<View>(R.id.activity_navigation_view_tv_content)
                .setOnClickListener { v -> drawerLayout.openDrawer(Gravity.LEFT) }

        val navigationView = findViewById<NavigationView>(
                R.id.activity_navigation_view)
        val headerView = navigationView.getHeaderView(0)
        headerView.setOnClickListener { v -> showToast("header image") }

        // 默认menu显示icon会变成灰色，即默认的ColorTint颜色，可以使用itemIconTint修改颜色,但是全部使用一个颜色
        // app:itemIconTint="@color/blue"
        // 也可以禁用这个功能，使用图片原来的颜色
        // navigationView.setItemIconTintList(null)

        navigationView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_navigation_favorite -> showToast("menu navigation favorite")

                R.id.menu_navigation_wallet -> showToast("menu navigation wallet")

                R.id.menu_navigation_photo -> showToast("menu navigation photo")

                R.id.menu_navigation_file -> showToast("menu navigation file")
            }

            false
        }
    }

    private fun showToast(content: String) {
        Toast.makeText(this@NavigationViewActivity, content, Toast.LENGTH_SHORT).show()
    }
}
