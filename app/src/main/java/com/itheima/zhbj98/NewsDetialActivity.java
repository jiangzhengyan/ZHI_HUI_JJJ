package com.itheima.zhbj98;

import java.net.URL;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.itheima.zhbj98.bean.NewBean.News;
import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DaoConfig;
import com.lidroid.xutils.exception.DbException;

public class NewsDetialActivity extends Activity implements OnClickListener {

	private TextView mTitle;
	private ImageView mMenu;
	private ImageView mBack;
	private LinearLayout mLLDetail;
	private ImageView mTextSize;
	private ImageView mShare;
	private ImageView mSH;
	private WebView mWebView;
	private ProgressBar mLoading;
	private ImageButton mSpeak;
	private News news;
	private WebSettings webSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detial);
		StatusBarUtil.setColor(this, Color.RED);

		//初始化讯飞语音
		SpeechUtility.createUtility(this, SpeechConstant.APPID +"=58635f3d");
		
		//1.创建 SpeechSynthesizer 对象, 第二个参数： 本地合成时传 InitListener
		mTts = SpeechSynthesizer.createSynthesizer(this, null);
		
		// 接受传递过来的bean类的对象
		news = (News) getIntent().getSerializableExtra("url");

		initView();
	}

	/**
	 * 初始化控件，加载数据操作
	 * 
	 * 2016年12月27日 下午4:52:53
	 */
	private void initView() {
		mTitle = (TextView) findViewById(R.id.titlebar_tv_title);
		mMenu = (ImageView) findViewById(R.id.titlebar_iv_menu);
		mBack = (ImageView) findViewById(R.id.titlebar_iv_back);
		mLLDetail = (LinearLayout) findViewById(R.id.titlebar_ll_textsizeandshare);
		mTextSize = (ImageView) findViewById(R.id.titlebar_iv_textsize);
		mShare = (ImageView) findViewById(R.id.titlebar_iv_share);
		mSH = (ImageView) findViewById(R.id.titlebar_iv_sh);

		mWebView = (WebView) findViewById(R.id.detail_wv_webview);
		mLoading = (ProgressBar) findViewById(R.id.detail_pb_loading);
		mSpeak = (ImageButton) findViewById(R.id.detail_ibtn_speak);

		// 修改标题文本，隐藏显示按钮
		mTitle.setText("详情");
		mMenu.setVisibility(View.GONE);
		mBack.setVisibility(View.VISIBLE);
		mLLDetail.setVisibility(View.VISIBLE);

		// 加载网页
		mWebView.loadUrl(news.url);// 根据网页路径，加载网页

		// 设置webview展示网页操作
		webSettings = mWebView.getSettings();
		webSettings.setBuiltInZoomControls(true);// 显示放大/缩小按钮（对wap网页不支持 ，pc:web）
		webSettings.setUseWideViewPort(true);// 支持双击放大（对wap网页不支持 ，pc:web）
		webSettings.setJavaScriptEnabled(true);// 支持网页中的JavaScript代码的调用

		// 设置加载网页的时候显示进度条，网页展示完成隐藏进度条
		// 设置监听网页加载的操作
		mWebView.setWebViewClient(new WebViewClient() {
			// 网页加载调用的方法
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mLoading.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			// 网页加载完成调用的方法
			@Override
			public void onPageFinished(WebView view, String url) {
				mLoading.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});

		mBack.setOnClickListener(this);
		mTextSize.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mSH.setOnClickListener(this);
		mSpeak.setOnClickListener(this);
		
		//android的webview本身不支持网页的alert标签，必须设置WebChromeClient才能支持
		mWebView.setWebChromeClient(new WebChromeClient());
		
		//添加js代码可以调用的操作
		//参数1：js调用的android的接口实现对象
		//参数2：js调用的android的接口实现对象的名称
		mWebView.addJavascriptInterface(new JsCallAndroid() {
			//@JavascriptInterface : 在Android4.2之后，为了提高js和android互调的安全性，所有提供给js调用的Android的方法，必须添加@JavascriptInterface
			@Override
			@JavascriptInterface
			public void back() {
				Toast.makeText(NewsDetialActivity.this,"JS调用Android了", 0).show();
				finish();
			}
		}, "Android");
	}

	/**
	 * 提供个js代码调用的接口
	 * Author _ jjjzzzyyy
	 * 2016年12月28日 下午3:24:33
	 */
	public interface JsCallAndroid{
		public void back();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.titlebar_iv_back:
			finish();
			break;
		case R.id.titlebar_iv_textsize:
			showTextSizeDialog();
			break;
		case R.id.titlebar_iv_share:
			share();
			break;
		case R.id.titlebar_iv_sh:
			save();
			break;
		case R.id.detail_ibtn_speak:
			//判断是否正在阅读，在，停止阅读
			if (mTts.isSpeaking()) {
				mTts.stopSpeaking();
			}else{
				mWebView.loadUrl("javascript:wave()");
				beginSpeak();
			}
		
			break;
		}
	}

	private String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
			"超小号字体" };

	/** 保存选中的字体大小条目索引 **/
	private int selectItem;

	/** 保存更改字体大小的索引 **/
	private int textSizeCode = 2;
	private SpeechSynthesizer mTts;

	/**
	 * 弹出设置字体大小对话框
	 * 
	 * 2016年12月27日 下午5:17:15
	 */
	private void showTextSizeDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("字体设置");

		// 创建单选条目组
		// 参数1：条目所需的文本
		// 参数2：选择的条目
		// 参数3：条目选中的监听
		builder.setSingleChoiceItems(items, textSizeCode,
				new DialogInterface.OnClickListener() {
					// which : 选中条目的索引
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectItem = which;
					}
				});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 根据保存的选中的字体大小的条目的索引更改字体大小
				switch (selectItem) {
				case 0:
					webSettings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					webSettings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					webSettings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					webSettings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					webSettings.setTextSize(TextSize.SMALLEST);
					break;
				}
				// 保存设置过的字体大小的索引，方便回显
				textSizeCode = selectItem;
			}
		});
		builder.setNegativeButton("取消", null);// 如果点击取消按钮只是为了隐藏对话框，并没有做其他操作，可以直接设置为null
		builder.show();
	}

	/**
	 * 分享
	 * 
	 * 2016年12月28日 上午10:14:05
	 */
	private void share() {

		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
		oks.setTitle("标题");
		// titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// 分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		//oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("ShareSDK");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);

	}
	
	
	/**
	 * 收藏
	 * 
	 * 2016年12月28日 上午10:35:40
	 */
	private void save() {
		//创建数据库的配置
		DaoConfig daoConfig = new DaoConfig(this);
		//设置数据库的名称
		daoConfig.setDbName("zhbj98");
		//创建数据库，参数：数据库配置
		DbUtils dbUtils = DbUtils.create(daoConfig);
		try {
			dbUtils.save(news);
			Toast.makeText(this, "收藏成功", 0).show();
		} catch (DbException e) {
			//已经收藏了，抛出异常
			//取消收藏
			try {
				dbUtils.delete(news);
				Toast.makeText(this, "取消收藏", 0).show();
			} catch (DbException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 开始阅读网页文本内容的操作
	 * 
	 * 2016年12月28日 上午11:31:11
	 */
	private void beginSpeak() {
		//1.解析出html网页中的文本内容
		//因为html网页不在本地，在服务器端，所以解析的时候，是通过服务器获取到html网页进行解析的，所以解析的操作是联网的操作，放到子线程中执行
		final StringBuilder sb = new StringBuilder();
		new Thread(){
			public void run() {
				try {
					//解析html网页,返回解析出来的网页的信息
					//参数1：网页的地址
					//参数2：解析超时时间
					Document document = Jsoup.parse(new URL(news.url), 5000);
					
					//获取相应标签的数据,包含所有的p标签的数据，返回所有p标签
					//参数：标签的名称
					Elements elements = document.select("p");
					
					//获取p标签迭代器
					Iterator<Element> iterator = elements.iterator();
					
					//遍历迭代器获取数据
					//判断迭代器中是否有下一个标签
					while(iterator.hasNext()){
						//获取下一个标签
						Element element = iterator.next();
						//获取一个标签的数据
						String text = element.text();
						
						//因为需要阅读所有的p标签的文本，所以需要将所有的文本整合到一起，方便阅读
						sb.append(text);
					}
					
					System.out.println(sb.toString());
					//2.阅读操作
					speak(sb.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		
	
	}

	/**
	 * 阅读网页文本的操作
	 * 
	 * 2016年12月28日 上午11:50:03
	 */
	protected void speak(String string) {
		//2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
		//设置发音人（更多在线发音人，用户可参见 附录13.2
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaorong"); //设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
		//设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
		//保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
		//仅支持保存为 pcm 和 wav 格式， 如果不需要保存合成音频，注释该行代码
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
		//3.开始合成
		mTts.startSpeaking(string, null);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//退出应用程序，停止阅读
		if (mTts.isSpeaking()) {
			mTts.stopSpeaking();
		}
	}
	
	
	
	
	
	
	
	
	

}
