package com.lst.lscourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.ClassPathResource;
import com.lst.lscourier.utils.CountDownButtonHelper;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegistActivity extends Activity implements View.OnClickListener {
    private Button bn_regist;
    private String username, code, password;
    private TextView remark;
    private Button getverificationcode;
    private EditText et_code, et_password, et_username;
    private String type;
    private CountDownButtonHelper helper;
    private CheckBox checkbox;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String newUsernameCode = (String) msg.obj;
                    SharePrefUtil.saveString(RegistActivity.this, "newUsername", et_username.getText().toString());
                    SharePrefUtil.saveString(RegistActivity.this, "newUsernameCode", newUsernameCode);

                    break;
                case 1:
                    Intent intent =new Intent().setClass(RegistActivity.this,DataFillingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        type = getIntent().getStringExtra("type");
        initTitle();
        initView();
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        if (type.equals("0")) {
            title_text.setText("注册");
        } else if (type.equals("1")) {
            title_text.setText("重置密码");
        }
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistActivity.this.finish();
            }
        });
    }

    private void initView() {
        bn_regist = (Button) findViewById(R.id.bn_regist);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_code = (EditText) findViewById(R.id.et_code);
        getverificationcode = (Button) findViewById(R.id.getverificationcode);
        LinearLayout ll_remark = (LinearLayout) findViewById(R.id.ll_remark);
        remark = (TextView) findViewById(R.id.remark);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

        if (type.equals("1")) {
            et_password.setHint("请输入新密码");
            bn_regist.setText("重置密码");
            ll_remark.setVisibility(View.GONE);
        }
        getverificationcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ClassPathResource.isMobileNO(et_username.getText().toString().trim().toString())) {
                    Toast.makeText(RegistActivity.this, "请正确填写手机号码",
                            Toast.LENGTH_SHORT).show();
                } else {
                    sendSms();
                    helper = new CountDownButtonHelper(getverificationcode, "倒计时", 60, 1);
                    helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                        @Override
                        public void finish() {
                            getverificationcode.setText("再次获取");
                        }
                    });
                    helper.start();
                }
            }
        });
        bn_regist.setOnClickListener(this);
        remark.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bn_regist:
                Intent intent =new Intent().setClass(RegistActivity.this,DataFillingActivity.class);
                startActivity(intent);

                password = et_password.getText().toString().trim();
                username = et_username.getText().toString().trim();
                code = et_code.getText().toString().trim();
                if (password.equals("")) {
                    Toast.makeText(RegistActivity.this, "手机号不能为空", Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else if (!username.equals(SharePrefUtil.getString(RegistActivity.this, "newUsername", ""))) {
                    Toast.makeText(RegistActivity.this, "号码验证不一致，请重新获取验证码", Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else if (code.equals("")) {
                    Toast.makeText(RegistActivity.this, "验证码不能为空", Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else if (!code.equals(SharePrefUtil.getString(RegistActivity.this, "newUsernameCode", ""))) {
                    Toast.makeText(RegistActivity.this, "验证码错误，请核对后输入", Toast.LENGTH_SHORT)
                            .show();
                    return;
                } else if (password.length() < 6) {
                    Toast.makeText(RegistActivity.this, "密码不能小于6位",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    regist(username, password);
                }
                break;
            case R.id.remark:

                break;
            default:
                break;
        }
    }

    private void sendSms() {
        String url = ParmasUrl.sendSms;
        Map<String, String> map = new HashMap<>();
        map.put("phone", et_username.getText().toString().trim());
        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST,
                url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("sendSms--TagJson", jsonObject.toString());
                try {
                    if (jsonObject.getString("code").equals("200")) {
                        String num = jsonObject.getString("num");
                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        msg.obj = num;
                        handler.sendMessage(msg);
                    }
                    Toast.makeText(RegistActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegistActivity.this, VolleyErrorHelper.getMessage(volleyError, RegistActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("sendSms");
        App.getHttpQueue().add(jsonObjectRequest);
    }

    public void regist(String username, String password) {
        String url;
        if (type.equals("1")) {
            //重置密码
            url = ParmasUrl.repwd;
        } else {
            //注册
            if (checkbox.isChecked()) {
                url = ParmasUrl.register;
            } else {
                Toast.makeText(RegistActivity.this, "请先阅读并同意龙商闪送服务条款", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String
                            code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        Toast.makeText(RegistActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                    Toast.makeText(RegistActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegistActivity.this, VolleyErrorHelper.getMessage(volleyError, RegistActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("registPost");
        // 将请求添加到队列中
        App.getHttpQueue().add(jsonObjectRequest);
    }
}
