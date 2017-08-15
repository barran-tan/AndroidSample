package com.example.mdtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * AppBarLayout嵌套TabLayout
 *
 * Created by tanwei on 2016/8/18.
 */
public class TabInAppBarActivity extends AppCompatActivity {
    
    private TabLayout tabLayout;
    
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_in_app_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        initTabLayout();
        initViewPager();
        
        // 绑定到viewpager
        tabLayout.setupWithViewPager(viewPager);
    }

    // setupWithViewPager调用后会使用ViewPager的adapter的getPageTitle创建tab
    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("tab3"));
    }
    
    private void initViewPager() {
        
        final ArrayList<String> list = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            list.add("item " + i);
        }
        
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }
            
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
            
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
//                ListView listView = new ListView(TabInAppBarActivity.this);
//                listView.setAdapter(new ArrayAdapter<>(TabInAppBarActivity.this,
//                        android.R.layout.simple_list_item_1, android.R.id.text1, list));
//                container.addView(listView);
//                return listView;

                // listview目前不能支持Behavior的效果，即滑动listview不能引起Appbar滑动，只能滑动Appbar自己才能有效果
                // 使用RecyclerView替代
//                RecyclerView recyclerView = new RecyclerView(TabInAppBarActivity.this);
//                recyclerView.setLayoutManager(new LinearLayoutManager(TabInAppBarActivity.this));
//                recyclerView.setAdapter(new RecyclerView.Adapter() {
//                    @Override
//                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                        View v = LayoutInflater.from(TabInAppBarActivity.this).inflate(R.layout.item_text, parent, false);
//                        return new Holder(v);
//                    }
//
//                    @Override
//                    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//                        ((Holder)holder).setText("RecyclerView item " + position);
//                    }
//
//                    @Override
//                    public int getItemCount() {
//                        return list.size();
//                    }
//                });
//                container.addView(recyclerView);
//                return recyclerView;

                // 换成NestedScrollView替代
                View view = LayoutInflater.from(TabInAppBarActivity.this).inflate(R.layout.layout_nested_scroll_view, container, false);
                container.addView(view);
                return view;
            }
            
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
            
            @Override
            public CharSequence getPageTitle(int position) {
                return "getPageTitle " + position;
            }
        });

        // viewPager.setAdapter且tabLayout.setupWithViewPager后，可以使用以下代码设置纯icon的tab，但是不能重写getPageTitle
//        int[] tabDarwableIds = new int[] { R.drawable.bg_tab, R.drawable.bg_tab,
//                R.drawable.bg_tab };
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            tabLayout.getTabAt(i).setIcon(tabDarwableIds[i]);
//        }
    }

    class Holder extends RecyclerView.ViewHolder{

        private TextView tv;

        public Holder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        public void setText(String text){
            tv.setText(text);
        }
    }
}
