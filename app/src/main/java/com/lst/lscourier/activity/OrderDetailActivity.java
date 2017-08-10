package com.lst.lscourier.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.lst.lscourier.R;
import com.lst.lscourier.bean.OrderEntry;


/**
 * 订单详情
 */

public class OrderDetailActivity extends FragmentActivity {
    private OrderEntry orderEntry;
    private TextView order_details_order_status, order_details_order_number, order_details_order_price,
            order_details_order_taking_address, order_details_service_address, order_total_price,
            order_details_order_distance, order_details_order_weight, order_taking_address_user,
            order_taking_address_user_phone_number, service_address_user, service_address_user_phone_number,
            order_taking_time, item_information, remark_information, payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_details);
        initTitle();
        initView();
        initData();
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("订单详情");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailActivity.this.finish();
            }
        });
    }

    private void initView() {
        //TODO 获取数据
        orderEntry = (OrderEntry) getIntent().getExtras().get("data");
        order_details_order_status = (TextView) findViewById(R.id.order_details_order_status);
        order_details_order_number = (TextView) findViewById(R.id.order_details_order_number);
        order_details_order_price = (TextView) findViewById(R.id.order_details_order_price);
        order_details_order_distance = (TextView) findViewById(R.id.order_details_order_distance);
        order_details_order_taking_address = (TextView) findViewById(R.id.order_details_order_taking_address);
        order_details_service_address = (TextView) findViewById(R.id.order_details_service_address);
        order_total_price = (TextView) findViewById(R.id.order_total_price);
        order_details_order_weight = (TextView) findViewById(R.id.order_details_order_weight);
        order_taking_address_user = (TextView) findViewById(R.id.order_taking_address_user);
        order_taking_address_user_phone_number = (TextView) findViewById(R.id.order_taking_address_user_phone_number);
        service_address_user = (TextView) findViewById(R.id.service_address_user);
        service_address_user_phone_number = (TextView) findViewById(R.id.service_address_user_phone_number);
        order_taking_time = (TextView) findViewById(R.id.order_taking_time);
        item_information = (TextView) findViewById(R.id.item_information);
        remark_information = (TextView) findViewById(R.id.remark_information);
        payment = (TextView) findViewById(R.id.payment);

    }

    private void initData() {
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
        order_taking_time.setText(orderEntry.getStart_time());
        item_information.setText(orderEntry.getOrder_type());
        remark_information.setText(orderEntry.getMessage());
        payment.setText(orderEntry.getPay_type());
    }
}
