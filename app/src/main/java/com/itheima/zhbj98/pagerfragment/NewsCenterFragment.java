package com.itheima.zhbj98.pagerfragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.itheima.zhbj98.base.BaseMenuPager;
import com.itheima.zhbj98.base.BasePagerFragment;
import com.itheima.zhbj98.bean.EventBusMsgInfo;
import com.itheima.zhbj98.bean.NewsCenterInfo;
import com.itheima.zhbj98.menu.MenuActionPager;
import com.itheima.zhbj98.menu.MenuNewsPager;
import com.itheima.zhbj98.menu.MenuPhotosPager;
import com.itheima.zhbj98.menu.MenuSpecialPager;
import com.itheima.zhbj98.net.NetUrl;
import com.itheima.zhbj98.utils.Constants;
import com.itheima.zhbj98.utils.SharedPreferencesUtil;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 新闻中心的Fragment
 * Author _ jjjzzzyyy
 * 2016年12月24日 下午3:10:47
 */
public class NewsCenterFragment extends BasePagerFragment {

	/**保存侧拉菜单条目对应的界面**/
	private List<BaseMenuPager> pagers;
	
	private String[] TITLES = new String[] { "新闻", "专题", "组图", "互动" };
	
	//1.首页，新闻等Fragment界面都要加载界面，显示数据，相同操作，抽取父类
	
	
	//子类重写父类的方法，根据自己的需要对相应的方法进行操作
	@Override
	public View initView() {
		// TODO Auto-generated method stub
		return super.initView();
	}
	
	
	@Override
	public void initData() {
		//显示内容
		/*TextView textView = new TextView(activity);
		textView.setText("新闻中心");
		textView.setTextSize(23);
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);
		//添加到父类的内容显示区域显示
		mContent.addView(textView);*/
		//设置父类的标题显示
		//mTitle.setText("新闻");
		//设置父类中的菜单页按钮是隐藏操作
		mMenu.setVisibility(View.VISIBLE);
		
		
		//2.再次请求服务器，判断是有缓存数据，有，加载显示缓存数据
		String sp_news = SharedPreferencesUtil.getString(activity, Constants.NEWSCENTERINFO, "");
		if (!TextUtils.isEmpty(sp_news)) {
			processJson(sp_news);
		}
		getData();
		
		super.initData();
	}

	/**
	 * 请求服务器获取数据
	 * 
	 * 2016年12月24日 下午5:04:52
	 */
	private void getData() {
		//联网操作，权限，子线程(第三框架一般都会封装到子线程，直接可以使用)
		
		//1.创建请求的客户端
		OkHttpClient okHttpClient = new OkHttpClient();
		//设置请求体操作
		Request request = new Request.Builder().url(NetUrl.NEWSCENTERURL).build();
		//2.创建请求
		//参数：设置请求体，可以设置请求路径等等参数
		Call call = okHttpClient.newCall(request);
		//3.请求服务器，获取服务器返回的数据
		call.enqueue(new Callback() {
			//请求成功调用的方法
			//response : 封装服务器返回的数据
			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				//获取服务器返回的数据
				final String json = response.body().string();
				//System.out.println("服务器返回的数据："+json);
				//1.将请求成功的数据，保存到sp
				SharedPreferencesUtil.saveString(activity, Constants.NEWSCENTERINFO, json);
				//3.加载显示最新数据，将缓存数据覆盖
				//细节：okhttp的回调是在子线程中执行的
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						processJson(json);
					}
				});
				
			}
			//请求失败调用的方法
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				
			}
		});
	}
	
	/**
	 * 解析json数据的操作
	 * 
	 * 2016年12月26日 上午9:40:43
	 */
	private void processJson(String json) {
		Gson gson = new Gson();
		//解析json数据
		NewsCenterInfo newsCenterInfo = gson.fromJson(json, NewsCenterInfo.class);
		System.out.println("服务器返回的数据："+newsCenterInfo.data.get(0).title);
		
		//将侧拉菜单条目对应的界面添加到NewsCenterFragment中
		addMenuPager(newsCenterInfo);
	}

	/**
	 * 添加侧拉菜单条目对应的界面
	 * 
	 * 2016年12月26日 上午10:48:18
	 */
	private void addMenuPager(NewsCenterInfo newsCenterInfo) {
		pagers =  new ArrayList<BaseMenuPager>();
		pagers.clear();
		pagers.add(new MenuNewsPager(activity,newsCenterInfo.data.get(0)));
		pagers.add(new MenuSpecialPager(activity));
		pagers.add(new MenuPhotosPager(activity,mPhotos));
		pagers.add(new MenuActionPager(activity));
		
		//设置刚进入界面的时候，默认显示新闻界面
		switchPager(0);
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		//注册EventBus
		//判断Eventbus是否注册
		if (!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}
		
	}
	
	@Override
	public void onStop() {
		super.onStop();
		//注销操作
		if (EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);
		}
	}
	
	
	//创建接受EventBus发送的消息的方法
	//POSTING : 默认的模式，发送和接受操作都在一个线程中执行
	//MAIN : 不管在哪个线程发送消息，都在UI线程接受
	//BACKGROUND : 如果在子线程发送，接受也要在该子线程，如果在UI线程发送，要创建一个子线程接受
	//ASYNC : 接受和发送都在子线程执行
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void getEventBusMsg(EventBusMsgInfo busMsgInfo){
		//调用切换界面的方法，实现切换界面
		switchPager(busMsgInfo.position);
	}
	
	
	/**
	 * 提供给MenuFragment菜单使用的，但是MenuFragment和NewsCenterFragment没有关系，可以通过广播接受者来实现
	 * 切换侧拉菜单条目对应界面
	 * position:侧拉菜单条目索引，方便根据索引找到条目对应的界面，进行切换展示操作
	 * 
	 * 2016年12月26日 上午11:12:53
	 */
	public void switchPager(int position){
		//1.切换标题
		mTitle.setText(TITLES[position]);
		//2.切换界面
		//根据条目的索引，找到相应的界面
		BaseMenuPager baseMenuPager = pagers.get(position);
		//将内容显示区域显示的界面全部清除
		mContent.removeAllViews();
		//界面要添加到内容显示区域进行显示
		mContent.addView(baseMenuPager.view);
		//展示数据
		baseMenuPager.initData();
		
		//切换界面，判断是否切换到组图界面，切换到显示组图按钮，没有切换到隐藏组图按钮
		//判断position是否为2
		//判断界面的类型是否是MenuPhotospager
		//instanceof : 判断前边的内容是否是后面的类型
		if (baseMenuPager instanceof MenuPhotosPager) {
			mPhotos.setVisibility(View.VISIBLE);
		}else{
			mPhotos.setVisibility(View.GONE);
		}
	}
	
	
	
	
	
	
	
	
	
}
