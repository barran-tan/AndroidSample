package com.barran.sample;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * description
 * <p>
 * on 2022/5/31
 */
public class App extends Application {

    private static final String TAG = "test_app";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.v(TAG, "attachBaseContext");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
    }
}
