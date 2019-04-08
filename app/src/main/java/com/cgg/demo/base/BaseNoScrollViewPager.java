package com.cgg.demo.base;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义一个不能左右滑动的ViewPager
 * @author Beyond
 *
 */
public class BaseNoScrollViewPager extends ViewPager {

	public BaseNoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseNoScrollViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}
	
	/**
	 * 不拦截事件
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
}
