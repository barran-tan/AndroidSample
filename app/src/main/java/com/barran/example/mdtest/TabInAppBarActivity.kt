package com.barran.example.mdtest

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList

/**
 * AppBarLayout嵌套TabLayout
 *
 * Created by tanwei on 2016/8/18.
 */
class TabInAppBarActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null

    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_in_app_bar)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.viewpager)
        //        initTabLayout()
        initViewPager()

        // 绑定到viewpager
        tabLayout!!.setupWithViewPager(viewPager)
    }

    // setupWithViewPager调用后会使用ViewPager的adapter的getPageTitle创建tab
    private fun initTabLayout() {
        tabLayout!!.addTab(tabLayout!!.newTab().setText("tab1"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("tab2"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("tab3"))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initViewPager() {

        val list = ArrayList<String>(30)
        for (i in 0..29) {
            list.add("item $i")
        }

        viewPager!!.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return 3
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                //                ListView listView = new ListView(TabInAppBarActivity.this)
                //                listView.setAdapter(new ArrayAdapter<>(TabInAppBarActivity.this,
                //                        android.R.layout.simple_list_item_1, android.R.id.text1, list))
                //                container.addView(listView)
                //                return listView

                // listview目前不能支持Behavior的效果，即滑动listview不能引起Appbar滑动，只能滑动Appbar自己才能有效果
                // 使用RecyclerView替代
                //                RecyclerView recyclerView = new RecyclerView(TabInAppBarActivity.this)
                //                recyclerView.setLayoutManager(new LinearLayoutManager(TabInAppBarActivity.this))
                //                recyclerView.setAdapter(new RecyclerView.Adapter() {
                //                    @Override
                //                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //                        View v = LayoutInflater.from(TabInAppBarActivity.this).inflate(R.layout.item_text, parent, false)
                //                        return new Holder(v)
                //                    }
                //
                //                    @Override
                //                    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                //                        ((Holder)holder).setText("RecyclerView item " + position)
                //                    }
                //
                //                    @Override
                //                    public int getItemCount() {
                //                        return list.size()
                //                    }
                //                })
                //                container.addView(recyclerView)
                //                return recyclerView

                // 换成NestedScrollView替代
                val view = LayoutInflater.from(this@TabInAppBarActivity).inflate(R.layout.layout_nested_scroll_view, container, false)
                container.addView(view)
                return view
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return "getPageTitle $position"
            }
        }

        // viewPager.setAdapter且tabLayout.setupWithViewPager后，可以使用以下代码设置纯icon的tab，但是不能重写getPageTitle
        //        int[] tabDarwableIds = new int[] { R.drawable.bg_tab, R.drawable.bg_tab,
        //                R.drawable.bg_tab }
        //        for (int i = 0 i < tabLayout.getTabCount() i++) {
        //            tabLayout.getTabAt(i).setIcon(tabDarwableIds[i])
        //        }
    }

    internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tv: TextView = itemView.findViewById<View>(R.id.tv) as TextView

        fun setText(text: String) {
            tv.text = text
        }
    }
}
