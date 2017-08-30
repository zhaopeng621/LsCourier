package com.lst.lscourier.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.lst.lscourier.adapter.WithdrawRecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.WithdrawBean;
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
 * 提现记录
 */

public class WithdrawRecordFragment extends LazyFragment implements XRecyclerView.LoadingListener {
    private View view;
    private TextView tv_null;
    private XRecyclerView recyclerview;
    private List<WithdrawBean> datas ;
    private boolean isPrepared;
    public WithdrawRecordFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.withdraw_record_fragment, container, false);
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

    private void initView(View view) {
        tv_null = (TextView) view.findViewById(R.id.tv_recharge_record_no);
        recyclerview = (XRecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerview.setLoadingListener(this);
    }

    public void getDatas() {
        datas = new ArrayList<>();
        String url = ParmasUrl.select_with;
        Map<String, String> map = new HashMap<>();
        map.put("deliveryman_id", SharePrefUtil.getString(getActivity(), "userid", ""));
        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("withdrawlist===", jsonObject.toString());
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            WithdrawBean withdrawBean = new WithdrawBean();
                            withdrawBean.setTrueName(obj.getString("name"));
                            withdrawBean.setBankName(obj.getString("bank_type"));
                            withdrawBean.setWithdrawNumber(obj.getString("money"));
                            withdrawBean.setCardNumber(obj.getString("bank_card"));
                            withdrawBean.setOrderTime(obj.getString("time"));
                            datas.add(withdrawBean);
                        }
                    }
                    if (datas.size() > 0) {
                        tv_null.setVisibility(View.GONE);
                        WithdrawRecyclerAdapter recyclerAdapter = new WithdrawRecyclerAdapter(datas, getActivity());
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

