package com.lst.lscourier.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lst.lscourier.R;
import com.lst.lscourier.bean.OrderProgressEntry;

import java.util.List;


/**
 * Created by Administrator on 2017/6/28.
 */

public class OrderProgressAdapter extends RecyclerView.Adapter<OrderProgressAdapter.MyViewHolder> {
    private Context mContext;
    private List<OrderProgressEntry> list;

    public OrderProgressAdapter(Context mContext, List<OrderProgressEntry> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_progress_listitem, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == 0) {
            holder.tvTop_line.setVisibility(View.INVISIBLE);
            holder.order_closed.setTextColor(mContext.getResources().getColor(R.color.light_blue));
            holder.tvDot.setBackgroundResource(R.color.light_blue);
        } else if (position == list.size()-1) {
            holder.tvBottom_line.setVisibility(View.INVISIBLE);
        }
        if (list.size()-1==position) {
            holder.tvDot.setBackgroundColor(mContext.getResources().getColor(R.color.light_blue));
            holder.order_closed.setTextColor(mContext.getResources().getColor(R
                    .color.light_blue));
        }
        holder.order_closed.setText(list.get(position).getTitle());
        holder.order_message.setText(list.get(position).getMessage());
        holder.order_run_time.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTop_line, tvDot, tvBottom_line, order_closed, order_message, order_run_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTop_line = (TextView) itemView.findViewById(R.id.tvTop_line);
            tvDot = (TextView) itemView.findViewById(R.id.tvDot);
            tvBottom_line = (TextView) itemView.findViewById(R.id.tvBottom_line);
            order_closed = (TextView) itemView.findViewById(R.id.order_closed);
            order_message = (TextView) itemView.findViewById(R.id.order_message);
            order_run_time = (TextView) itemView.findViewById(R.id.order_run_time);
        }
    }
}
