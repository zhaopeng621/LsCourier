package com.lst.lscourier.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.activity.OrderDetailActivity;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class RecyclerAdapter extends XRecyclerView.Adapter<RecyclerAdapter.MyHolder> implements View.OnClickListener {
    private View view;
    private List<OrderEntry> datas = new ArrayList<>();
    private Context mContext;

    public RecyclerAdapter(List<OrderEntry> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        view.setOnClickListener(this);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.order_number.setText("订单号:"+datas.get(position).getOrder_id());
        holder.order_status.setText(datas.get(position).getOrder_status());
        holder.order_price.setText("¥:"+datas.get(position).getMoney());
        holder.ordertaking_address.setText(datas.get(position).getStart_address() + datas.get(position).getStart_xxaddress());
        holder.service_address.setText(datas.get(position).getExit_address() + datas.get(position).getExit_xxaddress());
        String timet = TimeUtils.timet(datas.get(position).getStart_time());
        holder.ordertime.setText(timet);
        holder.list_item_ll.setTag(position);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onClick(View view) {
        Intent orderDtailIntent = new Intent();
        orderDtailIntent.setClass(mContext, OrderDetailActivity.class);

        //TODO 传递数据
        orderDtailIntent.putExtra("data", datas.get((int) view.getTag()));

        mContext.startActivity(orderDtailIntent);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView order_number, order_status, order_price, ordertaking_address,
                service_address, ordertime;
        private LinearLayout list_item_ll;

        public MyHolder(View itemView) {
            super(itemView);
            order_number = (TextView) itemView.findViewById(R.id.order_number);
            order_status = (TextView) itemView.findViewById(R.id.order_status);
            order_price = (TextView) itemView.findViewById(R.id.order_price);
            ordertaking_address = (TextView) itemView.findViewById(R.id.ordertaking_address);
            service_address = (TextView) itemView.findViewById(R.id.service_address);
            ordertime = (TextView) itemView.findViewById(R.id.ordertime);
            list_item_ll = (LinearLayout) itemView.findViewById(R.id.list_item_ll);

        }
    }
}
