package com.itheima.zhbj98;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.zhbj98.view.MyGridLayout;
import com.itheima.zhbj98.view.MyGridLayout.OnItemClickListener;
import com.jaeger.library.StatusBarUtil;

public class SelectItemActivity extends Activity {

	private List<String> titles1;
	
	/**底部列表所需的数据**/
	private String[] TITLES = new String[]{"娱乐","服饰","音乐","视频","段子","搞笑","科学","房产","名站"};

	private List<String> titles2;

	private MyGridLayout mGrid2;

	private MyGridLayout mGrid1;

	private ImageView mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_item);
		StatusBarUtil.setColor(this, Color.WHITE);
		
		getValue();
		initView();
	}
	
	/**
	 * 获取传递过来的数据，并且底部列表的数据
	 * 
	 * 2016年12月30日 上午9:21:55
	 */
	private void getValue() {
		titles1 = (List<String>) getIntent().getSerializableExtra("titles");
		
		//设置底部列表的数据
		titles2 = new ArrayList<String>();
		titles2.clear();
		for (int i = 0; i < TITLES.length; i++) {
			titles2.add(TITLES[i]);
		}
		
	}
	
	/**
	 * 初始化控件
	 * 
	 * 2016年12月30日 上午9:50:18
	 */
	private void initView() {
		mBack = (ImageView) findViewById(R.id.selectitem_iv_back);
		mGrid1 = (MyGridLayout) findViewById(R.id.selectitem_gl_grid1);
		mGrid2 = (MyGridLayout) findViewById(R.id.selectitem_gl_grid2);
		
		//调用设置是否可以进行拖拽的方法
		mGrid1.setDrag(true);
		mGrid2.setDrag(false);
		
		//调用自定义的GridLayout中的setItemDatas方法，将activity的数据传递给自定义控件显示
		mGrid1.setItemDatas(titles1);
		mGrid2.setItemDatas(titles2);
		
		mGrid1.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(TextView view) {
				//Grid1删除条目
				//Grid2添加条目
				mGrid1.removeView(view);
				mGrid2.addTextView(view.getText().toString());
			}
		});
		
		mGrid2.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(TextView view) {
				//Grid2删除条目
				//Grid1添加条目
				mGrid2.removeView(view);
				mGrid1.addTextView(view.getText().toString());
			}
		});
		
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
