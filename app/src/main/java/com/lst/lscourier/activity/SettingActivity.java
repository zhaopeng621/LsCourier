package com.lst.lscourier.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lst.lscourier.R;
import com.lst.lscourier.utils.SharePrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lst718-011 on 2017/7/6.
 */
public class SettingActivity extends Activity {
    private ListView setting_list;
    private List<String> settingList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        initTitle();
        initList();
        initView();
    }

    private void initList() {
        settingList.add("常见问题");
        settingList.add("意见反馈");
        settingList.add("关于我们");
    }

    private void initTitle() {
        ImageView title_back = (ImageView) findViewById(R.id.title_back);
        TextView title_text = (TextView) findViewById(R.id.title_text);
        title_text.setText("设置中心");
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });
    }

    private void initView() {
        setting_list = (ListView) findViewById(R.id.setting_list);
        TextView exit = (TextView) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
                SharePrefUtil.saveBoolean(SettingActivity.this, "isLogin", false);
                SettingActivity.this.finish();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.array_list_item,
                R.id.text1,
                settingList);
        setting_list.setAdapter(adapter);
        setting_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (settingList.get(i).equals("常见问题")) {
                    Intent intent = new Intent(SettingActivity.this, WebActivity.class);
                    intent.putExtra("weburl", "file:///android_asset/five.html");
                    intent.putExtra("title", "常见问题");
                    startActivity(intent);
                } else if (settingList.get(i).equals("意见反馈")) {
                    startActivity(new Intent(SettingActivity.this, UserFeedbackActivity.class));
                } else if (settingList.get(i).equals("关于我们")) {
                    startActivity(new Intent(SettingActivity.this, AboutMeActivity.class));
                }
            }
        });
    }

}