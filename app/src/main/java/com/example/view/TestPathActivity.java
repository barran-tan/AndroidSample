package com.example.view;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.FrameLayout;

/**
 * test {@link android.graphics.Path Path}
 *
 * Created by tanwei on 2017/9/20.
 */

public class TestPathActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentView = new FrameLayout(this);
        setContentView(contentView);
        
        SearchView view = new SearchView(this);
        contentView.addView(view, new FrameLayout.LayoutParams(400, 400));
    }
}
