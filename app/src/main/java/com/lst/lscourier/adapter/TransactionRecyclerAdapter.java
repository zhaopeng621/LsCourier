package com.lst.lscourier.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.bean.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class TransactionRecyclerAdapter extends XRecyclerView.Adapter<TransactionRecyclerAdapter.MyHolder> {
    private View view;
    private List<Transaction> datas = new ArrayList<>();
    private Context mContext;

    public TransactionRecyclerAdapter(List<Transaction> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactin_list_item, parent, false);
        return new TransactionRecyclerAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.left.setText( datas.get(position).getLeft());
        holder.right.setText(datas.get(position).getRight());
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView left, right;

        public MyHolder(View itemView) {
            super(itemView);
            left = (TextView) itemView.findViewById(R.id.left);
            right = (TextView) itemView.findViewById(R.id.right);
        }
    }
}
