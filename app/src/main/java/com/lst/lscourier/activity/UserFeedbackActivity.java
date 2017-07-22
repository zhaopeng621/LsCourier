package com.lst.lscourier.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户反馈界面
 *
 * @author gpc
 */
public class UserFeedbackActivity extends Activity {
    private EditText mFeedback;
    private Button msubBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_feedback);
        initTitle();
        initview();
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("意见反馈");
        title_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFeedbackActivity.this.finish();
            }
        });
    }

    private void initview() {
        // TODO Auto-generated method stub
        mFeedback = (EditText) findViewById(R.id.user_feedback_EdId);
        msubBtn = (Button) findViewById(R.id.sure_summit_btnId);
        msubBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String feedback = mFeedback.getEditableText().toString();
                if (TextUtils.isEmpty(feedback)) {
                    Toast.makeText(getApplicationContext(), "请填写您要反馈的内容",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    feedbackAdd();
                }
            }
        });

    }

    /**
     * 反馈
     */
    private void feedbackAdd() {
        String url = ParmasUrl.feedBackAdd;
        // 创建StringRequest，定义字符串请求的请求方式为POST，
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            // 请求成功后执行的函数
            @Override
            public void onResponse(String s) {
                // 打印出POST请求返回的字符串
                try {
                    Log.d("feedBackAdd--", s);
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        finish();
                    }
                    Toast.makeText(UserFeedbackActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            // 请求失败时执行的函数
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorHelper.getMessage(volleyError, UserFeedbackActivity.this);
            }
        }) {
            // 定义请求数据
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> hashMap = new HashMap<String, String>();
//                hashMap.put("user_id", SharePrefUtil.getString(UserFeedbackActivity.this, "userid", ""));
                hashMap.put("text", mFeedback.getText().toString());
                return hashMap;
            }
        };
        // 设置该请求的标签
        request.setTag("UserFeedbackPost");
        // 将请求添加到队列中
        App.getHttpQueue().add(request);
    }
}
