package com.lst.lscourier.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
 * 登录
 */
public class LoginActivity extends Activity {
    protected static final String TAG = "LoginActivity";
    private EditText et_username;
    private EditText et_password;
    private TextView register;
    private TextView find_password;
    private Button login;
    private String saveUserId;
    private UserBean mUser = new UserBean();

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mUser = (UserBean) msg.obj;
                    saveUserId = mUser.getUserid();
                    SharePrefUtil.saveString(getApplicationContext(), "userid",
                            saveUserId);
                    SharePrefUtil.saveBoolean(getApplicationContext(), "isLogin",
                            true);
                    SharePrefUtil.saveObj(LoginActivity.this, "User", mUser);
                    LoginActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        initView();
        initTitle();
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("登录");
        title_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.loginaccount);
        et_password = (EditText) findViewById(R.id.loginpassword);
        register = (TextView) findViewById(R.id.register);
        find_password = (TextView) findViewById(R.id.find_password);
        login = (Button) findViewById(R.id.login);
        //重置密码
        find_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(LoginActivity.this,
                        RegistActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
        // 注册按钮
        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,
                        RegistActivity.class);
                intent.putExtra("type", "0");
                startActivity(intent);

            }
        });
    }

    /*
     * 登录
     */
    @SuppressLint("NewApi")
    private void login() {
        String url = ParmasUrl.login;
        Map<String, String> map = new HashMap<>();
        map.put("username", et_username.getText().toString());
        map.put("password", et_password.getText().toString());
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

    public void loginclick(View v) {
        login();
    }
}
