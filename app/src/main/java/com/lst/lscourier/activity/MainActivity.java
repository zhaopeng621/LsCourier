package com.lst.lscourier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.lst.lscourier.LruCacheUtils.ImageCacheManager;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.bean.UserBean;
import com.lst.lscourier.parmas.ParmasUrl;
import com.lst.lscourier.utils.ImageViewHolder;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.ToastUtils;
import com.lst.lscourier.utils.VolleyErrorHelper;
import com.lst.lscourier.view.RoundImageView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private List<String> imgs = new ArrayList<>();

    private TextView  user_phone;

    private long exitTime = 0;



    private NavigationView navigationView;
    private RoundImageView user_face;
    private UserBean userBean;
    private OrderEntry orderBean;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    PayReq req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initNavigationView();
        getConvenientBanner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNavigationView();
    }


    public void getConvenientBanner() {
//        if (SharePrefUtil.getObj(MainActivity.this, "picList") != null) {
//            imgs = (List<String>) SharePrefUtil.getObj(MainActivity.this, "picList");
//            initConvenientBanner();
//        } else {
            getImgs();
//        }
    }

    public void initConvenientBanner() {
        ConvenientBanner mCb = (ConvenientBanner) findViewById(R.id.id_cb);
        mCb.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new ImageViewHolder();
            }
        }, imgs) //设置需要切换的View
                .setPointViewVisible(true)    //设置指示器是否可见
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.mipmap.login_point, R.mipmap.login_point_selected})
                //设置指示器位置（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .startTurning(2000)     //设置自动切换（同时设置了切换时间间隔）
                .setManualPageable(true); //设置手动影响（设置了该项无法手动切换）
    }

    public void getImgs() {
        String url = ParmasUrl.select_pic;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("200")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            imgs.add(data.getJSONObject(i).getString("pic_url"));
                        }
                        SharePrefUtil.saveObj(MainActivity.this, "picList", imgs);
                        initConvenientBanner();
                    }
                    Toast.makeText(MainActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, VolleyErrorHelper.getMessage(volleyError, MainActivity.this), Toast.LENGTH_SHORT).show();
            }
        });
        App.getHttpQueue().add(stringRequest);
    }

    public void initNavigationView() {
        userBean = (UserBean) SharePrefUtil.getObj(MainActivity.this, "User");
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        user_face = (RoundImageView) headerView.findViewById(R.id.user_face);
        user_phone = (TextView) headerView.findViewById(R.id.user_phone);
        if (SharePrefUtil.getBoolean(MainActivity.this, "isLogin", false)) {
            if (!userBean.getPic().equals("") && !userBean.getPic().equals("null")) {
                ImageCacheManager.loadImage(userBean.getPic(), user_face, ImageCacheManager.getBitmapFromRes(App.getInstance(), R.mipmap.default_user_head),
                        ImageCacheManager.getBitmapFromRes(App.getInstance(), R.mipmap.default_user_head));
            }
            CharSequence text = userBean.getUsername();
            if (!TextUtils.isEmpty(text) && text.length() > 6) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    if (i >= 3 && i <= 6) {
                        sb.append('*');
                    } else {
                        sb.append(c);
                    }
                }
                user_phone.setText(sb.toString());
            }
        }
        user_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonalCenterActivity.class);
                startActivityForResult(intent, 11);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!SharePrefUtil.getBoolean(MainActivity.this, "isLogin", false)) {
//                    initActivity(LoginActivity.class);
//                } else {
                    drawer.openDrawer(GravityCompat.START);
//                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showToast(MainActivity.this, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 100:

                break;

        }


    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_order) {

            return true;
        } else if (id == R.id.nav_wallet) {

            return true;
        } else if (id == R.id.nav_setting) {

            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }


}
