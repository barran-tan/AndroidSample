package com.example.mdtest.dialog;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.mdtest.R;

/**
 * 测试DialogFragment
 *
 * Created by tanwei on 2017/7/26.
 */

public class DialogActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_container);
        
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ContentFragment fragment = new ContentFragment();
        transaction.add(R.id.activity_empty_container, fragment);
        transaction.commit();
    }
}
