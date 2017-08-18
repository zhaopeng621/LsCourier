package com.lst.lscourier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.bean.WithdrawBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lst718-011 on 2017/7/26.
 */
public class WithdrawRecyclerAdapter extends XRecyclerView.Adapter<WithdrawRecyclerAdapter.MyHolder> {
    private View view;
    private List<WithdrawBean> datas = new ArrayList<>();
    private Context mContext;
    private MyOnClickListener myClick;

    public WithdrawRecyclerAdapter(List<WithdrawBean> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
    }

    public WithdrawRecyclerAdapter(List<WithdrawBean> datas, Context context, MyOnClickListener myClick) {
        this.myClick = myClick;
        this.datas = datas;
        this.mContext = context;
    }

    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_list_item, parent, false);
        return new MyHolder(view);
    }

    public void onBindViewHolder(MyHolder holder, int position) {

        holder.true_name.setText(datas.get(position).getTrueName());
        holder.bank_number.setText("尾号" + datas.get(position).getCardNumber().
                substring(datas.get(position).getCardNumber().length() - 4));
        holder.bank_name.setText(datas.get(position).getBankName());
        holder.withdraw_number.setText("¥" + datas.get(position).getWithdrawNumber());
        holder.withdraw_time.setText(datas.get(position).getOrderTime());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class MyHolder extends XRecyclerView.ViewHolder {
        private TextView true_name, bank_name, bank_number, withdraw_number,
                withdraw_time;

        public MyHolder(View itemView) {
            super(itemView);
            true_name = (TextView) itemView.findViewById(R.id.true_name);
            bank_name = (TextView) itemView.findViewById(R.id.bank_name);
            bank_number = (TextView) itemView.findViewById(R.id.bank_number);
            withdraw_number = (TextView) itemView.findViewById(R.id.withdraw_number);
            withdraw_time = (TextView) itemView.findViewById(R.id.withdraw_time);

        }
    }

    public static abstract class MyOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            myOnClick(view, (int) view.getTag());
        }

        public abstract void myOnClick(View view, int i);
    }
}