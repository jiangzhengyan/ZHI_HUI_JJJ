package com.itheima.zhbj98.fragment;

import org.greenrobot.eventbus.EventBus;

import com.itheima.zhbj98.HomeActivity;
import com.itheima.zhbj98.R;
import com.itheima.zhbj98.base.BaseFragment;
import com.itheima.zhbj98.bean.EventBusMsgInfo;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 菜单页的Fragment
 * Author _ jjjzzzyyy
 * 2016年12月24日 上午10:23:40
 */
public class MenuFragment extends BaseFragment {

	private ListView mListView;
	
	private String[] TITLES = new String[] { "新闻", "专题", "组图", "互动" };

	private Myadapter myadapter;
	
	/**保存被点击的条目的索引**/
	private int currentposition;

	@Override
	public View initView() {
		
		view = View.inflate(activity, R.layout.menufragment, null);
		return view;
	}

	@Override
	public void initData() {
		//1.初始化控件，显示数据
		mListView = (ListView) view.findViewById(R.id.menufragment_lv_listview);
		
		currentposition = 0;
		
		//2.设置ListView的adapter显示数据
		if (myadapter == null) {
			myadapter = new Myadapter();
			mListView.setAdapter(myadapter);//如果给listview重写设置adapter，相当于重新加载adapter重新显示数据
		}else{
			myadapter.notifyDataSetChanged();
		}
		
		//3.设置ListView的条目点击事件，完成点击条目更改条目样式的操作
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//更改被点击的条目的样式
				currentposition = position;
				myadapter.notifyDataSetChanged();//更新界面，执行该方法，就会重新调用adapter的getCount和getView方法
				
				//关闭侧拉菜单
				slidingMenu.toggle();//如果侧拉菜单是关闭的，执行打开，如果是打开，执行关闭
				
				//切换新闻中心Fragment中的界面，通过EventBus发送一个消息即可
				//参数：保存传递的数据的类
				EventBus.getDefault().post(new EventBusMsgInfo(position));
			}
		});
		
	}

	/**ListView的adapter**/
	private class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//加载条目的布局
			View rootview = View.inflate(activity, R.layout.menufragment_listview_item, null);
			ImageView mIcon = (ImageView) rootview.findViewById(R.id.item_iv_icon);
			TextView mTitle = (TextView) rootview.findViewById(R.id.item_tv_title);
			
			mTitle.setText(TITLES[position]);
			
			//判断被点击的条目是否和当前的条目一致，一致:更改样式，不一致：改成白色
			if (currentposition == position) {
				mIcon.setImageResource(R.drawable.menu_arr_select);
				mTitle.setTextColor(Color.RED);
			}else{
				mIcon.setImageResource(R.drawable.menu_arr_normal);
				mTitle.setTextColor(Color.WHITE);
			}
			
			return rootview;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
