package com.lst.lscourier.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.TOrderRecyclerAdapter;
import com.lst.lscourier.bean.OrderEntry;

import java.util.ArrayList;
import java.util.List;


/**
 * 所有订单
 */
@SuppressLint({"NewApi", "ValidFragment"})
public class AllOrdersFragment extends Fragment implements XRecyclerView.LoadingListener {
    private View view;
    private TextView tv_null;
    private XRecyclerView recyclerView;
    private List<OrderEntry> datas = new ArrayList<>();

    public AllOrdersFragment() {
        super();
    }

    public AllOrdersFragment(List<OrderEntry> datas) {
        super();
        this.datas = datas;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.main_all_orders_fragment, container, false);
        }
        initView(view);
        return view;
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
        if (datas.size() > 0) {
            tv_null.setVisibility(View.GONE);
            TOrderRecyclerAdapter recyclerAdapter = new TOrderRecyclerAdapter(datas, getActivity());
            recyclerView.setAdapter(recyclerAdapter);
        } else {
            tv_null.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
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


}
