package com.lst.lscourier.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.ScrambleRecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.parmas.MyJsonObjectRequest;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.DistanceUtils;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.ToastUtils;
import com.lst.lscourier.utils.Utils;
import com.lst.lscourier.utils.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lst718-011 on 2017/7/25.
 */
public class ScrambleActivity extends CheckPermissionsActivity implements XRecyclerView.LoadingListener {
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private ImageView img_grab;
    private TextView tv_null;
    private XRecyclerView scramble_recyclerview;
    private List<OrderEntry> datas = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String obj = (String) msg.obj;
                    if (obj.equals("200")){
                        img_grab.setBackgroundResource(R.mipmap.grab_success);
                    }else{
                        img_grab.setBackgroundResource(R.mipmap.tasked);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scramble);
        initTitle();
        initView();
        initLocation();
    }

    private void getOrder() {
        String url = ParmasUrl.select_order;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", SharePrefUtil.getString(ScrambleActivity.this, "userid", ""));
        MyJsonObjectRequest stringRequest = new MyJsonObjectRequest(Request.Method.POST, url, hashMap, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    Log.d("login======", object.toString());
                    if (object.getString("code").equals("200")) {
                        JSONArray arr = object.getJSONArray("order_id");
                        for (int i = arr.length() - 1; i > -1; i--) {
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
                            if (obj.getString("message").equals("null")) {
                                orderBean.setMessage("");
                            } else {
                                orderBean.setMessage(obj.getString("message"));
                            }
                            if (obj.getString("pay_type").equals("null")) {
                                orderBean.setPay_type("");
                            } else {
                                orderBean.setPay_type(obj.getString("pay_type"));
                            }
                            if (obj.getString("deliveryman_id").equals("null")) {
                                orderBean.setDeliveryman_id("");
                            } else {
                                orderBean.setDeliveryman_id(obj.getString("deliveryman_id"));
                            }
                            orderBean.setR_time(obj.getString("r_time"));
                            orderBean.setStart_long(obj.getString("start_long"));
                            orderBean.setStart_lat(obj.getString("start_lat"));
                            orderBean.setExit_long(obj.getString("exit_long"));
                            orderBean.setExit_lat(obj.getString("exit_lat"));
                            datas.add(orderBean);
                        }
                    }
                    if (datas.size() > 0) {
                        tv_null.setVisibility(View.GONE);
                        ScrambleRecyclerAdapter recyclerAdapter = new ScrambleRecyclerAdapter(datas, ScrambleActivity.this,
                                new ScrambleRecyclerAdapter.MyOnClickListener() {
                                    @Override
                                    public void myOnClick(View view, int i) {
                                        openPopupwin(view, i);
                                    }
                                });
                        scramble_recyclerview.setAdapter(recyclerAdapter);
                    } else {
                        tv_null.setVisibility(View.VISIBLE);
                        scramble_recyclerview.setVisibility(View.GONE);
                    }
                    Toast.makeText(ScrambleActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorHelper.getMessage(volleyError.getMessage(), ScrambleActivity.this);
            }
        });
        App.getHttpQueue().add(stringRequest);
    }

    private void initView() {
        scramble_recyclerview = (XRecyclerView) findViewById(R.id.scramble_recyclerview);
        tv_null = (TextView) findViewById(R.id.tv_null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ScrambleActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scramble_recyclerview.setLayoutManager(layoutManager);
        scramble_recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        scramble_recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        scramble_recyclerview.setLoadingListener(this);
    }

    @Override
    public void onRefresh() {
        scramble_recyclerview.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        scramble_recyclerview.loadMoreComplete();
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("我要抢单");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrambleActivity.this.finish();
            }
        });
    }

    //支付明细
    private void openPopupwin(View v,final int position) {
        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.scramble_pop, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.PopupAnimation);
        // 在底部显示
        window.showAtLocation(v,
                Gravity.CENTER_VERTICAL, 0, 0);
        // 这里检验popWindow里的button是否可以点击
        img_grab = (ImageView) view.findViewById(R.id.img_grab);
        ImageView close_img = (ImageView) view.findViewById(R.id.img_clear);
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        TextView tv_weight = (TextView) view.findViewById(R.id.tv_weight);
        TextView tv_my_distance = (TextView) view.findViewById(R.id.tv_my_distance);
        TextView get_time = (TextView) view.findViewById(R.id.get_time);
        TextView get_address = (TextView) view.findViewById(R.id.get_address);
        TextView send_address = (TextView) view.findViewById(R.id.send_address);
        TextView order_number = (TextView) view.findViewById(R.id.order_number);
        TextView order_genre = (TextView) view.findViewById(R.id.order_genre);
        TextView tv_scramble = (TextView) view.findViewById(R.id.tv_scramble);

        tv_money.setText(datas.get(position).getMoney().toString());
        tv_distance.setText(datas.get(position).getDistance().toString());
        tv_weight.setText(datas.get(position).getOrder_weight().toString());
        get_time.setText(datas.get(position).getOrder_time().toString());
        get_address.setText(datas.get(position).getStart_address() + datas.get(position).getStart_xxaddress());
        send_address.setText(datas.get(position).getExit_address() + datas.get(position).getExit_xxaddress());
        order_number.setText(datas.get(position).getOrder_id().toString());
        order_genre.setText(datas.get(position).getOrder_type().toString());
        float distance = DistanceUtils.calculateFrom(ScrambleActivity.this,
                datas.get(position).getStart_lat(),
                datas.get(position).getStart_long());
        tv_my_distance.setText(String.valueOf(distance));
        tv_scramble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateOrder(datas.get(position).getOrder_id().toString());
            }
        });
        close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                scramble_recyclerview.refreshComplete();
            }
        });
    }

    private void rateOrder(String orderId) {
            String url = ParmasUrl.rate_order;
            Map<String, String> map = new HashMap<>();
            map.put("d_id", SharePrefUtil.getString(ScrambleActivity.this,"userid",""));
            map.put("order_id", orderId);
            // 创建StringRequest，定义字符串请求的请求方式为POST，
            MyJsonObjectRequest jsonObjectRequest = new MyJsonObjectRequest(Request.Method.POST, url, map, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject object) {
                    try {
                        Log.d("rate_order======", object.toString());
                        String code = object.getString("code");
                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        msg.obj = code;
                        handler.sendMessage(msg);
                        Toast.makeText(ScrambleActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(ScrambleActivity.this, VolleyErrorHelper.getMessage(volleyError, ScrambleActivity.this), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setTag("loginPost");
            // 将请求添加到队列中
            App.getHttpQueue().add(jsonObjectRequest);



    }

    //定位相关
    public void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(ScrambleActivity.this);
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        //开启定位
        locationClient.startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                    sb.append("提供者    : " + location.getProvider() + "\n");

                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                    sb.append("角    度    : " + location.getBearing() + "\n");
                    // 获取当前提供定位服务的卫星个数
                    sb.append("星    数    : " + location.getSatellites() + "\n");
                    sb.append("国    家    : " + location.getCountry() + "\n");
                    sb.append("省            : " + location.getProvince() + "\n");
                    sb.append("市            : " + location.getCity() + "\n");
                    sb.append("城市编码 : " + location.getCityCode() + "\n");
                    sb.append("区            : " + location.getDistrict() + "\n");
                    sb.append("区域 码   : " + location.getAdCode() + "\n");
                    sb.append("地    址    : " + location.getAddress() + "\n");
                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
                    //定位完成的时间
                    sb.append("定位时间: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                    SharePrefUtil.saveString(ScrambleActivity.this, "MyLongitude", String.valueOf(location.getLongitude()));
                    SharePrefUtil.saveString(ScrambleActivity.this, "MyLatitude", String.valueOf(location.getLatitude()));
                    SharePrefUtil.saveString(ScrambleActivity.this, "MyLocation", String.valueOf(location.getAddress()));
//                    tvResult.setText(location.getCity());
                    getOrder();
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    ToastUtils.showToast(ScrambleActivity.this, "定位失败");
                    initLocation();
                }
                //定位之后的回调时间
                sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
                //解析定位结果，
                String result = sb.toString();
                Log.d("定位之后的回调", result);

            } else {
//                tvResult.setText("定位失败");
                ToastUtils.showToast(ScrambleActivity.this, "定位失败");
            }
        }
    };

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }
}