package com.lst.lscourier.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.RecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.VolleyErrorHelper;
import com.lst.lscourier.view.LazyFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 所有订单
 */
@SuppressLint({"NewApi", "ValidFragment"})
public class AllOrdersFragment extends LazyFragment implements XRecyclerView.LoadingListener {
    private View view;
    private TextView tv_null;
    private XRecyclerView recyclerView;
    private List<OrderEntry> datas;
    private boolean isPrepared;
    public AllOrdersFragment() {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_all_orders_fragment, container, false);
        }
        isPrepared = true;
        lazyLoad();

        return view;
    }
    @Override
    protected void lazyLoad() {
        // TODO Auto-generated method stub
        if (!isPrepared || !isVisible) {
            return;
        }
        initView(view);
        getOrder();
    }

    private void initData() {
        Log.d("datas---", String.valueOf(datas.size()));
        if (datas.size() > 0) {
            tv_null.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
//            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(datas, getActivity(), new RecyclerAdapter.MyClickListener() {
//                @Override
//                public void myOnClick(int position, View v) {
//                    closeOrder(datas.get(position));
//                }
//            });
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(datas, getActivity());
            recyclerView.setAdapter(recyclerAdapter);
        } else {
            tv_null.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    private void closeOrder(OrderEntry bean) {

//        String url = ParmasUrl.shutorder;
        String url = "";
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("order_id", bean.getOrder_id());
        MyJsonObjectRequest stringRequest = new MyJsonObjectRequest(Request.Method.POST, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("closeOrder======", object.toString());
                    if (object.getString("code").equals("200")) {
                        getOrder();
                    }

                    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorHelper.getMessage(volleyError.getMessage(),getActivity());
            }
        });
        App.getHttpQueue().add(stringRequest);
    }
    private void initView(View view) {
        tv_null = (TextView) view.findViewById(R.id.tv_null);
        recyclerView = (XRecyclerView) view.findViewById(R.id.recycleview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setLoadingListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRefresh() {
        recyclerView.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        recyclerView.loadMoreComplete();
    }

    public void getOrder() {
        datas = new ArrayList<>();
        String url = ParmasUrl.select_order;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("id", SharePrefUtil.getString(getActivity(), "userid", ""));
        hashMap.put("order_type", "");
        MyJsonObjectRequest stringRequest = new MyJsonObjectRequest(Request.Method.POST, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("login======", object.toString());
                    if (object.getString("code").equals("200")) {
                        JSONArray arr = object.getJSONArray("data");
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
                    initData();
                    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorHelper.getMessage(volleyError.getMessage(), getActivity());
            }
        });
        App.getHttpQueue().add(stringRequest);
    }
}
