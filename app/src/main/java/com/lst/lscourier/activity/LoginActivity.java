package com.lst.lscourier.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import com.lst.lscourier.bean.UserBean;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lst719 on 2017/7/24.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText login_edit_phone_number, login_edit_password;
    private Button login_button_login, login_button_flash_operator_enroll;
    private TextView login_text_find_password, login_text_protocol, login_text_device_number;
    private Intent intent;
    private String saveUserId;
    private UserBean mUser = new UserBean();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mUser = (UserBean) msg.obj;
                    saveUserId = mUser.getUserid();
                    SharePrefUtil.saveString(LoginActivity.this, "userid",
                            saveUserId);
                    SharePrefUtil.saveBoolean(LoginActivity.this, "isLogin",
                            true);
                    SharePrefUtil.saveObj(LoginActivity.this, "User", mUser);
                    if (SharePrefUtil.getBoolean(LoginActivity.this, "isDataFilling", false)) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this, DataFillingActivity.class));
                    }
                    Log.d("isDataFilling", String.valueOf(SharePrefUtil.getBoolean(LoginActivity.this, "isDataFilling", false)));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
        initTitle();
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("登录");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });
    }

    private void initView() {
        TelephonyManager tm = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        tm.getDeviceId();
        login_text_device_number = (TextView) findViewById(R.id.login_text_device_number);
        login_text_device_number.setText("当前设备号:" + tm.getDeviceId());
        login_edit_phone_number = (EditText) findViewById(R.id.login_edit_phone_number);
        login_edit_password = (EditText) findViewById(R.id.login_edit_password);
        login_button_login = (Button) findViewById(R.id.login_button_login);
        login_text_find_password = (TextView) findViewById(R.id.login_text_find_password);
        login_text_protocol = (TextView) findViewById(R.id.login_text_protocol);
        login_button_flash_operator_enroll = (Button) findViewById(R.id.login_button_flash_operator_enroll);
        login_button_login.setOnClickListener(this);
        login_text_find_password.setOnClickListener(this);
        login_text_protocol.setOnClickListener(this);
        login_button_flash_operator_enroll.setOnClickListener(this);
    }

    /*
     * 登录
     */
    @SuppressLint("NewApi")
    private void login() {
        String url = ParmasUrl.login;
        Map<String, String> map = new HashMap<>();
        map.put("username", login_edit_phone_number.getText().toString());
        map.put("password", login_edit_password.getText().toString());
        // 创建StringRequest，定义字符串请求的请求方式为POST，
        MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("login======", object.toString());
                    if (object.getString("code").equals("200")) {
                        JSONObject obj = object.getJSONArray("data").getJSONObject(0);
                        UserBean userBean = new UserBean();
                        userBean.setUserid(obj.getString("id"));
                        userBean.setUsername(obj.getString("username"));
                        userBean.setPhone(obj.getString("phone"));
                        userBean.setPic(obj.getString("pic"));
                        userBean.setLevel(obj.getString("level"));
                        userBean.setIntegral(obj.getString("integral"));
                        userBean.setBalance(obj.getString("balance"));
                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        msg.obj = userBean;
                        handler.sendMessage(msg);
                    }
                    Toast.makeText(LoginActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, VolleyErrorHelper.getMessage(volleyError, LoginActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setTag("loginPost");
        // 将请求添加到队列中
        App.getHttpQueue().add(jsonObjectRequest);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button_login:
                login();
                break;
            case R.id.login_text_find_password:
                intent = new Intent(LoginActivity.this,
                        RegistActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.login_text_protocol:
                Intent priceInstruction = new Intent();
                priceInstruction.setClass(this, WebActivity.class);
                priceInstruction.putExtra("weburl", "file:///android_asset/protocol.htm");
                priceInstruction.putExtra("title", " 服务协议");
                startActivity(priceInstruction);
                break;
            case R.id.login_button_flash_operator_enroll:
                intent = new Intent(LoginActivity.this,
                        RegistActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);
                break;
        }
    }
}
