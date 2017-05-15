package com.itheima.zhbj98.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.zhbj98.NewsDetialActivity;
import com.itheima.zhbj98.bean.NewBean.News;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//获取通过intent传递数据
		Bundle bundle = intent.getExtras();

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("收到了自定义消息。消息内容是："
					+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// 自定义消息不会展示在通知栏，完全要开发者写代码去处理
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("收到了通知");
			// 在这里可以做些统计，或者做些其他工作
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			
			// 在这里可以自己写代码去定义用户点击后的行为
			
			
			//获取推送过来的附加字段
			String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
			
			System.out.println("用户点击打开了通知:"+type);
			
			/*
			 {
			    "url": "http://10.0.2.2:8080/zhbj/10007/724D6A55496A11726628.html"
			 }
			*/
			try {
				JSONObject jsonObject = new JSONObject(type);
				String url = jsonObject.getString("url");
				News news = new News();
				news.url = url;
				
				Intent i = new Intent(context, NewsDetialActivity.class); // 自定义打开的界面
				i.putExtra("url", news);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} else {
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
