package com.itheima.zhbj98.view;

import java.io.IOException;

import com.itheima.zhbj98.R;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.net.rtp.AudioStream;
import android.os.Handler;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * 播放界面，因为有播放操作，所以不能继承任何控件进行实现，只能自己去写
 * Author _ jjjzzzyyy
 * 2016年12月31日 上午10:13:46
 */
public class MyMediaPlayer implements OnClickListener{

	private TextureView mPlay;
	private ProgressBar mLoading;
	private ImageView mPlayButton;
	private View view;
	
	private Surface surface;
	private MediaPlayer mediaPlayer;
	
	private Context mContext;

	//加载播放界面，因为播放操作，是在播放界面中进行的
	public MyMediaPlayer(Context context){
		this.mContext = context;
		//加载播放界面
		view = View.inflate(context, R.layout.videofragment_listview_play, null);
		mPlay = (TextureView) view.findViewById(R.id.item_ttv_play);
		mLoading = (ProgressBar) view.findViewById(R.id.item_pb_loading);
		mPlayButton = (ImageView) view.findViewById(R.id.item_iv_playbutton);
		
		//在播放视频之前要确保视频播放界面已经加载完成，并且获取到视频播放界面和视频播放操作，才能播放
		//监听视频播放界面操作
		mPlay.setSurfaceTextureListener(new SurfaceTextureListener() {
			
			//视频播放界面加载完成调用的方法
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture s, int width,
					int height) {
				//根据SurfaceTexture获取播放界面
				surface = new Surface(s);
				mediaPlayer = new MediaPlayer();
			}
			
			//视频播放界面更新的操作
			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture surface) {
				// TODO Auto-generated method stub
				
			}
			
			//视频播放界面大小改变的时候调用的方法
			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
					int height) {
				// TODO Auto-generated method stub
				
			}
			
			//视频播放界面销毁的时候调用
			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		
		mPlayButton.setOnClickListener(this);
	}
	
	/**
	 * 提供给ListViewItem调用，方便将播放界面，添加到自定义控件中展示
	 * 
	 * 2016年12月31日 上午10:23:36
	 */
	public View getRootView(){
		return view;
	}
	
	/**
	 * 提供给外部调用的，用来实现播放是操作
	 * 
	 * 2016年12月31日 上午10:31:12
	 */
	public void beginPlay(final String url){
		//显示进度条
		mLoading.setVisibility(View.VISIBLE);
		//延迟1秒钟进行播放的
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				play(url);
			}
		}, 1000);
	}

	/**
	 * 播放视频操作
	 * 
	 * 2016年12月31日 上午10:32:55
	 */
	protected void play(String url) {
		//实现播放视频操作
		try {
			mediaPlayer.setDataSource(mContext, Uri.parse(url));//加载播放资源
			mediaPlayer.setSurface(surface);//设置视频播放界面
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放类型
			mediaPlayer.setLooping(true);//设置循环播放
			mediaPlayer.prepareAsync();//异步准备操作
			//监听视频是否准备完毕
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				//准备完毕调用的方法
				@Override
				public void onPrepared(MediaPlayer mp) {
					//隐藏进度条，实现播放操作
					mLoading.setVisibility(View.GONE);
					mediaPlayer.start();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_iv_playbutton:
			//播放暂停操作
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				//暂停播放，按钮图片改为播放图片，提醒用户点击可以播放
				mediaPlayer.pause();//暂停播放视频
				mPlayButton.setImageResource(R.drawable.play_icon);
			}else{
				//播放操作
				mediaPlayer.start();
				mPlayButton.setImageResource(R.drawable.pause_video);
			}
			break;
		}
	}
	
	
	/**
	 * 停止播放，提供给外部调用的
	 * 
	 * 2016年12月31日 上午11:13:38
	 */
	public void release(){
		mediaPlayer.release();//停止播放
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
