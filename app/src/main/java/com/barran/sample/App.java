package com.barran.sample;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import com.example.nativelib.NativeLib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import me.weishu.reflection.Reflection;

/**
 * description
 * <p>
 * on 2022/5/31
 */
public class App extends Application {

    public static final String TAG = "test_app";

    public static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        int unseal = Reflection.unseal(base);
        Log.v(TAG, "attachBaseContext reflect result " + unseal);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");

        context = getApplicationContext();

        hookBitmapState();
    }

    private void hookBitmapState() {
        try {
            Class<?> stateClz = Class.forName("android.graphics.drawable.BitmapDrawable$BitmapState");
            Field accessFlagField = Class.class.getDeclaredField("accessFlags");
            accessFlagField.setAccessible(true);
            int access = (int) accessFlagField.get(stateClz);
            int newAccess = access & ~Modifier.FINAL;
            newAccess = newAccess | Modifier.PUBLIC;
            Log.i("bitmap", "ori access " + Integer.toBinaryString(access)
                    + ",new access " + Integer.toBinaryString(newAccess));
            accessFlagField.set(stateClz, newAccess);

            Constructor[] constructors = stateClz.getDeclaredConstructors();

            NativeLib lib = new NativeLib();
            for (Constructor con : constructors) {
                boolean modify = lib.setConstructorAccess(Modifier.PUBLIC, con, Build.VERSION.SDK_INT);
                Log.v("bitmap", "setConstructorAccess " + modify + ", " + con.getParameterTypes());
            }
        } catch (Exception e) {
            Log.w("bitmap", "hookBitmapState", e);
        }
    }
}
