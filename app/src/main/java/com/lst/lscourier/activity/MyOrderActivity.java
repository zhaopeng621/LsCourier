package com.lst.lscourier.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.lst.lscourier.R;
import com.lst.lscourier.adapter.MyTabAdapter;
import com.lst.lscourier.fragment.AllOrdersFragment;
import com.lst.lscourier.fragment.OrderInProgressFragment;
import com.lst.lscourier.fragment.OrdersCanceledFragment;
import com.lst.lscourier.fragment.OrdersDoneFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单
 */
public class MyOrderActivity extends FragmentActivity implements View.OnClickListener {
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private List<String> list_title;
    private List<Fragment> fragments;
    private MyTabAdapter tabAdapter;
    private ImageView back_img;
//    private Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        initView();
        initLayout();

        back_img = (ImageView) findViewById(R.id.back_img);
//        searchButton = (Button) findViewById(R.id.search_button);
        back_img.setOnClickListener(this);
//        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                MyOrderActivity.this.finish();
                break;
//            case R.id.search_button:
//                Intent searchButton = new Intent();
//                searchButton.setClass(MyOrderActivity.this, SearchActivity.class);
//                startActivity(searchButton);
//                break;
        }
    }

    private void initView() {
        tableLayout = (TabLayout) findViewById(R.id.tab_order_title);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentManager = getSupportFragmentManager();
        viewPager.setOffscreenPageLimit(3);

    }

    private void initLayout() {
        list_title = new ArrayList<>();
        list_title.add("全部单");
        list_title.add("进行中");
        list_title.add("完成单");
        list_title.add("取消单");

        fragments = new ArrayList<>();
        fragments.add(new AllOrdersFragment());
        fragments.add(new OrderInProgressFragment());
        fragments.add(new OrdersDoneFragment());
        fragments.add(new OrdersCanceledFragment());

        //设置TabLayout的模式
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tableLayout.addTab(tableLayout.newTab().setText(list_title.get(0)));
        tableLayout.addTab(tableLayout.newTab().setText(list_title.get(1)));
        tableLayout.addTab(tableLayout.newTab().setText(list_title.get(2)));
        tableLayout.addTab(tableLayout.newTab().setText(list_title.get(3)));

        tabAdapter = new MyTabAdapter(fragmentManager, list_title, fragments);
        viewPager.setAdapter(tabAdapter);
        tableLayout.setupWithViewPager(viewPager);
    }


}
