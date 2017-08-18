package com.lst.lscourier.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.DisciplinaryRecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.DisciplinaryBean;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 奖惩记录
 */

public class DisciplinaryRecordsActivity extends Activity implements XRecyclerView.LoadingListener {
    private TextView tv_null;
    private XRecyclerView recyclerview;
    private List<DisciplinaryBean> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disciplinary_result);
        initTitle();
        initView();
        getDatas();
    }

    private void initView() {
        tv_null = (TextView) findViewById(R.id.tv_record_no);
        recyclerview = (XRecyclerView) findViewById(R.id.disciplinary);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DisciplinaryRecordsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerview.setLoadingListener(this);

    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("奖惩记录");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onRefresh() {
        recyclerview.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        recyclerview.loadMoreComplete();
    }

    public void getDatas() {
        String url = ParmasUrl.select_change;
        Map<String, String> map = new HashMap<>();
        map.put("id", SharePrefUtil.getString(DisciplinaryRecordsActivity.this, "userid", ""));
        MyJsonObjectRequest myJsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.d("withdrawlist===", jsonObject.toString());
                    if (jsonObject.getString("code").equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            DisciplinaryBean disciplinaryBean = new DisciplinaryBean();
                            disciplinaryBean.setDisciplinaryId(obj.getString("id"));
                            disciplinaryBean.setDisciplinaryMoney(obj.getString("money"));
                            disciplinaryBean.setDisciplinaryMsg(obj.getString("message"));
                            disciplinaryBean.setDisciplinaryTime(obj.getString("time"));
                            disciplinaryBean.setDisciplinaryType(obj.getString("type"));
                            datas.add(disciplinaryBean);
                        }
                    }
                    if (datas.size() > 0) {
                        tv_null.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.VISIBLE);
                        DisciplinaryRecyclerAdapter recyclerAdapter = new DisciplinaryRecyclerAdapter(datas, DisciplinaryRecordsActivity.this);
                        recyclerview.setAdapter(recyclerAdapter);
                    } else {
                        tv_null.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.GONE);
                    }
                    Toast.makeText(DisciplinaryRecordsActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorHelper.getMessage(volleyError.getMessage(), DisciplinaryRecordsActivity.this);
            }
        });
        App.getHttpQueue().add(myJsonObjectRequest);

    }


}
