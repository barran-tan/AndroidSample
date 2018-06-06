package com.example.mdtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.html.WebVideoActivity;
import com.example.nestedscroll.TestOffsetActivity;
import com.example.view.TestPathActivity;

/**
 * 测试入口界面
 *
 * Created by tanwei on 2016/9/7.
 */
public class TestActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        
        ClickListener listener = new ClickListener();
        findViewById(R.id.test_goto_login).setOnClickListener(listener);
        findViewById(R.id.test_goto_tab).setOnClickListener(listener);
        findViewById(R.id.test_goto_fab).setOnClickListener(listener);
        findViewById(R.id.test_goto_collapse_tool_bar).setOnClickListener(listener);
        findViewById(R.id.test_swipe_dismiss).setOnClickListener(listener);
        findViewById(R.id.test_swipe_delete).setOnClickListener(listener);
        findViewById(R.id.test_constraint_activity).setOnClickListener(listener);
        findViewById(R.id.test_path).setOnClickListener(listener);
        findViewById(R.id.test_navigation_view).setOnClickListener(listener);
        findViewById(R.id.test_web_video).setOnClickListener(listener);
        findViewById(R.id.test_offset).setOnClickListener(listener);
        
    }
    
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.test_goto_login:
                    intent = new Intent(TestActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_goto_tab:
                    intent = new Intent(TestActivity.this, TabInAppBarActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_goto_fab:
                    intent = new Intent(TestActivity.this,
                            FABInCoordinateLayoutActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_goto_collapse_tool_bar:
                    intent = new Intent(TestActivity.this,
                            CollapsingToolbarInAppBarActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_swipe_dismiss:
                    intent = new Intent(TestActivity.this, SwipeDismissActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_swipe_delete:
                    intent = new Intent(TestActivity.this, SwipeDeleteActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_constraint_activity:
                    intent = new Intent(TestActivity.this,
                            TestConstraintLayoutActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_path:
                    intent = new Intent(TestActivity.this, TestPathActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_navigation_view:
                    intent = new Intent(TestActivity.this, NavigationViewActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_web_video:
                    intent = new Intent(TestActivity.this, WebVideoActivity.class);
                    startActivity(intent);
                    break;
                
                case R.id.test_offset:
                    intent = new Intent(TestActivity.this, TestOffsetActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
