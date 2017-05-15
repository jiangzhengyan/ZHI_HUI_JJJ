package com.itheima.zhbj98;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.itheima.zhbj98.utils.Constants;
import com.itheima.zhbj98.utils.SharedPreferencesUtil;
import com.itheima.zhbj98.view.CustomVideoView;

import cn.sharesdk.framework.ShareSDK;

public class SplashActivity extends Activity {

	private CustomVideoView  mVideoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		//初始化sharesdk操作
		ShareSDK.initSDK(this);
		
		mVideoView = (CustomVideoView ) findViewById(R.id.splash_vv_videoview);
		//播放视频操作
		initData();
	}

	/**
	 * 播放视频
	 * 
	 * 2016年12月23日 上午10:33:35
	 */
	private void initData() {
		//1.获取播放资源
		//android.resource:// 跟 content://类似的含义：提供资源路径
		//"android.resource://"+this.getPackageName()+"/"+R.raw.kr36 : 获取相应应用程序中的raw中资源
		mVideoView.setVideoURI(Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.kr36));
		
		//3.监听视频播是否播放完毕，如果播放完毕，重新进行播放
		mVideoView.setOnCompletionListener(new OnCompletionListener() {
			//当视频播放完毕调用的方法
			@Override
			public void onCompletion(MediaPlayer mp) {
				mVideoView.start();
			}
		});
		
		//2.播放操作
		mVideoView.start();
	}
	
	
	/**
	 * 进入智慧jjj按钮的点击事件
	 * 
	 * 2016年12月23日 上午11:15:57
	 */
	public void center(View view){
		//判断用户是否是第一次进入，是：跳转到引导界面，不是：跳转到首页
		//问题：如何知道用户是否是第一次进入
		//可以通过获取保存的用户是否是第一次进入的状态来进行判断
		//当前是获取用户是否是第一次进入的状态，保存操作实在引导界面的开始体验按钮中进行实现
		boolean isfirstcenter = SharedPreferencesUtil.getBoolean(this, Constants.ISFIRSTCENTER, true);
		if (isfirstcenter) {
			//是第一次进入，跳转到引导界面
			startActivity(new Intent(this,GuideActivity.class));
		}else{
			//不是第一次进入，跳转到首页
			startActivity(new Intent(this,HomeActivity.class));
		}
		//跳转界面完成，销毁splash界面
		finish();
	}
	


	
}
