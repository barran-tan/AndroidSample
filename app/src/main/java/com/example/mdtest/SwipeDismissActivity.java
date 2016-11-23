package com.example.mdtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 *
 *
 * Created by tanwei on 2016/10/31.
 */

public class SwipeDismissActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_swipe_dismiss);

        TextView tv = (TextView) findViewById(R.id.swipe_dismiss_text);

        SwipeDismissBehavior<View> behavior = new SwipeDismissBehavior<>();
        behavior.setStartAlphaSwipeDistance(0.1f);
        behavior.setEndAlphaSwipeDistance(0.6f);
//        behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
        behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        behavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
            @Override
            public void onDismiss(View view) {
                Log.v("swipe", "onDismiss");
                view.setVisibility(View.GONE);
            }

            @Override
            public void onDragStateChanged(int state) {
                Log.v("swipe", "onDragStateChanged  " + state);
            }
        });

        ((CoordinatorLayout.LayoutParams)tv.getLayoutParams()).setBehavior(behavior);
    }
}
