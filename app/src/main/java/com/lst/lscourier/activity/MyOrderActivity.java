package com.lst.lscourier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lst.lscourier.adapter.MyTabAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.fragment.AllOrdersFragment;
import com.lst.lscourier.fragment.OrderInProgressFragment;
import com.lst.lscourier.fragment.OrdersCanceledFragment;
import com.lst.lscourier.fragment.OrdersDoneFragment;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.VolleyErrorHelper;
import com.lst.lscourier.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的订单
 */
public class MyOrderActivity extends FragmentActivity implements View.OnClickListener {
    private TabLayout tableLayout;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private List<String> list_title;
    private List<Fragment> fragments;
    private List<OrderEntry> orderList;
    private List<OrderEntry> datas = new ArrayList<>();
    private List<OrderEntry> paiddatas = new ArrayList<>(); //进行中
    private List<OrderEntry> completeddatas = new ArrayList<>();//已完成
    private List<OrderEntry> canceleddatas = new ArrayList<>();//已取消
    private MyTabAdapter tabAdapter;
    private ImageView back_img;
    private Button searchButton;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    orderList = (List<OrderEntry>) msg.obj;
                    for (int i = 0; i < orderList.size(); i++) {
                        String order_status = orderList.get(i).getOrder_status();
                        if (order_status.equals("已付款")) {
                            paiddatas.add(orderList.get(i));
                            Log.d("order_status1", order_status);
                        } else if (order_status.equals("已完成")) {
                            completeddatas.add(orderList.get(i));
                            Log.d("order_status2", order_status);
                        } else if (order_status.equals("已取消")) {
                            canceleddatas.add(orderList.get(i));
                            Log.d("order_status3", order_status);
                        }
                    }
                    initLayout();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        getAllOrder();
        initView();

        back_img = (ImageView) findViewById(R.id.back_img);
        searchButton = (Button) findViewById(R.id.search_button);
        back_img.setOnClickListener(this);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                MyOrderActivity.this.finish();
                break;
            case R.id.search_button:
                Intent searchButton = new Intent();
                searchButton.setClass(MyOrderActivity.this, SearchActivity.class);
                startActivity(searchButton);
                break;
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
        fragments.add(new AllOrdersFragment(orderList));
        fragments.add(new OrderInProgressFragment(paiddatas));
        fragments.add(new OrdersDoneFragment(completeddatas));
        fragments.add(new OrdersCanceledFragment(canceleddatas));

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

    public void getAllOrder() {
        String url = ParmasUrl.select_order;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", SharePrefUtil.getString(MyOrderActivity.this, "userid", ""));
        MyJsonObjectRequest stringRequest = new MyJsonObjectRequest(Request.Method.POST, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("login======", object.toString());
                    if (object.getString("code").equals("200")) {
                        JSONArray arr = object.getJSONArray("order_id");
                        for (int i = 0; i < arr.length(); i++) {
                            final JSONObject obj = arr.getJSONObject(i);
                            OrderEntry orderBean = new OrderEntry();
                            orderBean.setId(obj.getString("id"));
                            orderBean.setOrder_id(obj.getString("order_id"));
                            orderBean.setStart_address(obj.getString("start_address"));
                            orderBean.setStart_xxaddress(obj.getString("start_xxaddress"));
                            orderBean.setExit_address(obj.getString("exit_address"));
                            orderBean.setExit_xxaddress(obj.getString("exit_xxaddress"));
                            orderBean.setOrder_type(obj.getString("order_type"));
                            orderBean.setOrder_weight(obj.getString("order_weight"));
                            orderBean.setStart_time(obj.getString("start_time"));
                            orderBean.setOrder_time(obj.getString("order_time"));
                            orderBean.setMoney(obj.getString("money"));
                            String order_status = obj.getString("order_status");
                            if (order_status.equals("0")) {
                                orderBean.setOrder_status("未付款");
                            } else if (order_status.equals("1")) {
                                orderBean.setOrder_status("已付款");
                            } else if (order_status.equals("2")) {
                                orderBean.setOrder_status("已完成");
                            } else if (order_status.equals("3")) {
                                orderBean.setOrder_status("已取消");
                            }
                            orderBean.setStart_name(obj.getString("start_name"));
                            orderBean.setExit_name(obj.getString("exit_name"));
                            orderBean.setStart_phone(obj.getString("start_phone"));
                            orderBean.setExit_phone(obj.getString("exit_phone"));
                            orderBean.setDistance(obj.getString("distance"));
                            if (obj.getString("message").equals("null")) {
                                orderBean.setMessage("");
                            } else {
                                orderBean.setMessage(obj.getString("message"));
                            }
                            if (obj.getString("pay_type").equals("null")) {
                                orderBean.setPay_type("");
                            } else {
                                orderBean.setPay_type(obj.getString("pay_type"));
                            }
                            if (obj.getString("deliveryman_id").equals("null")) {
                                orderBean.setDeliveryman_id("");
                            } else {
                                orderBean.setDeliveryman_id(obj.getString("deliveryman_id"));
                            }
                            orderBean.setR_time(obj.getString("r_time"));
                            orderBean.setStart_long(obj.getString("start_long"));
                            orderBean.setStart_lat(obj.getString("start_lat"));
                            orderBean.setExit_long(obj.getString("exit_long"));
                            orderBean.setExit_lat(obj.getString("exit_lat"));
                            datas.add(orderBean);
                        }
                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        msg.obj = datas;
                        handler.sendMessage(msg);

                    }
                    Toast.makeText(MyOrderActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorHelper.getMessage(volleyError.getMessage(), MyOrderActivity.this);
            }
        });
        App.getHttpQueue().add(stringRequest);
    }
}
