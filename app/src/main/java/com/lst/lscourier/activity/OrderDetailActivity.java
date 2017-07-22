package com.lst.lscourier.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lst.lscourier.R;
import com.lst.lscourier.adapter.MyTabAdapter;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.fragment.OrderDetailsFragment;
import com.lst.lscourier.fragment.OrderProgressFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * 订单详情
 */

public class OrderDetailActivity extends FragmentActivity implements View.OnClickListener {
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private List<String> list_title;
    private List<Fragment> fragments;
    private MyTabAdapter tabAdapter;
    private OrderEntry orderEntry;
    private Button order_information_return;
    private Button another_order;
    private LinearLayout complain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_information);

        initView();

        order_information_return = (Button) findViewById(R.id.order_information_return);
        another_order = (Button) findViewById(R.id.another_order);
        complain = (LinearLayout) findViewById(R.id.complain);
        order_information_return.setOnClickListener(this);
        another_order.setOnClickListener(this);
        complain.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_information_return:
                this.finish();
                break;
            case R.id.complain:
                Intent complainIntent = new Intent();
//                complainIntent.setClass(this, AnonymousComplaintActivity.class);
                startActivity(complainIntent);
                break;
            case R.id.another_order:
                Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
                intent.putExtra("OrderBean", orderEntry);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void initView() {
        tableLayout = (TabLayout) findViewById(R.id.tab_order_information_title);
        viewPager = (ViewPager) findViewById(R.id.viewPager_order_information);

        fragmentManager = getSupportFragmentManager();
        viewPager.setOffscreenPageLimit(1);

        fragmentManager = getSupportFragmentManager();
        viewPager.setOffscreenPageLimit(1);

        list_title = new ArrayList<>();
        list_title.add("订单进度");
        list_title.add("订单详情");
        fragments = new ArrayList<>();

        //TODO 获取数据
        orderEntry = (OrderEntry) getIntent().getExtras().get("data");

        OrderProgressFragment orderProgressFragment = new OrderProgressFragment();
        OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();

        //TODO 传递数据给fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", orderEntry);
        orderDetailsFragment.setArguments(bundle);
        orderProgressFragment.setArguments(bundle);

        fragments.add(orderProgressFragment);
        fragments.add(orderDetailsFragment);
//        orderDetailsFragment.setArguments(data);

        /*
        //不传递数据时，添加fragment的简单写法
        fragments.add(new OrderProgressFragment());
        fragments.add(new OrderDetailsFragment());*/

        //设置TabLayout的模式
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tableLayout.addTab(tableLayout.newTab().setText(list_title.get(0)));
        tableLayout.addTab(tableLayout.newTab().setText(list_title.get(1)));

        //TODO 调整tab下划线长度
        tableLayout.post(new Runnable() {
            @Override
            public void run() {
                setUnderline(tableLayout, 40, 40);
            }
        });
        tabAdapter = new MyTabAdapter(fragmentManager, list_title, fragments);
        viewPager.setAdapter(tabAdapter);
        tableLayout.setupWithViewPager(viewPager);

        tabAdapter = new MyTabAdapter(fragmentManager, list_title, fragments);
        viewPager.setAdapter(tabAdapter);
        tableLayout.setupWithViewPager(viewPager);

    }

    //TODO 调整tab下划线长度
    private void setUnderline(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip,
                Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip,
                Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
