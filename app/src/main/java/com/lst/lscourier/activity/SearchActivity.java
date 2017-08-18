package com.lst.lscourier.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.RecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
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
 * 搜索订单
 */

public class SearchActivity extends Activity implements XRecyclerView.LoadingListener {
    private Button search_return;
    private TextView search,tv_null;
    private EditText input_number_edit_text;
    private String input_content;
    private View view;
    private XRecyclerView recyclerView;
    private List<OrderEntry> datas = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    datas = (List<OrderEntry>) msg.obj;
                    initLayout();
                    break;
                default:
                    break;
            }
        }
    };
    //内部类
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.search_return:
                    SearchActivity.this.finish();
                    break;
                case R.id.search:
                    input_content = input_number_edit_text.getText().toString();
                    if (input_content.equals("")) {
                        Toast.makeText(SearchActivity.this, R.string.edit_text_hint, Toast.LENGTH_SHORT).show();
                    } else {
                        searchOrder(input_content);
                    }
                    break;
            }
        }
    };

    private void searchOrder(final String text) {
//        String url = ParmasUrl.searchorder;
        String url = "";
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", SharePrefUtil.getString(SearchActivity.this, "userid", ""));
        hashMap.put("text", text);
        MyJsonObjectRequest stringRequest = new MyJsonObjectRequest(Request.Method.POST, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("searchOrder======", object.toString());
                    if (object.getString("code").equals("200")) {
                        JSONArray arr = object.getJSONArray("data");
                        for (int i = 0; i < arr.length(); i++) {
                            final JSONObject obj = arr.getJSONObject(i);
                            OrderEntry orderBean = new OrderEntry();
                            orderBean.setId(obj.getString("id"));
                            orderBean.setOrder_id(obj.getString("order_id"));
                            orderBean.setStart_address(obj.getString("start_address"));
                            orderBean.setStart_xxaddress(obj.getString("start_xxaddress"));
                            orderBean.setExit_address(obj.getString("exit_address"));
                            orderBean.setExit_xxaddress(obj.getString("exit_xxaddress"));
                            orderBean.setOrder_type(obj.getString("order_type"));
                            orderBean.setOrder_weight(obj.getString("order_weight"));
                            orderBean.setStart_time(obj.getString("start_time"));
                            orderBean.setOrder_time(obj.getString("order_time"));
                            orderBean.setMoney(obj.getString("money"));
                            String order_status = obj.getString("order_status");
                            if (order_status.equals("0")) {
                                orderBean.setOrder_status("未付款");
                            } else if (order_status.equals("1")) {
                                orderBean.setOrder_status("已付款");
                            } else if (order_status.equals("2")) {
                                orderBean.setOrder_status("已完成");
                            } else if (order_status.equals("3")) {
                                orderBean.setOrder_status("已取消");
                            }
                            orderBean.setStart_name(obj.getString("start_name"));
                            orderBean.setExit_name(obj.getString("exit_name"));
                            orderBean.setStart_phone(obj.getString("start_phone"));
                            orderBean.setExit_phone(obj.getString("exit_phone"));
                            orderBean.setDistance(obj.getString("distance"));
                            orderBean.setMessage(obj.getString("message"));
                            orderBean.setPay_type(obj.getString("pay_type"));
                            orderBean.setDeliveryman_id(obj.getString("deliveryman_id"));
                            orderBean.setR_time(obj.getString("r_time"));
                            orderBean.setStart_long(obj.getString("start_long"));
                            orderBean.setStart_lat(obj.getString("start_lat"));
                            orderBean.setExit_long(obj.getString("exit_long"));
                            orderBean.setExit_lat(obj.getString("exit_lat"));
                            datas.add(orderBean);
                        }
                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        msg.obj = datas;
                        handler.sendMessage(msg);

                    }
                    Toast.makeText(SearchActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorHelper.getMessage(volleyError.getMessage(), SearchActivity.this);
            }
        });
        App.getHttpQueue().add(stringRequest);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);

        tv_null = (TextView) findViewById(R.id.tv_null);
        search_return = (Button) findViewById(R.id.search_return);
        search = (TextView) findViewById(R.id.search);
        input_number_edit_text = (EditText) findViewById(R.id.input_number_edit_text);

        recyclerView = (XRecyclerView) findViewById(R.id.search_recycle_view);

        //TODO使EditText仅仅接收字符（数字、字母和汉字）和“－”“_”
        input_number_edit_text.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int destart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i))
                                    .equals("_") && !Character.toString(source.charAt(i)).equals("-")) {
                                return "";
                            }
                        }
                        return null;
                    }
                }, new InputFilter.LengthFilter(20)//设置编辑框中可输入字符的长度
        });

        search_return.setOnClickListener(onClickListener);
        search.setOnClickListener(onClickListener);
    }


    private void initLayout() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setLoadingListener(this);
        if (datas.size() > 0) {
            tv_null.setVisibility(View.GONE);
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(datas,SearchActivity.this);
            recyclerView.setAdapter(recyclerAdapter);
        } else {
            tv_null.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }
    @Override
    public void onRefresh() {
        recyclerView.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        recyclerView.loadMoreComplete();
    }

}

