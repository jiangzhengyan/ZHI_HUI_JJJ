package com.itheima.zhbj98.utils;

import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 使用内存缓存图片
 * Author _ jjjzzzyyy
 * 2016年12月30日 下午4:03:33
 */
public class MyCacheBitmapUtils {

	private LruCache<String, Bitmap> lruCache;

	//private HashMap<String, SoftReference<Bitmap>> map;

	/**
	 * 1.软引用
	 * 	强引用：user = new UserInfo(),不会轻易被系统回收
	 * 	软引用：SoftReference<Bitmap>,当内存不足的时候，系统会回收软引用
	 *  弱引用：WeakReference<Bitmap>,当内存不足的时候，系统会回收弱引用，如果软引用和弱引用同时存在，先回收弱引用
	 *  虚引用：PhantomReference<Bitmap>,当内存不足的时候，系统会回收弱引用，优先级低于弱引用
	 * 2.LruCache:判断最新一段时间内图片引用使用的次数，判定是否需要缓存的，将使用频率比较高的引用缓存到内存中
	 */
	
	public MyCacheBitmapUtils(){
		//map = new HashMap<String, SoftReference<Bitmap>>();
		//maxSize : 缓存空间大小，一般是总内存的8分之一
		int maxSize = (int) (Runtime.getRuntime().totalMemory()/8);
		lruCache = new LruCache<String, Bitmap>(maxSize){
			//获取缓存图片的 大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				//value.getRowBytes() : 获取图片一行占用的字节数
				return value.getRowBytes() * value.getHeight();
			}
		};
	}
	
	/**
	 * 缓存图片
	 * 
	 * 2016年12月30日 下午4:12:27
	 */
	public void saveBitmap(String url,Bitmap bitmap){
		//SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);//使用软引用修改bitmap引用
		//map.put(url, softReference);
		lruCache.put(url, bitmap);
	}
	
	/**
	 * 通过图片的url（名称/key）获取相应的图片
	 * 
	 * 2016年12月30日 下午4:12:54
	 */
	public Bitmap getBitmap(String url){
		System.out.println("从内存中获取图片了");
		/*SoftReference<Bitmap> softReference = map.get(url);
		//确认软引用没有被回收
		if (softReference != null) {
			Bitmap bitmap = softReference.get();
			return bitmap;
		}*/
		Bitmap bitmap = lruCache.get(url);
		return bitmap;
	}
}
