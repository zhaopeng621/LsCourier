package com.lst.lscourier.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.ScrambleRecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.DistanceUtils;
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

/**
 * Created by lst718-011 on 2017/7/25.
 */
public class ScrambleActivity extends MyAmapLocationActivity implements XRecyclerView.LoadingListener {

    private TextView tv_null;
    private XRecyclerView scramble_recyclerview;
    private List<OrderEntry> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scramble);
        initTitle();
        initView();
        getOrder();
    }

    private void getOrder() {
        String url = ParmasUrl.select_order;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", SharePrefUtil.getString(ScrambleActivity.this, "userid", ""));
        MyJsonObjectRequest stringRequest = new MyJsonObjectRequest(Request.Method.POST, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("login======", object.toString());
                    if (object.getString("code").equals("200")) {
                        JSONArray arr = object.getJSONArray("order_id");
                        for (int i = arr.length() - 1; i >-1; i--) {
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
                    }
                    if (datas.size() > 0) {
                        tv_null.setVisibility(View.GONE);
                        ScrambleRecyclerAdapter recyclerAdapter = new ScrambleRecyclerAdapter(datas, ScrambleActivity.this,
                                new ScrambleRecyclerAdapter.MyOnClickListener() {
                                    @Override
                                    public void myOnClick(View view, int i) {
                                        openPopupwin(view, i);
                                    }
                                });
                        scramble_recyclerview.setAdapter(recyclerAdapter);
                    } else {
                        tv_null.setVisibility(View.VISIBLE);
                        scramble_recyclerview.setVisibility(View.GONE);
                    }
                    Toast.makeText(ScrambleActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorHelper.getMessage(volleyError.getMessage(), ScrambleActivity.this);
            }
        });
        App.getHttpQueue().add(stringRequest);
    }

    private void initView() {
        scramble_recyclerview = (XRecyclerView) findViewById(R.id.scramble_recyclerview);
        tv_null = (TextView) findViewById(R.id.tv_null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ScrambleActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scramble_recyclerview.setLayoutManager(layoutManager);
        scramble_recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        scramble_recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        scramble_recyclerview.setLoadingListener(this);
    }

    @Override
    public void onRefresh() {
        scramble_recyclerview.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        scramble_recyclerview.loadMoreComplete();
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("我要抢单");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrambleActivity.this.finish();
            }
        });
    }

    //支付明细
    private void openPopupwin(View v, int position) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.scramble_pop, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.PopupAnimation);
        // 在底部显示
        window.showAtLocation(v,
                Gravity.CENTER_VERTICAL, 0, 0);
        // 这里检验popWindow里的button是否可以点击
        ImageView close_img = (ImageView) view.findViewById(R.id.img_clear);
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        TextView tv_weight = (TextView) view.findViewById(R.id.tv_weight);
        TextView tv_my_distance = (TextView) view.findViewById(R.id.tv_my_distance);
        TextView get_time = (TextView) view.findViewById(R.id.get_time);
        TextView get_address = (TextView) view.findViewById(R.id.get_address);
        TextView send_address = (TextView) view.findViewById(R.id.send_address);
        TextView order_number = (TextView) view.findViewById(R.id.order_number);
        TextView order_genre = (TextView) view.findViewById(R.id.order_genre);
        TextView tv_scramble = (TextView) view.findViewById(R.id.tv_scramble);

        tv_money.setText(datas.get(position).getMoney().toString());
        tv_distance.setText(datas.get(position).getDistance().toString());
        tv_weight.setText(datas.get(position).getOrder_weight().toString());
        get_time.setText(TimeUtils.timet(datas.get(position).getStart_time().toString()));
        get_address.setText(datas.get(position).getStart_address() + datas.get(position).getStart_xxaddress());
        send_address.setText(datas.get(position).getExit_address() + datas.get(position).getExit_xxaddress());
        order_number.setText(datas.get(position).getOrder_id().toString());
        order_genre.setText(datas.get(position).getOrder_type().toString());
        float distance = DistanceUtils.calculateFrom(ScrambleActivity.this,
                datas.get(position).getStart_lat(),
                datas.get(position).getStart_long());
        tv_my_distance.setText(String.valueOf(distance));
        tv_scramble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                scramble_recyclerview.refreshComplete();
            }
        });
    }
}