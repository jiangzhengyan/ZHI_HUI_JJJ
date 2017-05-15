package com.itheima.zhbj98;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.itheima.zhbj98.fragment.ContentFragment;
import com.itheima.zhbj98.fragment.MenuFragment;
import com.jaeger.library.StatusBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;

//1.activity继承SlidingFragmentActivity
public class HomeActivity extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		StatusBarUtil.setColor(this, Color.RED);
		initSlidingMenu();
		initFragment();
	}

	/**
	 * 添加侧拉菜单
	 * 
	 * 2016年12月24日 上午9:38:40
	 */
	private void initSlidingMenu() {
		// 2.获取SlidingMenu对象，进行侧拉菜单的配置
		// 2.1.获取侧拉菜单对象
		SlidingMenu slidingMenu = getSlidingMenu();
		// 2.2.设置侧拉的类型
		// slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingMenu.setMode(SlidingMenu.LEFT);
		// 2.3.设置触摸类型
		// TOUCHMODE_FULLSCREEN : 全屏触摸
		// TOUCHMODE_MARGIN : 边缘触摸
		// TOUCHMODE_NONE : 不可触摸
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// 2.4.设置侧拉菜单的布局，在HomeActivity的左侧添加一个菜单页布局
		setBehindContentView(R.layout.menu);
		// 2.5.设置菜单页宽度
		// slidingMenu.setBehindWidth(300);//单位是px
		// 2.6.设置内容页的宽度
		
		//先求出200占用320的比例，然后求出比例在720中的宽度
		//200/320 * 手机界面的宽度
		WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		int width = windowManager.getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(200 * width / 320);// 单位是px
		
		// 2.7.设置分割线的样式
		slidingMenu
				.setShadowDrawable(R.drawable.shape_homeactiviyt_slidingmenu_shadow);
		// 设置分割线的宽度，单位是px
		slidingMenu.setShadowWidth(5);
		/*
		 * //2.8.设置右边菜单页的布局 slidingMenu.setSecondaryMenu(R.layout.menu_right);
		 * //2.9.设置右边菜单页的分割线,共享左侧菜单页分割线宽度
		 * slidingMenu.setSecondaryShadowDrawable(
		 * R.drawable.shape_homeactiviyt_slidingmenu_shadow);
		 */
	}

	/**
	 * 添加Fragment到HomeActivity
	 * 
	 * 2016年12月24日 上午10:49:16
	 */
	private void initFragment() {
		// 1.获取Fragment的管理者
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		// 2.获取事务
		FragmentTransaction beginTransaction = fragmentManager
				.beginTransaction();
		// 3.添加Fragment
		// 参数1：添加Fragment的布局文件的id
		// 参数2：添加的Fragment
		// 参数3：添加的Fragment的标示，方便后期可以通过标示找到添加的Fragment
		beginTransaction.replace(R.id.home_root, new ContentFragment(),
				"CONTENT");
		beginTransaction.replace(R.id.menu_root, new MenuFragment(), "MENU");
		// 4.提交事务，完成添加操作
		beginTransaction.commit();

		// 通过标示找到添加的Fragment
		// fragmentManager.findFragmentByTag(tag);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
