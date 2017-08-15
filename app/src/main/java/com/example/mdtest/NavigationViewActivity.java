package com.example.mdtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * NavigationView的使用
 *
 * Created by tanwei on 2017/8/15.
 */

public class NavigationViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_view);

        // DrawerLayout在侧边栏可以通过手势滑动显示
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(
                R.id.activity_navigation_view_drawer_layout);

        // 也可通过代码来控制显示
        // 对应的关闭则调用closeDrawer(Gravity.LEFT);
        findViewById(R.id.activity_navigation_view_tv_content)
                .setOnClickListener(v -> drawerLayout.openDrawer(Gravity.LEFT));

        NavigationView navigationView = (NavigationView) findViewById(
                R.id.activity_navigation_view);
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(v -> showToast("header image"));

        // 默认menu显示icon会变成灰色，即默认的ColorTint颜色，可以使用itemIconTint修改颜色,但是全部使用一个颜色
        // app:itemIconTint="@color/blue"
        // 也可以禁用这个功能，使用图片原来的颜色
        // navigationView.setItemIconTintList(null);
        
        navigationView.setNavigationItemSelectedListener(item -> {
            
            switch (item.getItemId()) {
                case R.id.menu_navigation_favorite:
                    showToast("menu navigation favorite");
                    break;
                
                case R.id.menu_navigation_wallet:
                    showToast("menu navigation wallet");
                    break;
                
                case R.id.menu_navigation_photo:
                    showToast("menu navigation photo");
                    break;
                
                case R.id.menu_navigation_file:
                    showToast("menu navigation file");
                    break;
            }
            
            return false;
        });
    }
    
    private void showToast(String content) {
        Toast.makeText(NavigationViewActivity.this, content, Toast.LENGTH_SHORT).show();
    }
}
