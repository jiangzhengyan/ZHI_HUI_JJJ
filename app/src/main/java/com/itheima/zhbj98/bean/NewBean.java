package com.itheima.zhbj98.bean;

import java.io.Serializable;
import java.util.List;

public class NewBean {
	public NewItem data;
	public String retcode;
	
	public class NewItem{
		public String countcommenturl;
		public String more;
		public String title;
		public List<News> news;
		public List<TopPic> topic;
		public List<TopNews> topnews;
	}
	
	public static class News implements Serializable{
		
		public boolean comment;
		public String commentlist;
		public String commenturl;
		//新闻唯一性标示,记录在本地，判读是是已读的新闻
		public String id;
		//图片url
		public String listimage;
		//图片url1
		public String listimage1;
		//图片url2
		public String listimage2;
		
		//时间
		public String pubdate;
		//新闻缩略内容
		public String title;
		public String type;
		
		//url关联详情页请求地址  html------>WebView(加载HTML网页)
		public String url;
		
		//标示当前条目新闻是否已读（）
		public boolean isRead;
	}
	public class TopPic{
		public String description;
		public int id;
		public String listimage;
		public String sort;
		public String title;
		public String url;
	}
	public class TopNews{
		public boolean comment;
		public String commentlist;
		public String commenturl;
		public int id;
		public String pubdate;
		//标题
		public String title;
		//图片
		public String topimage;
		public String type;
		//详情页url地址
		public String url;
	}
	
}
