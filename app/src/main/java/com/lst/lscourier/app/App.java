package com.lst.lscourier.app;

import android.app.ActivityGroup;
import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by bob on 2015/1/30.
 */
@SuppressWarnings("deprecation")
public class App extends Application {
	private List<ActivityGroup> activityList = new LinkedList<ActivityGroup>();
	private static App instance;
	public static RequestQueue queue;

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
	public void addActivity(ActivityGroup activity) {
		activityList.add(activity);
	}

	public void exit() {
		// TODO Auto-generated method stub
		for (ActivityGroup activity : activityList) {
			activity.finish();
		}

	}
}
