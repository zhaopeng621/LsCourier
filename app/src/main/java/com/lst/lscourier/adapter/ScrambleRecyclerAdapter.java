package com.lst.lscourier.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.utils.DistanceUtils;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.TimeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lst718-011 on 2017/7/26.
 */
public class ScrambleRecyclerAdapter extends XRecyclerView.Adapter<ScrambleRecyclerAdapter.MyHolder> {
    private View view;
    private List<OrderEntry> datas = new ArrayList<>();
    private Context mContext;
    private MyOnClickListener myClick;

    public ScrambleRecyclerAdapter(List<OrderEntry> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
    }

    public ScrambleRecyclerAdapter(List<OrderEntry> datas, Context context, MyOnClickListener myClick) {
        this.myClick = myClick;
        this.datas = datas;
        this.mContext = context;
    }

    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scramble_list_item, parent, false);
        return new MyHolder(view);
    }

    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_my_address.setText(SharePrefUtil.getString(mContext, "MyLocation", ""));
        long time = System.currentTimeMillis();
        String order_time = datas.get(position).getOrder_time();
        long mTime = 0;
        if (!order_time.equals("null")) {
            String data = TimeUtils.data(order_time);
            long aLong = Long.valueOf(data) * 1000;
            mTime = aLong - time;
            Log.e("time", String.valueOf(time));
            Log.e("Long", String.valueOf(aLong));
            Log.e("mTime", String.valueOf(mTime));
        }
        if (mTime > 0) {
            long days = mTime / (1000 * 60 * 60 * 24);
            long hours = (mTime - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (mTime - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            if (days != 0 && hours != 0) {
                holder.tv_clock.setText(days + "天" + hours + "小时" + minutes);
            } else if (hours != 0) {
                holder.tv_clock.setText(hours + "小时" + minutes);
            } else {
                holder.tv_clock.setText(minutes + "");
            }
        }
        float distance = DistanceUtils.calculateFrom(mContext,
                datas.get(position).getStart_lat(),
                datas.get(position).getStart_long());
        holder.tv_get_distance.setText(String.valueOf(distance) + "Km");
        holder.tv_get_address.setText(datas.get(position).getStart_address());
        holder.tv_send_distance.setText(datas.get(position).getDistance() + "Km");
        holder.tv_send_address.setText(datas.get(position).getExit_address());
        holder.tv_pay.setText("¥:" + datas.get(position).getMoney());
        float v = Float.valueOf(datas.get(position).getMoney()) / 5 * 4;
        float freight = new BigDecimal(v).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        holder.tv_freight.setText("¥:" + freight);
        holder.bt_scramble.setOnClickListener(myClick);
        holder.bt_scramble.setTag(position);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class MyHolder extends XRecyclerView.ViewHolder {
        private TextView tv_my_address, tv_clock, tv_get_distance, tv_get_address,
                tv_send_distance, tv_send_address, tv_pay, tv_freight;
        private Button bt_scramble;

        public MyHolder(View itemView) {
            super(itemView);
            tv_my_address = (TextView) itemView.findViewById(R.id.tv_my_address);
            tv_clock = (TextView) itemView.findViewById(R.id.tv_clock);
            tv_get_distance = (TextView) itemView.findViewById(R.id.tv_get_distance);
            tv_get_address = (TextView) itemView.findViewById(R.id.tv_get_address);
            tv_send_distance = (TextView) itemView.findViewById(R.id.tv_send_distance);
            tv_send_address = (TextView) itemView.findViewById(R.id.tv_send_address);
            tv_pay = (TextView) itemView.findViewById(R.id.tv_pay);
            tv_freight = (TextView) itemView.findViewById(R.id.tv_freight);
            bt_scramble = (Button) itemView.findViewById(R.id.bt_scramble);

        }
    }

    public static abstract class MyOnClickListener implements View.OnClickListener {

        public void onClick(View view) {
            myOnClick(view, (int) view.getTag());
        }

        public abstract void myOnClick(View view, int i);
    }
}