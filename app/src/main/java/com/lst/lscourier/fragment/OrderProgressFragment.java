package com.lst.lscourier.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.OrderProgressAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.bean.OrderProgressEntry;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单进度
 */

public class OrderProgressFragment extends Fragment {

    private RecyclerView order_progress_lv;
    private OrderProgressAdapter orderProgressAdapter;
    private List<OrderProgressEntry> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_progress_fragment, container, false);
        order_progress_lv = (RecyclerView) view.findViewById(R.id.order_progress_lv);
        //TODO 获取数据
        OrderEntry orderEntry = (OrderEntry) getArguments().get("data");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        order_progress_lv.setLayoutManager(linearLayoutManager);
        getOrderLog(orderEntry);

        return view;
    }

    public void getOrderLog(OrderEntry orderEntry) {
        String url = ParmasUrl.order_log;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("order_id", orderEntry.getOrder_id());
        MyJsonObjectRequest stringRequest = new MyJsonObjectRequest(Request.Method.POST, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    if (object.getString("code").equals("200")) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            OrderProgressEntry orderProgressEntry = new OrderProgressEntry();
                            orderProgressEntry.setMessage(jsonObject.getString("message"));
                            orderProgressEntry.setTitle(jsonObject.getString("order_id"));
                            orderProgressEntry.setTime(jsonObject.getString("time"));
                            list.add(orderProgressEntry);
                        }
                        orderProgressAdapter = new OrderProgressAdapter(getActivity(), list);
                        order_progress_lv.setAdapter(orderProgressAdapter);

                    }
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
