package com.lst.lscourier.app;

import android.app.Activity;
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lst.lscourier.utils.ToastUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by bob on 2015/1/30.
 */
@SuppressWarnings("deprecation")
public class App extends Application {
	private List<Activity> activityList = new LinkedList<Activity>();
	private static App instance;
	public static RequestQueue queue;
	private long exitTime = 0;
	@Override
	public void onCreate() {
		super.onCreate();
		queue = Volley.newRequestQueue(getApplicationContext());
		instance = this;
	}
	public static RequestQueue getHttpQueue() {
		return queue;
	}

	// 单例模式中获取唯一的MyApplication实例
	public static App getInstance() {
		if (null == instance) {
			instance = new App();
		}
		return instance;

	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit() {
		// TODO Auto-generated method stub
		for (Activity activity : activityList) {
			activity.finish();
		}
		activityList.clear();
	}

	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			ToastUtils.showToast(getApplicationContext(), "再按一次退出程序");
			exitTime = System.currentTimeMillis();
		} else {
			System.exit(0);
		}
	}
}
