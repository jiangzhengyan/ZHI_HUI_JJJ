package com.itheima.zhbj98.base;

import com.itheima.zhbj98.HomeActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;

/**
 * 侧拉菜单条目界面的父类
 * Author _ jjjzzzyyy
 * 2016年12月26日 上午10:36:09
 */
public abstract class BaseMenuPager {

	public View view;
	public Activity activity;
	public SlidingMenu slidingMenu;
	
	public BaseMenuPager(Activity activity){
		this.activity = activity;
		slidingMenu = ((HomeActivity)activity).getSlidingMenu();
		view = initView();
	}
	
	//2.父类不知道子类加载什么界面，显示什么数据，所以将initView和initData设置为抽象方法，子类实现抽取方法进行操作
	/**
	 * 加载布局
	 * 
	 * 2016年12月26日 上午10:32:34
	 */
	public abstract View initView();
	
	/**
	 * 初始化控件，显示数据
	 * 
	 * 2016年12月26日 上午10:32:59
	 */
	public abstract void initData();
	
}
