package com.lst.lscourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 我的钱包
 */

public class MyWalletActivity extends Activity implements View.OnClickListener {

    private Button wallet_return;
    private Button instant_recharge;
    private RelativeLayout apply_for_invoice;
    private RelativeLayout transaction_details;
    private RelativeLayout price_indication;
    private TextView tv_balance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mywallet);

        tv_balance = (TextView) findViewById(R.id.tv_balance);
        wallet_return = (Button) findViewById(R.id.wallet_return);
        instant_recharge = (Button) findViewById(R.id.instant_recharge);
        apply_for_invoice = (RelativeLayout) findViewById(R.id.apply_for_invoice);
        transaction_details = (RelativeLayout) findViewById(R.id.transaction_details);
        price_indication = (RelativeLayout) findViewById(R.id.price_indication);

        wallet_return.setOnClickListener(this);
        instant_recharge.setOnClickListener(this);
        apply_for_invoice.setOnClickListener(this);
        transaction_details.setOnClickListener(this);
        price_indication.setOnClickListener(this);
        getdata();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wallet_return:
                this.finish();
                break;
            case R.id.instant_recharge:
//                Intent instantRecharge = new Intent();
//                instantRecharge.setClass(this, RechargeActivity.class);
//                startActivity(instantRecharge);
                break;
            case R.id.apply_for_invoice:
//                Intent myInvoice = new Intent();
//                myInvoice.setClass(MyWalletActivity.this, MyInvoiceActivity.class);
//                startActivity(myInvoice);
                break;
            case R.id.transaction_details:
//                Intent transactionDetails = new Intent();
//                transactionDetails.setClass(this, TransactionDetailsActivity.class);
//                startActivity(transactionDetails);
                break;
            case R.id.price_indication:
                Intent priceInstruction = new Intent();
                priceInstruction.setClass(this, WebActivity.class);
                priceInstruction.putExtra("weburl", "file:///android_asset/price.htm");
                priceInstruction.putExtra("title", " 价格说明");
                startActivity(priceInstruction);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getdata() {
        String url = ParmasUrl.wallet;
        Map<String, String> map = new HashMap<>();
        map.put("id", SharePrefUtil.getString(MyWalletActivity.this, "userid", ""));
        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("login======", object.toString());
                    if (object.getString("code").equals("200")) {
                        JSONArray data = object.getJSONArray("data");
                        String balance = data.getJSONObject(0).getString("balance");
                        tv_balance.setText(balance);
                    }
                    Toast.makeText(MyWalletActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyWalletActivity.this, VolleyErrorHelper.getMessage(volleyError, MyWalletActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("walletPost");
        // 将请求添加到队列中
        App.getHttpQueue().add(jsonObjectRequest);
    }
}