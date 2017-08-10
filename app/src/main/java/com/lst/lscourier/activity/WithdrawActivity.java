package com.lst.lscourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.WithdrawBean;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.VolleyErrorHelper;
import com.lst.lscourier.view.BandCardEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 提现
 */

public class WithdrawActivity extends Activity implements View.OnClickListener {
    private Button confirm_withdraw;
    private TextView bank_name;
    private EditText et_number, et_name;
    private BandCardEditText bank_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.withdraw);
        initTitle();
        initView();
    }

    private void initView() {
        confirm_withdraw = (Button) findViewById(R.id.confirm_withdraw);

        bank_number = (BandCardEditText) findViewById(R.id.bank_number);
        et_name = (EditText) findViewById(R.id.et_name);
        et_number = (EditText) findViewById(R.id.et_number);
        bank_name = (TextView) findViewById(R.id.bank_name);
        bank_number.setBankCardListener(new BandCardEditText.BankCardListener() {
            @Override
            public void success(String name) {
                bank_name.setVisibility(View.VISIBLE);
                bank_name.setText(name);
            }

            @Override
            public void failure() {
                bank_name.setVisibility(View.VISIBLE);
                bank_name.setText("未查到所属银行，请校对卡号");
            }
        });
        confirm_withdraw.setOnClickListener(this);
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("提现");
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
            case R.id.confirm_withdraw:
                if (et_name.getText().toString().equals("")) {
                    Toast.makeText(WithdrawActivity.this, "请填写真实姓名",
                            Toast.LENGTH_SHORT).show();
                } else if (Float.valueOf(et_number.getText().toString()) <= 0.0f) {
                    Toast.makeText(WithdrawActivity.this, "请输入提现金额",
                            Toast.LENGTH_SHORT).show();
                } else if (bank_number.getText().toString().length() < 23) {
                    Toast.makeText(WithdrawActivity.this, "请输入银行卡号",
                            Toast.LENGTH_SHORT).show();
                } else {
//                    confirmWithdraw();
                    WithdrawBean withdrawBean = new WithdrawBean();
                    withdrawBean.setBankName(bank_name.getText().toString());
                    withdrawBean.setCardNumber(bank_number.getText().toString());
                    withdrawBean.setWithdrawNumber(et_number.getText().toString());
                    Intent intent = new Intent(WithdrawActivity.this, WithdrawResultActivity.class);
                    intent.putExtra("withdrawBean", withdrawBean);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    private void confirmWithdraw() {
        String url = ParmasUrl.register;
        Map<String, String> map = new HashMap<>();
//        map.put("username", username);
//        map.put("password", password);
        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String
                            code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        WithdrawBean withdrawBean = new WithdrawBean();
                        withdrawBean.setBankName(bank_name.getText().toString());
                        withdrawBean.setCardNumber(bank_number.getText().toString());
                        withdrawBean.setWithdrawNumber(et_number.getText().toString());
                        Intent intent = new Intent(WithdrawActivity.this, WithdrawResultActivity.class);
                        intent.putExtra("withdrawBean", withdrawBean);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(WithdrawActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(WithdrawActivity.this, VolleyErrorHelper.getMessage(volleyError, WithdrawActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("RechargePost");
        // 将请求添加到队列中
        App.getHttpQueue().add(jsonObjectRequest);

    }
}
