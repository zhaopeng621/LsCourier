package com.lst.lscourier.fragment;

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
import com.lst.lscourier.adapter.TransactionRecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.Transaction;
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
 * 收入明细
 */
public class IncomeDetailsFragment extends LazyFragment implements XRecyclerView.LoadingListener {
    private View view;
    private TextView tv_null;
    private XRecyclerView recyclerview;
    private boolean isPrepared;
    private List<Transaction> datas;

    public IncomeDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.income_details_fragment, container, false);
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
        getDatas();
    }

    private void initView(View v) {
        tv_null = (TextView) v.findViewById(R.id.tv_consumer_details_no);
        recyclerview = (XRecyclerView) v.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerview.setLoadingListener(this);
    }

    //收入明细
    public void getDatas() {
        String url = ParmasUrl.selectChange;
        Map<String, String> map = new HashMap<>();
        map.put("id", SharePrefUtil.getString(getActivity(), "userid", ""));
        map.put("type", "1");
        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("selectChange===", jsonObject.toString());
                    if (jsonObject.getString("code").equals("200")) {
                        datas = new ArrayList<>();
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            Transaction transaction = new Transaction();
                            transaction.setLeft(data.getJSONObject(i).getString("money"));
                            transaction.setRight(data.getJSONObject(i).getString("time"));
                            datas.add(transaction);
                        }
                    }
                    if (datas.size() > 0) {
                        tv_null.setVisibility(View.GONE);
                        TransactionRecyclerAdapter recyclerAdapter = new TransactionRecyclerAdapter(datas, getActivity());
                        recyclerview.setAdapter(recyclerAdapter);
                    } else {
                        tv_null.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.GONE);
                    }
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
        App.getHttpQueue().add(myJsonObjectRequest);

    }

    @Override
    public void onStart() {
        super.onStart();
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
