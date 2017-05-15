package com.itheima.zhbj98.base;

import com.itheima.zhbj98.HomeActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 内容页和菜单页的父类 Author _ jjjzzzyyy 2016年12月24日 上午10:26:37
 */
public abstract class BaseFragment extends Fragment {

	public View view;
	public Activity activity;
	public SlidingMenu slidingMenu;

	// 初始化数据
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//因为两个子类可能都会用到上下文，所有直接在父类获取，让子类直接使用
		//getActivity() : 获取Fragment依赖着的activity，当前获取的其实就是HomeActivity
		activity = getActivity();
		//因为MenuFragment和ContentFragment是添加在HomeActivity的，所以可以通过获取HomeActivity对象，获取侧拉菜单对象
		slidingMenu = ((HomeActivity)activity).getSlidingMenu();
		super.onCreate(savedInstanceState);
	}

	// 加载布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//4.因为真正的加载布局和显示数据是在Fragment的oncreateView和onActivityCreate方法中实现的，不是在initview和initdata中实现的
		//所以需要让oncreateview和onActivityCteate方法调用initview和initdata方法，实现布局加载显示数据操作
		view = initView();
		return view;
	}

	// 加载显示数据
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		super.onActivityCreated(savedInstanceState);
	}
	
	//2.父类不知道子类要显示什么界面，显示什么数据，所以父类可以创建抽象方法，子类实现抽象方法，根据自己的特性进行具体的操作
	
	/**
	 * 加载布局
	 * 
	 * 2016年12月24日 上午10:29:15
	 */
	public abstract View initView();
	
	/**
	 * 显示数据
	 * 
	 * 2016年12月24日 上午10:29:48
	 */
	public abstract void initData();
	
}
