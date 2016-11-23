package com.example.mdtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
    }

    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.test_goto_login:
                    intent = new Intent(TestActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;

                case R.id.test_goto_tab:
                    intent = new Intent(TestActivity.this, TabInAppBar.class);
                    startActivity(intent);
                    break;

                case R.id.test_goto_fab:
                    intent = new Intent(TestActivity.this, FABInCoordinateLayout.class);
                    startActivity(intent);
                    break;

                case R.id.test_goto_collapse_tool_bar:
                    intent = new Intent(TestActivity.this, CollapsingToolbarInAppBar.class);
                    startActivity(intent);
                    break;

                case R.id.test_swipe_dismiss:
                    intent = new Intent(TestActivity.this, SwipeDismissActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
