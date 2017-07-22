package com.lst.lscourier.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * author describe parameter return
 */
@SuppressLint("ClickableViewAccessibility")
public class MyViewPager extends ViewPager {
	private boolean result = false;

	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (result) {
			return super.onInterceptTouchEvent(arg0);
		} else {
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (result) {
			return super.onTouchEvent(arg0);
		} else {
			return false;
		}
	}

	/**
	 * 根据子控件确定viewpager高度
	 */
	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	//
	// int height = 0;
	// for (int i = 0; i < getChildCount(); i++) {
	// View child = getChildAt(i);
	// child.measure(widthMeasureSpec,
	// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	// int h = child.getMeasuredHeight();
	// if (h > height)
	// height = h;
	// }
	//
	// heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
	// MeasureSpec.EXACTLY);
	//
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// }

}