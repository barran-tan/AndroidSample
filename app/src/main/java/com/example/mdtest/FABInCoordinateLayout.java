package com.example.mdtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 测试fab在CoordinatorLayout的表现&SnackBar
 *
 * Created by tanwei on 2016/9/7.
 */
public class FABInCoordinateLayout extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_in_coordinate_layout);
        
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "you have clicked fab", Snackbar.LENGTH_LONG)
                        .setAction("i know", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(FABInCoordinateLayout.this,
//                                        "you have reviewed the tip", Toast.LENGTH_SHORT)
//                                        .show();
                                finish();
                            }
                        }).show();
            }
        });
    }
}
