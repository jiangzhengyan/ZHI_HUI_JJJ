package com.itheima.zhbj98.view;

import com.itheima.zhbj98.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
/**
 * 自定义视频播放界面listview的条目样式
 * Author _ jjjzzzyyy
 * 2016年12月31日 上午9:44:48
 */
public class ListViewItem extends FrameLayout {


	private ImageView mPlay;

	public ListViewItem(Context context) {
		//super(context);
		this(context,null);
	}
	
	public ListViewItem(Context context, AttributeSet attrs) {
		//super(context, attrs);
		this(context,attrs,-1);
	}
	
	public ListViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//设置显示默认的样式
		View view = View.inflate(context, R.layout.videofragment_listview_normal, null);
		mPlay = (ImageView) view.findViewById(R.id.item_iv_play);
		
		//将默认的样式添加到自定义控件中进行展示
		this.addView(view);
	}

	
	/**
	 * 提供给VideoFragment的ListView的getView调用的，方便获取播放按钮
	 * 
	 * 2016年12月31日 上午9:56:12
	 */
	public ImageView getPlayButton(){
		return mPlay;
	}
	
	private MyMediaPlayer mediaPlayer;
	/**
	 * 添加播放界面到自定义控件中
	 * 
	 * 2016年12月31日 上午10:24:19
	 */
	public void addVideoView(MyMediaPlayer myMediaPlayer){
		//将播放界面添加到了自定义控件中展示了
		this.mediaPlayer = myMediaPlayer;
		this.addView(myMediaPlayer.getRootView());
	}
	
	/**
	 * 删除播放界面，停止播放操作
	 * 
	 * 2016年12月31日 上午11:15:06
	 */
	public void removeVideoView(){
		this.mediaPlayer.release();
		this.removeView(this.mediaPlayer.getRootView());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
