package com.itheima.zhbj98.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.itheima.zhbj98.R;
import com.itheima.zhbj98.base.BaseMenuPager;
import com.itheima.zhbj98.bean.PhotosBean;
import com.itheima.zhbj98.bean.PhotosBean.PhotosItem;
import com.itheima.zhbj98.net.NetUrl;
import com.itheima.zhbj98.utils.Constants;
import com.itheima.zhbj98.utils.MyBitmapUtils;
import com.itheima.zhbj98.utils.SharedPreferencesUtil;


/**
 * 侧拉菜单组图条目的界面
 * 不使用Fragment，因为之前已经嵌套了两层Fragment，Fragment嵌套太多容易内容移除
 * 不使用Activity，因为Activity无法添加到Fragment中
 * 因为一个界面其实就是通过加载界面，初始化控件，将数据添加到控件中进行显示就可以了，可以使用普通的java，通过实现这些操作来充当界面
 * Author _ jjjzzzyyy
 * 2016年12月26日 上午10:29:34
 */
public class MenuPhotosPager extends BaseMenuPager implements OnClickListener{

	//1.因为新闻，专题等界面都要实现加载布局，显示数据操作，相同操作抽取父类
	
	private GridView mGridView;
	private ListView mListView;
	
	private List<PhotosItem> list = new ArrayList<PhotosItem>();
	private ImageView mPhotos;

	public MenuPhotosPager(Activity activity,ImageView photos) {
		super(activity);
		this.mPhotos = photos;
		this.mPhotos.setOnClickListener(this);
	}

	@Override
	public View initView() {
		view = View.inflate(activity, R.layout.menuphotospager, null);
		
		mListView = (ListView) view.findViewById(R.id.menuphotos_lv_listview);
		mGridView = (GridView) view.findViewById(R.id.menuphotos_gv_gridview);
		
		return view;
	}

	@Override
	public void initData() {
		//2.再次请求服务器，判断是否有缓存数据，有加载解析
		String sp_json = SharedPreferencesUtil.getString(activity, Constants.PHOTOS, "");
		if (!TextUtils.isEmpty(sp_json)) {
			processJson(sp_json);
		}
		getData();
	}

	/**
	 * 请求服务器获取组图数据
	 * 
	 * 2016年12月30日 下午2:52:11
	 */
	private void getData() {
		OkHttpClient okHttpClient = new OkHttpClient();
		Request request = new Request.Builder().url(NetUrl.PHOTOSURL).build();
		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				final String json = response.body().string();
				
				//1.请求服务器成功，获取数据，缓存数据
				SharedPreferencesUtil.saveString(activity, Constants.PHOTOS, json);
				
				//3.加载解析完缓存数据，获取最新的数据，加载解析最新的数，覆盖缓存数据显示
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						processJson(json);
					}
				});
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				
			}
		});
	}
	
	/**
	 * 解析json数据的操作
	 * 
	 * 2016年12月30日 下午2:56:45
	 */
	private void processJson(String json) {
		Gson gson = new Gson();
		PhotosBean photosBean = gson.fromJson(json, PhotosBean.class);
		
		list.clear();
		list = photosBean.data.news;
		
		//展示数据
		mListView.setAdapter(new Myadapter());
		mGridView.setAdapter(new Myadapter());
	}
	
	/**ListView的adapter**/
	private class Myadapter extends BaseAdapter{

		private MyBitmapUtils myBitmapUtils;

		public Myadapter(){
			myBitmapUtils = new MyBitmapUtils();
		}
		
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rootview;
			ViewHolder viewHolder;
			if (convertView == null) {
				rootview = View.inflate(activity, R.layout.menuphotos_item, null);
				viewHolder = new ViewHolder();
				viewHolder.mIcon = (ImageView) rootview.findViewById(R.id.item_iv_icon);
				viewHolder.mTitle = (TextView) rootview.findViewById(R.id.item_tv_title);
				rootview.setTag(viewHolder);
			}else{
				rootview = convertView;
				viewHolder = (ViewHolder) rootview.getTag();
			}
			PhotosItem photosItem = list.get(position);
			viewHolder.mTitle.setText(photosItem.title);
			
			//获取图片
			myBitmapUtils.getBitmap(viewHolder.mIcon, photosItem.listimage);
			
			return rootview;
		}
		
	}
	
	static class ViewHolder{
		ImageView mIcon;
		TextView mTitle;
	}

	/**标示listview是否显示**/
	private boolean isShowListView = true;
	@Override
	public void onClick(View v) {
		//隐藏显示listview和Gridview
		//如果listview显示，点击组图按钮，隐藏listview显示Gridview，并且更改组图按钮的图片
		//如果Gridview显示,点击组图按钮，隐藏GridView显示ListView,并且更改组图按钮的图片
		if (isShowListView) {
			mListView.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
			mPhotos.setImageResource(R.drawable.icon_pic_grid_type);
			isShowListView = false;
		}else{
			mGridView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mPhotos.setImageResource(R.drawable.icon_pic_list_type);
			isShowListView = true;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
