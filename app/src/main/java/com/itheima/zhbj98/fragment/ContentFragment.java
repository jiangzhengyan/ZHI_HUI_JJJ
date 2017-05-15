package com.itheima.zhbj98.fragment;

import com.itheima.zhbj98.R;
import com.itheima.zhbj98.base.BaseFragment;
import com.itheima.zhbj98.pagerfragment.GovFragment;
import com.itheima.zhbj98.pagerfragment.HomeFragment;
import com.itheima.zhbj98.pagerfragment.NewsCenterFragment;
import com.itheima.zhbj98.pagerfragment.SettingFragment;
import com.itheima.zhbj98.pagerfragment.VideoFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 内容页的Fragment Author _ jjjzzzyyy 2016年12月24日 上午10:23:40
 */
public class ContentFragment extends BaseFragment {

	// 1.因为菜单页和内容页都要初始化数据，加载布局，显示数据，相同操作，抽取到父类

	private RadioGroup mButtons;

	@Override
	public View initView() {

		view = View.inflate(activity, R.layout.contentfragment, null);
		return view;
	}

	@Override
	public void initData() {
		mButtons = (RadioGroup) view
				.findViewById(R.id.contentfragment_rg_buttons);

		// 1.设置默认显示首页的Fragment
		getChildFragmentManager()
				.beginTransaction()
				.replace(R.id.contentfragment_fl_pagers, new HomeFragment(),
						"HOME").commit();
		//设置默认选中首页按钮
		mButtons.check(R.id.contentfragment_rbtn_home);//设置选中那个Radiobutton，id:RadioButton的id
		
		//2.设置点击按钮切换显示按钮对应的Fragment界面
		//监听选中了那个Radiobutton
		mButtons.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//当Radiobutton被选中的时候调用的方法
			//checkedId : 选中的RadioButton的id
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				//判断选中了那个Radiobutton，切换显示相应的界面就可以了
				switch (checkedId) {
				case R.id.contentfragment_rbtn_home:
					//首页
					//replace : 将之前的Fragment删除，添加当前的Fragment
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fl_pagers, new HomeFragment(),
							"HOME").commit();
					
					setSlidingMenu(false);
					
					break;
				case R.id.contentfragment_rbtn_news:
					//新闻
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fl_pagers, new NewsCenterFragment(),
							"NEWSCENTER").commit();
					
					setSlidingMenu(true);
					
					break;
				case R.id.contentfragment_rbtn_video:
					//视频
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fl_pagers, new VideoFragment(),
							"VIDEO").commit();
					
					setSlidingMenu(true);
					
					break;
				case R.id.contentfragment_rbtn_gov:
					//政务
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fl_pagers, new GovFragment(),
							"GOV").commit();
					
					setSlidingMenu(true);
					
					break;
				case R.id.contentfragment_rbtn_setting:
					//设置
					getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.contentfragment_fl_pagers, new SettingFragment(),
							"SETTING").commit();
					
					setSlidingMenu(false);
					
					break;
				}
			}
		});
	}
	
	
	/**
	 * 设置侧拉菜单是否可以侧拉
	 * isSliding ： 是否可以侧拉的标示，true:可以侧拉，false:不可以侧拉
	 * 2016年12月24日 下午4:44:00
	 */
	private void setSlidingMenu(boolean isSliding){
		if (isSliding) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
