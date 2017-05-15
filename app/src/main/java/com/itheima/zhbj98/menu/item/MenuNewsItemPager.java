package com.itheima.zhbj98.menu.item;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.itheima.zhbj98.NewsDetialActivity;
import com.itheima.zhbj98.R;
import com.itheima.zhbj98.base.BaseMenuPager;
import com.itheima.zhbj98.bean.NewBean;
import com.itheima.zhbj98.bean.NewBean.News;
import com.itheima.zhbj98.net.NetUrl;
import com.itheima.zhbj98.utils.Constants;
import com.itheima.zhbj98.utils.SharedPreferencesUtil;
import com.itheima.zhbj98.view.RoolViewPager;
import com.viewpagerindicator.CirclePageIndicator;
/**
 * 新闻条目的子界面
 * Author _ jjjzzzyyy
 * 2016年12月26日 下午3:15:19
 */
public class MenuNewsItemPager extends BaseMenuPager {
	/**子界面的请求路径**/
	private String url;
	private PullToRefreshListView mReListView;
	private RoolViewPager mViewPager;
	private TextView mTitle;
	private CirclePageIndicator mIndicator;
	
	/**保存图片路径的**/
	private List<String> imageurls = new ArrayList<String>();
	/**保存文本**/
	private List<String> titles = new ArrayList<String>();
	
	/**存放ListView数据**/
	private List<News> mNews = new ArrayList<News>();
	
	/**保存已读新闻id的集合**/
	private List<String> reads = new ArrayList<String>();
	
	private MyPagerAdapter myPagerAdapter;
	private MyListViewAdapter myListViewAdapter;
	private View listviewView;
	private View viewPagerView;
	private Handler handler;
	private ListView mListView;
	private String longmoreUrl;
	
	public MenuNewsItemPager(Activity activity,String url) {
		super(activity);
		this.url = url;
	}

	@Override
	public View initView() {
		//加载viewpager和listview到单独布局文件中
		listviewView = View.inflate(activity, R.layout.menunewsitempager_listview, null);
		viewPagerView = View.inflate(activity, R.layout.menunewsitempager_viewpager, null);
		
		mReListView = (PullToRefreshListView) listviewView.findViewById(R.id.menunewsitempager_lv_listview);
		mViewPager = (RoolViewPager) viewPagerView.findViewById(R.id.menunewsitempager_vp_viewpager);
		mTitle = (TextView) viewPagerView.findViewById(R.id.menunewsitempager_tv_title);
		mIndicator = (CirclePageIndicator) viewPagerView.findViewById(R.id.menunewsitempager_cpi_indicator);
		
		//通过PullToRefreshListView获取到操作的listview
		mListView = mReListView.getRefreshableView();
		//可以将viewpager的布局，添加到操作的listview中
		mListView.addHeaderView(viewPagerView);
		
		return listviewView;
	}

	@Override
	public void initData() {
		
		
		//设置PullToRefresh的下拉刷新和上拉加载操作
		//下拉刷新操作
		mReListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				//获取新的数据，将原来显示数据覆盖
				getData(url,false);
			}
		});
		//上拉加载操作
		mReListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				//在原来的数据的基础上+新加载出来的数据一起显示
				getData(longmoreUrl,true);
			}
		});
		
		//设置listview条目点击事件，实现新闻的已读未读操作
		mReListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//因为ListView添加有刷新和viewpager，相当于listview多出了两个条目，所以在操作的时候要减去这个两个条目
				if (mNews.get(position-2).isRead == false) {
					mNews.get(position-2).isRead = true;
					myListViewAdapter.notifyDataSetChanged();
					
					//点击更改新闻为已读样式之后，还需要将已读新闻保存起来，方便再次进入的时候进行回显标示
					//获取原先保存的已读新闻的id
					String newsread = SharedPreferencesUtil.getString(activity, Constants.NEWSREAD, "");
					//将原先的已读新闻id和最新的已读新闻的id拼接一起进行保存
					//#id#id#id
					SharedPreferencesUtil.saveString(activity, Constants.NEWSREAD, newsread+"#"+mNews.get(position-2).id);
				}
				//跳转到新闻的详情页面，展示新闻的详情操作
				Intent intent = new Intent(activity,NewsDetialActivity.class);
				//因为新闻详情页需要展示网页，同时又收藏功能，会收藏新闻的图标名称等信息，所以最好是传递一个bean类给新闻详情页面
				//如果想要通过intent传递一个对象，需要将该对象序列化
				//集合序列化操作：(Serializable)mNews
				//bean类序列化：News implements Serializable
				intent.putExtra("url", mNews.get(position-2));
				activity.startActivity(intent);
			}
		});
		
		
		//当进入界面的时候，获取保存的已读新闻的id，修改新闻的样式
		String newsread = SharedPreferencesUtil.getString(activity, Constants.NEWSREAD, "");
		if (!TextUtils.isEmpty(newsread)) {
			reads.clear();
			//#id#id#id
			String[] split = newsread.split("#");
			for (int i = 0; i < split.length; i++) {
				reads.add(split[i]);
			}
		}
		

		//2.再次请求服务器，判断是否有缓存信息，有加载显示
		String sp_json = SharedPreferencesUtil.getString(activity, NetUrl.SERVERURL+url, "");
		if (!TextUtils.isEmpty(sp_json)) {
			processJson(sp_json,false);
		}
		getData(url,false);
		
	}

	/**
	 * 请求服务器数据
	 * url : 请求服务器的路径
	 * 2016年12月26日 下午4:42:15
	 */
	private void getData(final String url,final boolean isLoadMore) {
		
		//判断请求路径是否为空，如果为空，提醒用户没有数据，并停止刷新
		if (!TextUtils.isEmpty(url)) {
			OkHttpClient okHttpClient = new OkHttpClient();
			Request request = new Request.Builder().url(NetUrl.SERVERURL+url).build();
			Call call = okHttpClient.newCall(request);
			call.enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, Response response) throws IOException {
					final String json = response.body().string();
					//System.out.println("获取子界面的数据："+json);
					//1.请求数据成功，缓存数据
					//因为所有的子界面使用的都是MenuNewsItemPager界面，所以使用的是同一个请求服务器的操作，如果保存数据的key是一样的话，会出现，缓存数据被覆盖的问题
					SharedPreferencesUtil.saveString(activity, NetUrl.SERVERURL+url, json);
					//3.加载解析最新的数据，将缓存数据覆盖展示
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							processJson(json,isLoadMore);
						}
					});
					
				}
				
				@Override
				public void onFailure(Call arg0, IOException arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}else{
			Toast.makeText(activity, "没有更多数据了", 0).show();
			mReListView.onRefreshComplete();
		}
		
		
	}
	
	/**
	 * 解析json数据操作
	 * 
	 * 2016年12月26日 下午4:49:30
	 */
	private void processJson(String json,boolean isloadmore) {
		
		System.out.println(json);
		
		Gson gson = new Gson();
		NewBean newBean = gson.fromJson(json, NewBean.class);
		//System.out.println("获取子界面的数据："+newBean.data.topnews.get(0).title);
		
		//展示数据
		showMSG(newBean,isloadmore);
	}

	/**
	 * 展示数据
	 * isloadmore : 是否是加载更多操作，true:是，false:不是
	 * 2016年12月27日 上午9:36:10
	 */
	private void showMSG(NewBean newBean,boolean isloadmore) {
		
		//获取加载更多的路径
		longmoreUrl = newBean.data.more;
		
		//如果是下拉刷新，需要覆盖原来的数据，显示最新的数据，所以更新viewpager没有问题
		//但是如果是上拉加载，是在原来的数据的基础上，往后添加加载出来的新的数据，不会改变原来的数据
		if (!isloadmore) {
			//1.展示viewpager的数据
			if (newBean.data.topnews.size() > 0) {
				imageurls.clear();
				titles.clear();
				//1.1.获取viewpager展示的数据，图片路径和标题文本
				for (int i = 0; i < newBean.data.topnews.size(); i++) {
					imageurls.add(newBean.data.topnews.get(i).topimage);
					titles.add(newBean.data.topnews.get(i).title);
				}
				//1.2.通过viewpager展示数据
				if (myPagerAdapter == null) {
					myPagerAdapter = new MyPagerAdapter();
					mViewPager.setAdapter(myPagerAdapter);
				}else{
					myPagerAdapter.notifyDataSetChanged();
				}
				//1.3.将viewpager和第三方框架设置的点关联，实现点随着viewpager切换而切换
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);//使用快照的形式展示点
				
				//5.设置viewpager界面切换的时候，更改显示文本
				mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
					
					@Override
					public void onPageSelected(int position) {
						//切换到第几个界面，显示第几个界面的文本
						mTitle.setText(titles.get(position));
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
				
				//1.4.初始化显示第一个图片，第一个文本，第一个点
				mTitle.setText(titles.get(0));
				mIndicator.onPageSelected(0);//设置切换选中那个点，position：点的索引
				mViewPager.setCurrentItem(0);//设置viewpager当前显示的条目，参数：条目的索引
				
				//4.viewpager自动滑动
				//因为Handler是在showMSG方法中，而showMSG方法processJson中调用，processJson会在缓存和请求到最新数据的时候调用，所以会出现发送两次延迟消息问题
				if (handler == null) {
					handler = new Handler(){
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							//实现切换viewpager界面
							//获取当前显示界面的索引
							int currentItem = mViewPager.getCurrentItem();
							//计算下一个界面的索引
							//判断如果切换到最后一个界面，需要重新切换回第一个界面进行操作
							if (currentItem == imageurls.size()-1) {
								currentItem=0;
							}else{
								currentItem++;
							}
							//设置viewpager显示下一个界面
							mViewPager.setCurrentItem(currentItem);
							//一次切换完成，还需要接着执行第二次
							handler.sendEmptyMessageDelayed(0, 3000);
						}
					};
					handler.sendEmptyMessageDelayed(0, 3000);//延迟3秒个handler发送消息
				}
				
				//3.将viewpager的布局添加ListView中
				//getHeaderViewsCount() : 获取添加在listview头部的view对象的个数
				/*if (mReListView.getHeaderViewsCount() < 1) {
					mReListView.addHeaderView(viewPagerView);//给listview的头部添加view对象
				}*/
				
			}
		}
		
		//2.展示ListView的数据
		if (newBean.data.news.size() > 0) {
			//mNews = newBean.data.news;
			//如果是上拉加载：原数据+新数据
			//如果是下拉刷新：覆盖原数据
			if (isloadmore) {
				//合并集合
				//A[1,2,3]
				//B[4,5,6]   A.addAll(B)  A[1,2,3,4,5,6]
				mNews.addAll(newBean.data.news);
			}else{
				mNews = newBean.data.news;
			}
			
			//判断需要展示的数据中有已读的新闻
			for (News info : mNews) {
				if (reads.contains(info.id)) {
					info.isRead = true;
				}else{
					info.isRead = false;
				}
			}
			
			if (myListViewAdapter == null) {
				myListViewAdapter = new MyListViewAdapter();
				mListView.setAdapter(myListViewAdapter);
			}else{
				myListViewAdapter.notifyDataSetChanged();
			}
		}
		//加载完数据，设置停止listview刷新数据
		mReListView.onRefreshComplete();
	}
	
	/**viewpager的adapter**/
	private class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return imageurls.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			//1.获取图片
			//2.设置ImageView,方便展示图片
			//3.将imageView添加到viewpager中展示
			
			View rootview = View.inflate(activity, R.layout.menunewsitempager_viewpager_item, null);
			ImageView mIcon = (ImageView) rootview.findViewById(R.id.item_iv_icon);
			
			//根据图片的路径获取图片，将图片存放imageview中展示
			Glide.with(activity.getApplicationContext()).load(imageurls.get(position)).into(mIcon);
		
			container.addView(rootview);
			
			//设置viewpager条目界面的触摸事件，实现按下viewpager停止滑动，抬起viewpager重新滑动
			rootview.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						//停止发送handler延迟消息
						handler.removeCallbacksAndMessages(null);//参数如果是null，表示删除所有的延迟消息
						break;
					case MotionEvent.ACTION_UP:
						//重新发送handler延迟消息，实现viewpager自动滑动
						handler.sendEmptyMessageDelayed(0, 3000);
						break;
					case MotionEvent.ACTION_CANCEL:
						//当取消view的事件的时候，重新让viewpager自动滑动
						handler.sendEmptyMessageDelayed(0, 3000);
						break;
					}
					//返回true:执行触摸监听操作，false：不执行
					return true;
				}
			});
			
			return rootview;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//super.destroyItem(container, position, object);
			container.removeView((View) object);
		}
	}
	
	/**ListView的adapter**/
	private class MyListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mNews.size();
		}

		@Override
		public Object getItem(int position) {
			return mNews.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		//设置listview显示几种条目样式
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		
		//根据条目的索引，确定条目显示那种样式
		//position : 条目的索引
		@Override
		public int getItemViewType(int position) {
			//根据条目的索引，获取条目的type类型，根据type类型决定条目显示那种样式
			String type = mNews.get(position).type;
			if ("0".equals(type)) {
				//显示单张图片的样式
				return 0;
			}else{
				//显示多张图片的样式
				return 1;
			}
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			//根据条目的索引获取条目要显示的样式
			int itemViewType = getItemViewType(position);
			if (itemViewType == 0) {
				//显示单张图片的样式
				if (convertView == null) {
					convertView = View.inflate(activity, R.layout.menunewsitempager_listview_onimage, null);
				}
				//初始化控件，展示数据
				ImageView mIcon = (ImageView) convertView.findViewById(R.id.item_iv_icon);
				TextView mTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
				TextView mTime = (TextView) convertView.findViewById(R.id.item_tv_time);
				
				News news = mNews.get(position);
				mTitle.setText(news.title);
				mTime.setText(news.pubdate);
				
				Glide.with(activity).load(news.listimage).into(mIcon);
				
				//根据新闻是否已读的标示，设置条目的样式
				if (news.isRead) {
					mTitle.setTextColor(Color.RED);
				}else{
					mTitle.setTextColor(Color.BLACK);
				}
				
			}else if(itemViewType == 1){
				//显示多张图片的样式
				if (convertView == null) {
					convertView = View.inflate(activity, R.layout.menunewsitempager_listview_imges, null);
				}
				ImageView mIcon1 = (ImageView) convertView.findViewById(R.id.item_iv_icon1);
				ImageView mIcon2 = (ImageView) convertView.findViewById(R.id.item_iv_icon2);
				ImageView mIcon3 = (ImageView) convertView.findViewById(R.id.item_iv_icon3);
				TextView mTime = (TextView) convertView.findViewById(R.id.item_tv_time);
				
				News news = mNews.get(position);
				mTime.setText(news.pubdate);
				
				Glide.with(activity).load(news.listimage).into(mIcon1);
				Glide.with(activity).load(news.listimage1).into(mIcon2);
				Glide.with(activity).load(news.listimage2).into(mIcon3);
				
				//根据新闻是否已读的标示，设置条目的样式
				if (news.isRead) {
					mTime.setTextColor(Color.RED);
				}else{
					mTime.setTextColor(Color.BLACK);
				}
			}
			
			return convertView;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
