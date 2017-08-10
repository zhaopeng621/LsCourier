package com.lst.lscourier.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lst.lscourier.R;
import com.lst.lscourier.bean.WithdrawBean;
import com.lst.lscourier.utils.CalendarUtil;


/**
 * 提现
 */

public class WithdrawResultActivity extends Activity implements View.OnClickListener {
    private Button confirm;
    private TextView bank_name, tv_number, card_number, tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.withdraw_result);
        initTitle();
        initView();
    }

    private void initView() {
        WithdrawBean withdrawBean = (WithdrawBean) getIntent().getExtras().get("withdrawBean");

        confirm = (Button) findViewById(R.id.confirm);
        bank_name = (TextView) findViewById(R.id.bank_name);
        tv_number = (TextView) findViewById(R.id.tv_number);
        card_number = (TextView) findViewById(R.id.card_number);
        tv_time = (TextView) findViewById(R.id.tv_time);
        String bankName = withdrawBean.getBankName();
        String substring = bankName.substring(0, bankName.indexOf("."));
        bank_name.setText(substring);
        tv_number.setText("¥："+withdrawBean.getWithdrawNumber());
        card_number.setText("尾号："+withdrawBean.getCardNumber().substring(withdrawBean.getCardNumber().length() - 4));
        tv_time.setText(new CalendarUtil().getNextMonday());
        confirm.setOnClickListener(this);
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("提现结果");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                finish();
                break;
        }
    }
}
