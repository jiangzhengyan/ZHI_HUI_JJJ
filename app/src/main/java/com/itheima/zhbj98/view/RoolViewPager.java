package com.itheima.zhbj98.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class RoolViewPager extends ViewPager {

	private int downX;
	private int downY;


	public RoolViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RoolViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//通过代码控件父控件是否拦截事件
		//getParent() : 获取父控件
		//requestDisallowInterceptTouchEvent : 请求父控件不要拦截事件,true:不拦截；false:拦截
		//getParent().requestDisallowInterceptTouchEvent(false);
		//1.判断是否上下滑动还是左右滑动，可以通过x和y之间的距离进行判断
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			//判断是上下滑动还是左右滑动
			if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
				//左右滑动
				//从右往左
				//如果是最后一张图片，父控件拦截事件，父控件执行切换界面，如果不是最后一张图片，当前的viewpager中切换下一张图片
				//getAdapter() : 获取设置给viewpager的adapter
				if (downX - moveX > 0 && getCurrentItem() == getAdapter().getCount()-1) {
					getParent().requestDisallowInterceptTouchEvent(false);
				}else if(downX - moveX > 0 && getCurrentItem() < getAdapter().getCount()-1){
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				//从左往右
				//如果是第一张图片，父控件拦截事件，实现打开侧拉菜单，如果不是第一张图片，当前的viewpager切换上一张图片
				else if(downX - moveX < 0 && getCurrentItem() == 0){
					getParent().requestDisallowInterceptTouchEvent(false);
				}else if(downX - moveX < 0 && getCurrentItem() > 0){
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				
			}else{
				//上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
