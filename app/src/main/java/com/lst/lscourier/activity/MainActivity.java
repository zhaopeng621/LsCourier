package com.lst.lscourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.TOrderRecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.bean.UserBean;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.TimeUtils;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity
        implements View.OnClickListener,
        XRecyclerView.LoadingListener {

    private TextView user_phone, main_time, tv_number, tv_money, tv_null;
    private Button bt_go_scramble;
    private XRecyclerView recyclerview;
    private LinearLayout refresh;
    private UserBean userBean = new UserBean();
    private List<OrderEntry> datas = new ArrayList<>();
    private List<OrderEntry> orderList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    orderList = (List<OrderEntry>) msg.obj;
                    if (orderList.size() > 0) {
                        tv_null.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.VISIBLE);
                        TOrderRecyclerAdapter recyclerAdapter = new TOrderRecyclerAdapter(datas, MainActivity.this);
                        recyclerview.setAdapter(recyclerAdapter);
                    } else {
                        tv_null.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
        getTorder();
    }

    private void getTorder() {
        String url = ParmasUrl.today_order;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("deliveryman_id", SharePrefUtil.getString(MainActivity.this, "userid", ""));
        MyJsonObjectRequest stringRequest = new MyJsonObjectRequest(Request.Method.POST, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("getTorder======", object.toString());
                    if (object.getString("code").equals("200")) {
                        JSONArray arr = object.getJSONArray("data");
                        datas.clear();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject json = arr.getJSONObject(i);
                            final JSONObject obj = json.getJSONObject("order");
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
                    }
                    Message msg = handler.obtainMessage();
                    msg.what = 0;
                    msg.obj = datas;
                    handler.sendMessage(msg);
                    Toast.makeText(MainActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorHelper.getMessage(volleyError.getMessage(), MainActivity.this);
            }
        });
        App.getHttpQueue().add(stringRequest);
    }

    private void initView() {
        userBean = (UserBean) SharePrefUtil.getObj(MainActivity.this, "User");
        tv_null = (TextView) findViewById(R.id.tv_null);
        main_time = (TextView) findViewById(R.id.main_time);
        main_time.setText(TimeUtils.getTime());
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_money = (TextView) findViewById(R.id.tv_money);
        bt_go_scramble = (Button) findViewById(R.id.bt_go_scramble);
        refresh = (LinearLayout) findViewById(R.id.refresh);
        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
        refresh.setOnClickListener(this);
        bt_go_scramble.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerview.setLoadingListener(this);
        tv_number.setText(userBean.getExit_order());
        tv_money.setText(userBean.getToday_money());

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
                Intent intent = new Intent(MainActivity.this, ScrambleActivity.class);
                startActivityForResult(intent,100);
                break;
            case R.id.refresh:
                getTorder();
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
                getTorder();
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
