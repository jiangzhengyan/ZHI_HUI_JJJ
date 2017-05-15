package com.itheima.zhbj98.bean;

import java.util.List;

/**
 * 其中的内容需要根据json进行编写
 * 1.json:{ : jsonObject(bean类) []：数组/集合      单个字段：bean中保存数据的一个字段
 * 2.根据json串的格式编写bean类格式
 * 3.bean类中的字段名称必须和json串中保持一致
 * Author _ jjjzzzyyy
 * 2016年12月26日 上午9:49:50
 */
public class NewsCenterInfo {

	public List<NewsCenterDataInfo> data;
	public List<String> extend;
	public String retcode;
	
	public class NewsCenterDataInfo{
		public List<Child> children;
		public String id;
		public String title;
		public String type;
		
		public String url;
		public String url1;
		
		public String dayurl;
		public String excurl;
		public String weekurl;
	}
	
	public class Child{
		public String id;
		public String title;
		public String type;
		
		public String url;
	}
	
}
