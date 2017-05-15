package com.itheima.zhbj98.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.zhbj98.R;
import com.itheima.zhbj98.SelectItemActivity;
import com.itheima.zhbj98.base.BaseMenuPager;
import com.itheima.zhbj98.bean.NewsCenterInfo.NewsCenterDataInfo;
import com.itheima.zhbj98.menu.item.MenuNewsItemPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;


/**
 * 侧拉菜单新闻条目的界面
 * 不使用Fragment，因为之前已经嵌套了两层Fragment，Fragment嵌套太多容易内容移除
 * 不使用Activity，因为Activity无法添加到Fragment中
 * 因为一个界面其实就是通过加载界面，初始化控件，将数据添加到控件中进行显示就可以了，可以使用普通的java，通过实现这些操作来充当界面
 * Author _ jjjzzzyyy
 * 2016年12月26日 上午10:29:34
 */
public class MenuNewsPager extends BaseMenuPager{

	//1.因为新闻，专题等界面都要实现加载布局，显示数据操作，相同操作抽取父类
	
	private NewsCenterDataInfo mDataInfo;
	private ViewPager mViewPager;
	private ImageView mSelect;
	private TabPageIndicator mIndicator;
	
	/**保存viewpager填充的子界面**/
	private List<MenuNewsItemPager> itemPagers;
	/**存放标签显示的文本**/
	private List<String> titles;
	private Myadapter myadapter;
	
	public MenuNewsPager(Activity activity,NewsCenterDataInfo dataInfo) {
		super(activity);
		this.mDataInfo = dataInfo;
	}

	@Override
	public View initView() {
		view = View.inflate(activity, R.layout.menunewspager, null);
		return view;
	}

	@Override
	public void initData() {
		//初始化控件
		mIndicator = (TabPageIndicator) view.findViewById(R.id.menunewspager_tpi_indicator);
		mSelect = (ImageView) view.findViewById(R.id.menunewspager_iv_select);
		mViewPager = (ViewPager) view.findViewById(R.id.menunewspager_vp_viewpager);
		
		itemPagers = new ArrayList<MenuNewsItemPager>();
		itemPagers.clear();
		titles = new ArrayList<String>();
		titles.clear();
		
		//获取数据展示数据
		//获取数据，将标签的数据获取出来保存集合中，根据标签的个数创建相应个数的条目子界面，方便填充到viewpager中展示
		for (int i = 0; i < mDataInfo.children.size(); i++) {
			itemPagers.add(new MenuNewsItemPager(activity,mDataInfo.children.get(i).url));
			titles.add(mDataInfo.children.get(i).title);
		}
		
		//展示数据
		if (myadapter == null) {
			myadapter = new Myadapter();
			mViewPager.setAdapter(myadapter);
		}else{
			myadapter.notifyDataSetChanged();
		}
		//将viewpager和标签关联起来
		mIndicator.setViewPager(mViewPager);//将viewpager和标签关联
		
		//设置viewpager的界面切换监听，当切换到北京的时候，可以侧拉侧拉菜单，如果不是北京，不可以进行侧拉菜单操作
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//判断是否切换到北京界面，如果是，可以侧拉菜单，如果不是，不能侧拉菜单
				if (position == 0) {
					//可以侧拉菜单
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}else{
					//不能侧拉菜单
					slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		//设置选择按钮的点击事件，实现跳转到拖拽界面
		mSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,SelectItemActivity.class);
				//需要将标签文本的数据，传递给拖拽界面，方便展示条目
				intent.putExtra("titles", (Serializable)titles);
				activity.startActivity(intent);
			}
		});
	}
	
	/**ViewPager的adapter**/
	private class Myadapter extends PagerAdapter{

		//设置滑动标签的文本
		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position);
		}
		
		@Override
		public int getCount() {
			return itemPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//根据条目的索引获取到对应的界面
			MenuNewsItemPager menuNewsItemPager = itemPagers.get(position);
			//将界面的view对象添加到viewpager中
			container.addView(menuNewsItemPager.view);
			//展示界面完成，填充数据
			menuNewsItemPager.initData();
			return menuNewsItemPager.view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
