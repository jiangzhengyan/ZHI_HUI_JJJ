package com.itheima.zhbj98.pagerfragment;

import com.itheima.zhbj98.R;
import com.itheima.zhbj98.base.BasePagerFragment;
import com.itheima.zhbj98.view.ListViewItem;
import com.itheima.zhbj98.view.MyMediaPlayer;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 视频的Fragment
 * Author _ jjjzzzyyy
 * 2016年12月24日 下午3:10:47
 */
public class VideoFragment extends BasePagerFragment {

	
	private String url = "http://flv2.bn.netease.com/videolib3/1604/28/fVobI0704/SD/fVobI0704-mobile.mp4";
	//1.首页，新闻等Fragment界面都要加载界面，显示数据，相同操作，抽取父类
	
	
	private ListView mListView;
	
	/**保存当前播放视频的 界面**/
	private ListViewItem currentView;


	//子类重写父类的方法，根据自己的需要对相应的方法进行操作
	@Override
	public View initView() {
		// TODO Auto-generated method stub
		return super.initView();
	}
	
	
	@Override
	public void initData() {
		//显示内容
		
		view = View.inflate(activity, R.layout.videofragment, null);
		mListView = (ListView) view.findViewById(R.id.videofragment_lv_listview);
		
		//添加到父类的内容显示区域显示
		mContent.addView(view);
		//设置父类的标题显示
		mTitle.setText("视频");
		//设置父类中的菜单页按钮是隐藏操作
		mMenu.setVisibility(View.VISIBLE);
		
		mListView.setAdapter(new Myadapter());
		
		super.initData();
	}
	
	/**ListView的adapter**/
	private class Myadapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			//不使用getViewTypeCount原因：getViewTypeCount是控制listview显示几种条目样式，但是一个条目只能显示一种样式，只不过是几个条目显示的样式不一样
			//显示操作是，一个条目要动态的显示两种样式
			//可以通过自定义控件作为条目，在自定义控件中实现动态的切换显示不同的样式
			
			//将自定义控件作为listview的条目展示
			if (convertView == null) {
				final ListViewItem viewItem = new ListViewItem(activity);
				
				//获取播放按钮，实现播放按钮的点击事件
				viewItem.getPlayButton().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//点击播放按钮，显示视频播放界面
						
						//点击条目按钮实现播放操作的时候，先判断之前有没有条目在进行播放，有停止播放，播放当前条目的视频
						if (currentView != null) {
							currentView.removeVideoView();
						}
						
						//将播放界面，添加到自定义的控件中展示
						MyMediaPlayer myMediaPlayer = new MyMediaPlayer(activity);
						
						//调用播放操作进行视频的播放
						myMediaPlayer.beginPlay(url);
						
						viewItem.addVideoView(myMediaPlayer);
						
						//保存播放界面
						currentView = viewItem;
					}
				});
				
				convertView = viewItem;
			}else{
				//在复用缓存的时候，判断复用的view对象是否在播放视频，播放，停止播放即可
				if (currentView != null && currentView == convertView) {
					currentView.removeVideoView();
				}
			}
			
			return convertView;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
