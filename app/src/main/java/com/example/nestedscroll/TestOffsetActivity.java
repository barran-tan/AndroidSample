package com.example.nestedscroll;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.example.mdtest.R;

/**
 * view的移动
 * 
 * Created by tanwei on 2018/5/10.
 */

public class TestOffsetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_test_offset);
    }

    public void scrollBy(View v) {
        v.scrollBy(20, -20);
    }

    public void offset(View v) {
        offsetView(v, 20, -20);
    }
    
    private void offsetView(View v, int x, int y) {
        v.offsetLeftAndRight(x);
        v.offsetTopAndBottom(y);
    }
    
    public void layout(View v) {
        layoutView(v, 20, -20);
    }
    
    private void layoutView(View v, int x, int y) {
        v.layout(v.getLeft() + x, v.getTop() + y, v.getRight() + x, v.getBottom() + y);
    }
    
    public void scrollTo(View v) {
        v.scrollTo(20, -20);
    }
}
