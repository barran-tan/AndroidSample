package com.barran.dynamicfeature;

import android.view.View;

public class TestR {
    public void setClickListener(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.test_container_dynamic:
                        break;
                }
            }
        });
    }
}
