package com.itheima.zhbj98.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 从网络获取图片
 * Author _ jjjzzzyyy
 * 2016年12月30日 下午5:05:20
 */
public class MyNetBitmapUtils {

	private MyCacheBitmapUtils cacheBitmapUtils;
	private MyLocalBitmapUtils localBitmapUtils;
	
	public MyNetBitmapUtils(MyCacheBitmapUtils cacheBitmapUtils,MyLocalBitmapUtils localBitmapUtils){
		this.cacheBitmapUtils = cacheBitmapUtils;
		this.localBitmapUtils = localBitmapUtils;
	};
	
	/**
	 * 下载图片，设置到imageview中展示
	 * 
	 * 2016年12月30日 下午5:06:06
	 */
	public void getBitmap(ImageView imageView,String url){
		//执行异步加载
		//参数：doInBackground所需的参数
		new MyAsyncTask().execute(imageView,url);
	}
	
	//参数1： 子线程使用的参数数据
	//参数2： 更新进度参数数据
	//参数3: 子线程执行返回结果，数据返回给onPostExecute处理的
	private class MyAsyncTask extends AsyncTask<Object, Integer, Bitmap>{

		private ImageView imageview;
		private String url;

		//在子线程之前调用方法
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		//在子线程之中执行的操作，run方法中执行的操作
		@Override
		protected Bitmap doInBackground(Object... params) {
			//因为实在子线程中操作的，并且图片有大有小，下载有慢又快，所以不能确定根据当前的url下载的图片一定可以存放到相应的imageView，可能会出现图片显示错乱
			imageview = (ImageView) params[0];
			url = (String) params[1];
			
			imageview.setTag(url);//将url和对应的iamgeview绑定
			
			//根据图片路径，获取图片，放到相应的imageView中展示
			Bitmap bitmap = dowload(url);
			
			return bitmap;
		}
		
		//在子线程之后执行的操作，runOnuiThread操作
		@Override
		protected void onPostExecute(Bitmap result) {
			System.out.println("从网络获取图片了");
			//将doInBackground获取的图片，展示到imageview中
			//将通过url获取的图片，存放到相应的imageView中
			//获取和imageView绑定的url
			String mUrl = (String) imageview.getTag();
			//将绑定的url和获取图片的url进行比较，如果一致，说明两个url是一个url，就可以将图片存放url绑定的imageview中
			if (mUrl.equals(url)) {
				if (result != null) {
					imageview.setImageBitmap(result);
					
					//将获取的图片，缓存到内存和本地
					cacheBitmapUtils.saveBitmap(url, result);
					localBitmapUtils.saveBitmap(url, result);
				}
			}
			super.onPostExecute(result);
		}
		
		//更新进度
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}
	/**
	 * 根据图片的url下载图片
	 * 
	 * 2016年12月30日 下午5:20:14
	 */
	public Bitmap dowload(String url) {
		try {
			URL mUrl = new URL(url);
			HttpURLConnection con = (HttpURLConnection) mUrl.openConnection();
			con.setConnectTimeout(5000);//设置链接超时时间
			con.setReadTimeout(5000);//设置读取超时时间
			con.connect();//链接网络操作
			int code = con.getResponseCode();//获取服务器响应码
			if (code == 200) {
				//获取服务器数据，以流的形式返回
				InputStream stream = con.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(stream);
				return bitmap;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
