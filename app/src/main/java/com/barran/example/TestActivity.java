package com.barran.example;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.barran.example.constraint.TestConstraintLayout2Activity;
import com.barran.example.constraint.TestConstraintLayoutActivity;
import com.barran.example.html.WebVideoActivity;
import com.barran.example.mdtest.CollapsingToolbarInAppBarActivity;
import com.barran.example.mdtest.FABInCoordinateLayoutActivity;
import com.barran.example.mdtest.NavigationViewActivity;
import com.barran.example.mdtest.R;
import com.barran.example.mdtest.SwipeDeleteActivity;
import com.barran.example.mdtest.SwipeDismissActivity;
import com.barran.example.mdtest.TabInAppBarActivity;
import com.barran.example.nestedscroll.TestOffsetActivity;
import com.barran.example.view.TestPathActivity;

/**
 * 测试入口界面
 *
 * Created by tanwei on 2016/9/7.
 */
public class TestActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
        findViewById(R.id.test_constraint_1_1).setOnClickListener(listener);
        
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
                
                case R.id.test_constraint_1_1:
                    intent = new Intent(TestActivity.this,
                            TestConstraintLayout2Activity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
