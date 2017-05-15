package com.itheima.zhbj98.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

/**
 * 本地缓存图片
 * 本质：保存图片到本地和从本地获取图片
 * Author _ jjjzzzyyy
 * 2016年12月30日 下午4:44:40
 */
public class MyLocalBitmapUtils {
	private static final String PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj_98";
	
	/**
	 * 保存图片到本地中
	 * 
	 * 2016年12月30日 下午4:45:28
	 */
	public void saveBitmap(String url,Bitmap bitmap){
		//创建zhbj_98文件夹
		File dr = new File(PATH);
		//判断如果文件不存在，或者不是一个目录的话，重新创建目录
		if (!dr.exists() || !dr.isDirectory()) {
			dr.mkdirs();
		}
		try {
			//将图片保存到目录
			File file = new File(dr, MD5Util.Md5(url).substring(0, 10));
			FileOutputStream stream = new FileOutputStream(file);
			//设置图片类型质量，将图片保存本地文件中
			//参数1：图片格式
			//参数2：图片的质量
			//参数3：写入流
			bitmap.compress(CompressFormat.JPEG, 100, stream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 根据url获取本地文件中的图片
	 * 
	 * 2016年12月30日 下午4:45:52
	 */
	public Bitmap getBitmap(String url){
		System.out.println("从本地获取图片了");
		try {
			File file = new File(PATH, MD5Util.Md5(url).substring(0, 10));
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			return bitmap;
		} catch (Exception e) {
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
