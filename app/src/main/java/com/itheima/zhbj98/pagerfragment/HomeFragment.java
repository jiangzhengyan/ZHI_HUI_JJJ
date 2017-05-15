package com.itheima.zhbj98.pagerfragment;

import com.itheima.zhbj98.base.BasePagerFragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
/**
 * 首页的Fragment
 * Author _ jjjzzzyyy
 * 2016年12月24日 下午3:10:47
 */
public class HomeFragment extends BasePagerFragment {

	
	//1.首页，新闻等Fragment界面都要加载界面，显示数据，相同操作，抽取父类
	
	//子类重写父类的方法，根据自己的需要对相应的方法进行操作
	@Override
	public View initView() {
		// TODO Auto-generated method stub
		return super.initView();
	}
	
	
	@Override
	public void initData() {
		//显示内容
		TextView textView = new TextView(activity);
		textView.setText("首页");
		textView.setTextSize(23);
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);
		//添加到父类的内容显示区域显示
		mContent.addView(textView);
		//设置父类的标题显示
		mTitle.setText("首页");
		//设置父类中的菜单页按钮是隐藏操作
		mMenu.setVisibility(View.GONE);
		
		super.initData();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
