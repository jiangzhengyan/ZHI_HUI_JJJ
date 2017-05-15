package com.itheima.zhbj98.view;

import android.view.View;
/**
 * 自定义ViewPager的旋转动画
 * Author _ jjjzzzyyy
 * 2016年12月23日 下午4:52:34
 */
public class RotatePagerPageTransFormer implements android.support.v4.view.ViewPager.PageTransformer{

	private static final float MAXRADIUS = 25f;
	
	//当viewpager界面切换的时候调用的方法
	//page : 切换的界面
	//position : 切换的界面的状态（切换的界面包含两个界面）,当前的界面position是0，预加载的界面的position是1，从右往左切换界面的时候
	//当前界面的position会从0开始往-1变化，预加载界面的position是从1开始往0变化
	@Override
	public void transformPage(View page, float position) {
		//实现viewpager的切换动画效果
		//System.out.println("viewpager的界面切换动画状态："+position);
		
		//界面的宽度
		int width = page.getWidth();
		
		//因为我们的viewpager的动画效果是，当前界面和预加载界面进行动画，其他界面没有动画，那如何知道应该是当前界面和预加载界面执行动画呢？可以通过position进行判断
		//当前界面的position[0,-1]   预加载界面的position[1,0]
		//实现界面的旋转动画效果
		//参数：旋转的角度
		//page.setRotation(rotation);
		
		//判断哪个界面需要执行动画，并执行动画
		if (position < -1) {
			//不执行动画
			page.setRotation(0);
		}else if(position > 1){
			//不执行动画
			page.setRotation(0);
		}else if(position < 0){
			//符合[0,-1]，当前界面执行动画
			page.setRotation(position * MAXRADIUS);
			//设置旋转的中心点坐标
			page.setPivotX(width/2);//设置旋转中心点的x的坐标
			page.setPivotY(page.getHeight());
			
		}else if(position < 1){
			//符合[0,-1]，当前界面执行动画
			page.setRotation(position * MAXRADIUS);
			//设置旋转的中心点坐标
			page.setPivotX(width/2);//设置旋转中心点的x的坐标
			page.setPivotY(page.getHeight());
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
