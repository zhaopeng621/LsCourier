package com.lst.lscourier.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lst.lscourier.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lst718-011 on 2017/7/7.
 */
public class AboutMeActivity extends CheckPermissionsActivity{
    private ListView list;
    private List<String> stringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_me);
        initTitle();
        initList();
        initView();
    }

    private void initView() {
        list = (ListView) findViewById(R.id.about_list);
        TextView version = (TextView) findViewById(R.id.version);
        version.setText("龙商闪送员 V："+getVersion());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.array_list_item,
                R.id.text1,
                stringList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (stringList.get(i).equals("版本更新")) {
//
//                } else
                if (stringList.get(i).equals("联系客服")) {
                    Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:4001188521"));
                    //启动
                    startActivity(phoneIntent);
                }
            }
        });
    }

    private void initList() {
//       stringList.add("版本更新");
        stringList.add("联系客服");
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("关于我们");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutMeActivity.this.finish();
            }
        });
    }

    private String getVersion() {
        // 获得一个系统包管理器
        PackageManager pm = getPackageManager();
        // 获得包管理器
        try {
            // 获得功能清单文件
            PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            // 不可能发生的异常
            return "";
        }
    }
}
