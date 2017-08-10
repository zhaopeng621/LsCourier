package com.lst.lscourier.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.lst.lscourier.R;
import com.lst.lscourier.adapter.MyTabAdapter;
import com.lst.lscourier.fragment.IncomeDetailsFragment;
import com.lst.lscourier.fragment.WithdrawRecordFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 交易明细
 */

public class TransactionDetailsActivity extends FragmentActivity {
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;

    private List<String> list_title;
    private List<Fragment> fragments;

    private MyTabAdapter tabAdapter;

    private Button transaction_details_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.transaction_details);

        transaction_details_return = (Button) findViewById(R.id.transaction_details_return);
        transaction_details_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionDetailsActivity.this.finish();
            }
        });

        initView();
    }

    private void initView() {
        tableLayout = (TabLayout) findViewById(R.id.transaction_details_title);
        viewPager = (ViewPager) findViewById(R.id.transaction_details_viewPager);

        fragmentManager = getSupportFragmentManager();
        viewPager.setOffscreenPageLimit(1);

        list_title = new ArrayList<>();
        list_title.add("收入明细");
        list_title.add("提现记录");
        fragments = new ArrayList<>();
        fragments.add(new IncomeDetailsFragment());
        fragments.add(new WithdrawRecordFragment());
        //设置TabLayout的模式
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tableLayout.addTab(tableLayout.newTab().setText(list_title.get(0)));
        tableLayout.addTab(tableLayout.newTab().setText(list_title.get(1)));

        tabAdapter = new MyTabAdapter(fragmentManager, list_title, fragments);
        viewPager.setAdapter(tabAdapter);
        tableLayout.setupWithViewPager(viewPager);
    }
}
