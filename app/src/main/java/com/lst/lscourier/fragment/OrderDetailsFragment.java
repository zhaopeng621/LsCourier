package com.lst.lscourier.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lst.lscourier.R;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.utils.TimeUtils;


/**
 * Created by lst719 on 2017/6/27.
 */

public class OrderDetailsFragment extends Fragment {
    private View view;

    private TextView order_details_order_status, order_details_order_number, order_details_order_price, order_details_order_taking_address,
            order_details_service_address, order_total_price,order_details_order_distance,order_details_order_weight,order_taking_address_user
           ,order_taking_address_user_phone_number,service_address_user ,service_address_user_phone_number,order_taking_time,item_information
            ,remark_information,payment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.order_details_fragment, container, false);

        order_details_order_status = (TextView) view.findViewById(R.id.order_details_order_status);
        order_details_order_number = (TextView) view.findViewById(R.id.order_details_order_number);
        order_details_order_price = (TextView) view.findViewById(R.id.order_details_order_price);
        order_details_order_distance = (TextView) view.findViewById(R.id.order_details_order_distance);
        order_details_order_taking_address = (TextView) view.findViewById(R.id.order_details_order_taking_address);
        order_details_service_address = (TextView) view.findViewById(R.id.order_details_service_address);
        order_total_price = (TextView) view.findViewById(R.id.order_total_price);
        order_details_order_weight = (TextView) view.findViewById(R.id.order_details_order_weight);
        order_taking_address_user = (TextView) view.findViewById(R.id.order_taking_address_user);
        order_taking_address_user_phone_number = (TextView) view.findViewById(R.id.order_taking_address_user_phone_number);
        service_address_user = (TextView) view.findViewById(R.id.service_address_user);
        service_address_user_phone_number = (TextView) view.findViewById(R.id.service_address_user_phone_number);
        order_taking_time = (TextView) view.findViewById(R.id.order_taking_time);
        item_information = (TextView) view.findViewById(R.id.item_information);
        remark_information = (TextView) view.findViewById(R.id.remark_information);
        payment = (TextView) view.findViewById(R.id.payment);

        //TODO 获取数据
        OrderEntry orderEntry = (OrderEntry) getArguments().get("data");

        order_details_order_status.setText(orderEntry.getOrder_status());
        order_details_order_number.setText(orderEntry.getOrder_id());
        order_details_order_price.setText(orderEntry.getMoney());
        order_details_order_taking_address.setText(orderEntry.getStart_address() + orderEntry.getStart_xxaddress());
        order_details_service_address.setText(orderEntry.getExit_address() + orderEntry.getExit_xxaddress());
        order_total_price.setText(orderEntry.getMoney());
        order_details_order_distance.setText(orderEntry.getDistance());
        order_details_order_weight.setText(orderEntry.getOrder_weight());
        order_taking_address_user.setText(orderEntry.getStart_name());
        order_taking_address_user_phone_number.setText(orderEntry.getStart_phone());
        service_address_user.setText(orderEntry.getExit_name());
        service_address_user_phone_number.setText(orderEntry.getExit_phone());
        order_taking_time.setText(TimeUtils.timet(orderEntry.getStart_time()));
        item_information.setText(orderEntry.getOrder_type());
        remark_information.setText(orderEntry.getMessage());
        payment.setText(orderEntry.getPay_type());

        return view;
    }
}
