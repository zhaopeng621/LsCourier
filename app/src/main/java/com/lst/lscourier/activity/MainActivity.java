package com.lst.lscourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.TOrderRecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.bean.UserBean;
import com.lst.lscourier.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity
        implements View.OnClickListener,
        XRecyclerView.LoadingListener {

    private TextView user_phone, main_time, tv_number, tv_money, tv_null;
    private Button bt_go_scramble;
    private XRecyclerView recyclerview;
    private UserBean userBean;
    private List<OrderEntry> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
    }

    private void initView() {
        tv_null = (TextView) findViewById(R.id.tv_null);
        main_time = (TextView) findViewById(R.id.main_time);
        main_time.setText(TimeUtils.getTime());
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_money = (TextView) findViewById(R.id.tv_money);
        bt_go_scramble = (Button) findViewById(R.id.bt_go_scramble);
        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
        bt_go_scramble.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerview.setLoadingListener(this);
        for (int i = 0; i < 10; i++) {
            OrderEntry orderEntry = new OrderEntry();
            orderEntry.setStart_address("幸福大厦");
            orderEntry.setStart_xxaddress("东三环北路3号");
            orderEntry.setExit_address("牛王庙9号小区");
            orderEntry.setExit_xxaddress("新源街13号");
            orderEntry.setMoney("35");
            orderEntry.setOrder_weight("5");
            orderEntry.setDistance("12");
            orderEntry.setOrder_id("HD15452145458");
            orderEntry.setOrder_time("7-25 00:00");
            orderEntry.setOrder_status("待取件 ");
            datas.add(orderEntry);
        }
        if (datas.size() > 0) {
            tv_null.setVisibility(View.GONE);
            TOrderRecyclerAdapter recyclerAdapter = new TOrderRecyclerAdapter(datas, MainActivity.this);
            recyclerview.setAdapter(recyclerAdapter);
        } else {
            tv_null.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }
    }

    public void initToolbar() {
        ImageView toolbar_right = (ImageView) findViewById(R.id.toolbar_right);
        toolbar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MySendFlashActivity.class));
                App.getInstance().addActivity(MainActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        App.getInstance().onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_go_scramble:
                startActivity(new Intent(MainActivity.this, ScrambleActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:

                break;
        }
    }

    @Override
    public void onRefresh() {
        recyclerview.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        recyclerview.loadMoreComplete();
    }
}
