package com.lst.lscourier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.bean.DisciplinaryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lst718-011 on 2017/7/26.
 */
public class DisciplinaryRecyclerAdapter extends XRecyclerView.Adapter<DisciplinaryRecyclerAdapter.MyHolder> {
    private View view;
    private List<DisciplinaryBean> datas = new ArrayList<>();
    private Context mContext;
    private MyOnClickListener myClick;

    public DisciplinaryRecyclerAdapter(List<DisciplinaryBean> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
    }

    public DisciplinaryRecyclerAdapter(List<DisciplinaryBean> datas, Context context, MyOnClickListener myClick) {
        this.myClick = myClick;
        this.datas = datas;
        this.mContext = context;
    }

    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disciplinary_list_item, parent, false);
        return new MyHolder(view);
    }

    public void onBindViewHolder(MyHolder holder, int position) {
        if (datas.get(position).getDisciplinaryType().equals("2")) {
            holder.disciplinary_type.setText("奖励");
            holder.disciplinary_type.setTextColor(mContext.getResources().getColor(R.color.mGreen));
        } else if (datas.get(position).getDisciplinaryType().equals("3")) {
            holder.disciplinary_type.setText("惩罚");
            holder.disciplinary_type.setTextColor(mContext.getResources().getColor(R.color.tomato));
        }
        holder.disciplinary_msg.setText(datas.get(position).getDisciplinaryMsg());
        holder.disciplinary_money.setText("¥" + datas.get(position).getDisciplinaryMoney());
        holder.disciplinary_time.setText(datas.get(position).getDisciplinaryTime());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class MyHolder extends XRecyclerView.ViewHolder {
        private TextView disciplinary_type, disciplinary_money, disciplinary_msg, disciplinary_time;

        public MyHolder(View itemView) {
            super(itemView);
            disciplinary_type = (TextView) itemView.findViewById(R.id.disciplinary_type);
            disciplinary_money = (TextView) itemView.findViewById(R.id.disciplinary_money);
            disciplinary_msg = (TextView) itemView.findViewById(R.id.disciplinary_msg);
            disciplinary_time = (TextView) itemView.findViewById(R.id.disciplinary_time);

        }
    }

    public static abstract class MyOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            myOnClick(view, (int) view.getTag());
        }

        public abstract void myOnClick(View view, int i);
    }
}