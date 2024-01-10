package com.barran.mylibrary;

import android.util.Log;
import android.view.View;

public class TestR {
    public void setClickListener(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switch (v.getId()){
//                    case R.id.test_container:
//                        break;
//                }

                if (v.getId() == R.id.test_container) {
                    Log.v("test", "click test_container");
                }
            }
        });
    }
}
