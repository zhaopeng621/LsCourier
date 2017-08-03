package com.lst.lscourier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lst.lscourier.LruCacheUtils.ImageCacheManager;
import com.lst.lscourier.R;
import com.lst.lscourier.adapter.TOrderRecyclerAdapter;
import com.lst.lscourier.app.App;
import com.lst.lscourier.bean.OrderEntry;
import com.lst.lscourier.bean.UserBean;
import com.lst.lscourier.utils.SharePrefUtil;
import com.lst.lscourier.utils.TimeUtils;
import com.lst.lscourier.utils.ToastUtils;
import com.lst.lscourier.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        XRecyclerView.LoadingListener {
    private long exitTime = 0;
    private TextView user_phone, main_time, tv_number, tv_money, tv_null;
    private Button bt_go_scramble;
    private XRecyclerView recyclerview;
    private LinearLayout ll_refrash;
    private NavigationView navigationView;
    private RoundImageView user_face;
    private UserBean userBean;
    private List<OrderEntry> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initNavigationView();
        initView();
    }

    private void initView() {
        tv_null = (TextView) findViewById(R.id.tv_null);
        main_time = (TextView) findViewById(R.id.main_time);
        main_time.setText(TimeUtils.getTime());
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_money = (TextView) findViewById(R.id.tv_money);
        bt_go_scramble = (Button) findViewById(R.id.bt_go_scramble);
        ll_refrash = (LinearLayout) findViewById(R.id.ll_refrash);
        recyclerview = (XRecyclerView) findViewById(R.id.recyclerview);
        ll_refrash.setOnClickListener(this);
        bt_go_scramble.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerview.setLoadingListener(this);
        for (int i = 0; i < 10; i++) {
            OrderEntry orderEntry = new OrderEntry();
            orderEntry.setStart_address("幸福大厦");
            orderEntry.setStart_xxaddress("东三环北路3号");
            orderEntry.setExit_address("牛王庙9号小区");
            orderEntry.setExit_xxaddress("新源街13号");
            orderEntry.setMoney("35");
            orderEntry.setOrder_weight("5");
            orderEntry.setDistance("12");
            orderEntry.setOrder_id("HD15452145458");
            orderEntry.setOrder_time("7-25 00:00");
            orderEntry.setOrder_status("待取件 ");
            datas.add(orderEntry);
        }
        if (datas.size() > 0) {
            tv_null.setVisibility(View.GONE);
            TOrderRecyclerAdapter recyclerAdapter = new TOrderRecyclerAdapter(datas, MainActivity.this);
            recyclerview.setAdapter(recyclerAdapter);
        } else {
            tv_null.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        initNavigationView();
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
                Intent intent = new Intent(MainActivity.this, MySendFlashActivity.class);
                startActivityForResult(intent, 11);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ImageView toolbar_right = (ImageView) findViewById(R.id.toolbar_right);
        toolbar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,PersonalCenterActivity.class));
            }
        });
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharePrefUtil.getBoolean(MainActivity.this, "isLogin", false)) {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                } else {
                drawer.openDrawer(GravityCompat.START);
                }
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
            case R.id.bt_go_scramble:
               startActivity(new Intent(MainActivity.this , ScrambleActivity.class));
                break;
            case R.id.ll_refrash:

                break;
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
            startActivity(new Intent(MainActivity.this,MyOrderActivity.class) );
            return true;
        } else if (id == R.id.nav_wallet) {
            startActivity(new Intent(MainActivity.this,MyWalletActivity.class) );
            return true;
        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(MainActivity.this,SettingActivity.class) );
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        recyclerview.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        recyclerview.loadMoreComplete();
    }
}
