package com.itheima.zhbj98.base;

import com.itheima.zhbj98.HomeActivity;
import com.itheima.zhbj98.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 首页，新闻中心等Fragment界面的父类 Author _ jjjzzzyyy 2016年12月24日 下午3:12:43
 */
public class BasePagerFragment extends Fragment {

	public View view;
	public Activity activity;
	public FrameLayout mContent;
	public ImageView mMenu;
	public TextView mTitle;
	public SlidingMenu slidingMenu;
	public ImageView mPhotos;

	// 初始化数据
	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = getActivity();
		slidingMenu = ((HomeActivity)activity).getSlidingMenu();
		super.onCreate(savedInstanceState);
	}

	// 加载布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView();
		return view;
	}

	// 显示数据
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		super.onActivityCreated(savedInstanceState);
	}
	
	
	//2.父类不知道子类要加载什么布局，显示什么数据，所以创建抽象方法，子类实现抽象方法根据自己的特性进行操作
	
	
	
	//3.因为首页，新闻中心等界面都是标题栏+内容显示区域操作，布局也是相同操作，也可以抽取到父类中，所以父类需要加载一个抽取的布局
	//所以，initview和initdata不能写成抽象方法
	/**
	 * 加载布局
	 * 
	 * 2016年12月24日 下午3:14:01
	 */
	public View initView(){
		//由父类加载抽取的布局
		view = View.inflate(activity, R.layout.basepagerfragment, null);
		mTitle = (TextView) view.findViewById(R.id.titlebar_tv_title);
		mMenu = (ImageView) view.findViewById(R.id.titlebar_iv_menu);
		mContent = (FrameLayout) view.findViewById(R.id.basepagerfragment_fl_content);
		mPhotos = (ImageView) view.findViewById(R.id.titlebar_iv_photos);
		
		//设置侧拉菜单的按钮点击事件，实现打开关闭侧拉菜单
		mMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});
		
		return view;
	};
	
	/**
	 * 显示数据
	 * 
	 * 2016年12月24日 下午3:14:19
	 */
	public void initData(){
		
	};
	
	
	
	
	
	
	
	
	
	
	

}
