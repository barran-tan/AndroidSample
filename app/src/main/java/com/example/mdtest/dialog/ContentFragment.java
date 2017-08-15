package com.example.mdtest.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mdtest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * use DialogFragment
 *
 * Created by tanwei on 2017/7/26.
 */

public class ContentFragment extends DialogFragment {
    
    private FrameLayout frameLayoutContainer;
    
    private List<View> viewList;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        viewList = new ArrayList<>();
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        
        View content = inflater.inflate(R.layout.fragment_content, container, false);
        frameLayoutContainer = (FrameLayout) content;
        
        final LinearLayout childContainer = (LinearLayout) content
                .findViewById(R.id.dialog_content_list);
        
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_content_list_add:
                        TextView tv = new TextView(getActivity());
                        tv.setPadding(20, 15, 20, 15);
                        tv.setText("add item");
                        viewList.add(tv);
                        childContainer.addView(tv);
                        break;
                    
                    case R.id.dialog_content_list_delete:
                        int size = viewList.size();
                        if (size > 0) {
                            View view = viewList.get(size - 1);
                            childContainer.removeView(view);
                            viewList.remove(size - 1);
                        }
                        break;
                    
                    case R.id.dialog_content_list_detail:
                        frameLayoutContainer.removeAllViews();
                        getActivity().getLayoutInflater().inflate(
                                R.layout.dialog_content_detail, frameLayoutContainer);
                        break;
                    
                    case R.id.dialog_content_list_del_all:
                        if (viewList.size() > 0) {
                            childContainer.removeAllViews();
                        }
                        break;
                }
            }
        };
        content.findViewById(R.id.dialog_content_list_add)
                .setOnClickListener(clickListener);
        content.findViewById(R.id.dialog_content_list_delete)
                .setOnClickListener(clickListener);
        content.findViewById(R.id.dialog_content_list_detail)
                .setOnClickListener(clickListener);
        content.findViewById(R.id.dialog_content_list_del_all)
                .setOnClickListener(clickListener);
        
        return content;
    }
    
    private void switchContent() {
        
    }
}
