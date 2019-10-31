package com.barran.example.mdtest;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

/**
 * 测试fab在CoordinatorLayout的表现&SnackBar
 *
 * Created by tanwei on 2016/9/7.
 */
public class FABInCoordinateLayoutActivity extends AppCompatActivity {
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
//                                Toast.makeText(FABInCoordinateLayoutActivity.this,
//                                        "you have reviewed the tip", Toast.LENGTH_SHORT)
//                                        .show();
                                finish();
                            }
                        }).show();
            }
        });
    }
}
