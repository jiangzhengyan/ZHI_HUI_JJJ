package com.itheima.zhbj98.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.zhbj98.base.BaseMenuPager;


/**
 * 侧拉菜单专题条目的界面
 * 不使用Fragment，因为之前已经嵌套了两层Fragment，Fragment嵌套太多容易内容移除
 * 不使用Activity，因为Activity无法添加到Fragment中
 * 因为一个界面其实就是通过加载界面，初始化控件，将数据添加到控件中进行显示就可以了，可以使用普通的java，通过实现这些操作来充当界面
 * Author _ jjjzzzyyy
 * 2016年12月26日 上午10:29:34
 */
public class MenuSpecialPager extends BaseMenuPager{

	//1.因为新闻，专题等界面都要实现加载布局，显示数据操作，相同操作抽取父类
	
	public MenuSpecialPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		TextView textView = new TextView(activity);
		textView.setText("菜单详情页-专题");
		textView.setTextSize(23);
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);
		return textView;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
