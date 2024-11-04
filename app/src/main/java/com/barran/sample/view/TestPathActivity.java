package com.barran.sample.view;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;


import com.barran.sample.R;
import com.barran.sample.layoutinflater.TestFactory2Activity;

/**
 * test {@link android.graphics.Path Path}
 *
 * Created by tanwei on 2017/9/20.
 */

public class TestPathActivity extends TestFactory2Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        Log.i("lifecycle","call finish in onCreate");
        finish();
        // call finish in onCreate
        // onDestroy

        // 原因
        // ActivityThread在执行performResumeActivity时，会判断mFinished标记，true直接返回
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("lifecycle","onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("lifecycle","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("lifecycle","onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("lifecycle","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lifecycle","onResume");
    }
}
