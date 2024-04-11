package com.barran.sample.view;

import android.os.Bundle;
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
    }
}
