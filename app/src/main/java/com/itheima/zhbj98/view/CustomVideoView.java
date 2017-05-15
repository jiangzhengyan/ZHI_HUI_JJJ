package com.itheima.zhbj98.view;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.VideoView;
/**
 * 自定义的VideoView
 * Author _ jjjzzzyyy
 * 2016年12月23日 上午10:44:55
 */
public class CustomVideoView extends VideoView {

	public CustomVideoView(Context context) {
		//super(context);
		this(context,null);
	}

	public CustomVideoView(Context context, AttributeSet attrs) {
		//super(context, attrs);
		this(context,attrs,-1);
	}
	
	public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	//测量控件
	//widthMeasureSpec : 期望的宽度（可以理解为布局文件的宽度）
	//heightMeasureSpec : 期望的高度（可以理解为布局文件的高度）
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//获取控件的宽度，手动进行测量
		//获取被父控件约束的宽度或者是高度
		//参数1：默认控件的宽/高
		//参数2：父控件约束的宽/高
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		//手动测量控件宽高
		this.setMeasuredDimension(width, height);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
