package com.itheima.zhbj98;

import java.util.List;

import com.bumptech.glide.Glide;
import com.itheima.zhbj98.bean.NewBean.News;
import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.exception.DbException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionActivity extends Activity {

	private ListView mListView;
	private ImageView mBack;
	private ImageView mMenu;
	private TextView mTitle;
	private List<News> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_collection);
		StatusBarUtil.setColor(this, Color.RED);
	 
		initView();
		initData();
	}

	/**
	 * 初始化控件
	 * 
	 * 2016年12月28日 上午11:09:13
	 */
	private void initView() {
		mTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		mMenu = (ImageView) findViewById(R.id.titlebar_iv_menu);
		mBack = (ImageView) findViewById(R.id.titlebar_iv_back);
		mListView = (ListView) findViewById(R.id.collection_lv_listview);
		
		mTitle.setText("收藏");
		mMenu.setVisibility(View.GONE);
		mBack.setVisibility(View.VISIBLE);
		
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//设置ListView的条目点击事件，实现跳转到新闻详情界面实现展示新闻的操作
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(CollectionActivity.this,NewsDetialActivity.class);
				intent.putExtra("url", list.get(position));
				//startActivity(intent);
				//当界面退出的时候，会调用之前的界面的onActivityResult方法
				startActivityForResult(intent, 0);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		initData();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 加载显示数据
	 * 
	 * 2016年12月28日 上午11:13:38
	 */
	private void initData() {
		//查询保存在数据库中的新闻信息，展示出来
		DaoConfig daoConfig = new DaoConfig(this);
		daoConfig.setDbName("zhbj98");
		DbUtils dbUtils = DbUtils.create(daoConfig);
		try {
			list = dbUtils.findAll(News.class);
			//通过listview展示数据
			if (list!=null && list.size() > 0) {
				mListView.setAdapter(new MyListViewAdapter());
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	
	private class MyListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
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
			String type = list.get(position).type;
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
					convertView = View.inflate(CollectionActivity.this, R.layout.menunewsitempager_listview_onimage, null);
				}
				//初始化控件，展示数据
				ImageView mIcon = (ImageView) convertView.findViewById(R.id.item_iv_icon);
				TextView mTitle = (TextView) convertView.findViewById(R.id.item_tv_title);
				TextView mTime = (TextView) convertView.findViewById(R.id.item_tv_time);
				
				News news = list.get(position);
				mTitle.setText(news.title);
				mTime.setText(news.pubdate);
				
				Glide.with(CollectionActivity.this).load(news.listimage).into(mIcon);
				
			}else if(itemViewType == 1){
				//显示多张图片的样式
				if (convertView == null) {
					convertView = View.inflate(CollectionActivity.this, R.layout.menunewsitempager_listview_imges, null);
				}
				ImageView mIcon1 = (ImageView) convertView.findViewById(R.id.item_iv_icon1);
				ImageView mIcon2 = (ImageView) convertView.findViewById(R.id.item_iv_icon2);
				ImageView mIcon3 = (ImageView) convertView.findViewById(R.id.item_iv_icon3);
				TextView mTime = (TextView) convertView.findViewById(R.id.item_tv_time);
				
				News news = list.get(position);
				mTime.setText(news.pubdate);
				
				Glide.with(CollectionActivity.this).load(news.listimage).into(mIcon1);
				Glide.with(CollectionActivity.this).load(news.listimage1).into(mIcon2);
				Glide.with(CollectionActivity.this).load(news.listimage2).into(mIcon3);
			}
			
			return convertView;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
