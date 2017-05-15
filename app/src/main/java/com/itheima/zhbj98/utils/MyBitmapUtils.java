package com.itheima.zhbj98.utils;

import u.aly.bi;

import com.itheima.zhbj98.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 图片三级缓存加载类
 * Author _ jjjzzzyyy
 * 2016年12月30日 下午4:00:17
 */
public class MyBitmapUtils {

	private MyCacheBitmapUtils cacheBitmapUtils;
	private MyLocalBitmapUtils localBitmapUtils;
	private MyNetBitmapUtils netBitmapUtils;

	public MyBitmapUtils(){
		cacheBitmapUtils = new MyCacheBitmapUtils();
		localBitmapUtils = new MyLocalBitmapUtils();
		netBitmapUtils = new MyNetBitmapUtils(cacheBitmapUtils, localBitmapUtils);
	}
	
	/**
	 * 根据图片的url获取图片，存放到iamgeview中
	 * 
	 * 2016年12月30日 下午4:00:58
	 */
	public void getBitmap(ImageView imageView,String imageurl){
		//设置imagetView显示默认图片
		imageView.setImageResource(R.drawable.pic_item_list_default);
		
		//1.从内存中获取图片，有记载显示，没有加载本地图片
		Bitmap bitmap = cacheBitmapUtils.getBitmap(imageurl);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			//如果内存已经获取到图片，就不用从本地文件和网络获取图片
			return;
		}
		//2.如果内存没有缓存图片，从本地获取图片，有加载，并缓存到内存中，没有请求网络获取图片
		bitmap = localBitmapUtils.getBitmap(imageurl);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			cacheBitmapUtils.saveBitmap(imageurl, bitmap);
			return;
		}
		//3.如果本地也没有缓存图片，从网络下载图片，下载完成，展示图片，并缓存到内存中和本地，方便下一次展示图片可以直接从内存和本地获取
		netBitmapUtils.getBitmap(imageView, imageurl);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
