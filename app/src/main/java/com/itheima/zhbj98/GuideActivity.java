package com.itheima.zhbj98;

import java.util.ArrayList;
import java.util.List;

import com.itheima.zhbj98.utils.Constants;
import com.itheima.zhbj98.utils.DensityUtil;
import com.itheima.zhbj98.utils.SharedPreferencesUtil;
import com.itheima.zhbj98.view.DepthPageTransformer;
import com.itheima.zhbj98.view.RotatePagerPageTransFormer;
import com.itheima.zhbj98.view.ZoomOutPageTransformer;
import com.jaeger.library.StatusBarUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
/**
 * 引导界面
 * Author _ jjjzzzyyy
 * 2016年12月23日 上午11:36:31
 */
public class GuideActivity extends Activity {

	private int[] mImageIds = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
	
	/**存放ImageView的集合**/
	private List<ImageView> list;

	private ViewPager mViewPager;

	private Button mStart;

	private LinearLayout mLLDots;

	private ImageView mRedDot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//代码去除标题栏
		setContentView(R.layout.activity_guide);
		//设置沉浸式状态栏,必须在setContentView方法之后执行
		StatusBarUtil.setColor(this, Color.YELLOW);
		
		initView();
		
	}

	/**
	 * 初始化控件
	 * 
	 * 2016年12月23日 下午2:53:11
	 */
	private void initView() {
		
		mViewPager = (ViewPager) findViewById(R.id.guide_vp_viewpager);
		mStart = (Button) findViewById(R.id.guide_btn_start);
		mLLDots = (LinearLayout) findViewById(R.id.guide_ll_dots);
		mRedDot = (ImageView) findViewById(R.id.guide_iv_reddot);
		
		//图片是通过imageView展示的，所以需要将图片放到ImageView中，再将ImageView通过ViewPager进行展示
		list = new ArrayList<ImageView>();
		list.clear();
		
		//1.根据图片创建ImageView
		for (int i = 0; i < mImageIds.length; i++) {
			//创建相应的ImageView来显示相应的图片
			createImageView(i);
			//根据图片的张数，创建相应个数的点
			createDot();
		}
		
		//设置ViewPager界面切换动画
		//参数1：界面执行的顺序，false:从第一个条目执行到最后一个条目，true:从最后一个条目执行到第一个条目
		//参数2：执行的动画
		//mViewPager.setPageTransformer(false, new ZoomOutPageTransformer());
		//mViewPager.setPageTransformer(true, new DepthPageTransformer());
		mViewPager.setPageTransformer(true, new RotatePagerPageTransFormer());
		
		//2.通过viewpager展示数据
		mViewPager.setAdapter(new Myadapter());
		//3.设置ViewPager的界面切换监听，当切换到第三个界面的时候显示按钮，不是第三个界面隐藏按钮
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			//切换（滑动）到界面的时候调用方法
			//position : 切换到的界面的索引
			@Override
			public void onPageSelected(int position) {
				//判断是否切换到第三个界面，隐藏显示按钮
				if (position == list.size()-1) {
					mStart.setVisibility(View.VISIBLE);
				}else{
					mStart.setVisibility(View.GONE);
				}
			}
			
			//viewpager切换的时候调用的方法
			//position : 条目的索引
			//positionOffset : 偏移的百分比,从右往左滑动，依次从0%增加到100%，如果切换界面了自动归0%
			//positionOffsetPixels : 偏移的像素
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				//滑动的时候，移动红色的点
				System.out.println("偏移的百分比:"+positionOffset);
				//红色点移动的距离 = positionOffset * 20
				//第二个界面 ： 0*20+1*20   第三个界面：0*20+2*20
				//参数：平移的距离
				//mRedDot.setTranslationX(positionOffset * 20+position*20);
				//优化操作
				mRedDot.setTranslationX((positionOffset+position)*DensityUtil.dip2px(GuideActivity.this, 20));
			}
			
			//切换状态改变的时候调用的方法
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 创建图片对应的imageView
	 * 
	 * 2016年12月23日 下午2:56:01
	 */
	private void createImageView(int i) {
		ImageView imageView = new ImageView(this);
		//将相应的图片存放到imageview中展示
		imageView.setBackgroundResource(mImageIds[i]);
		//将创建的ImageView存放到list集合中，方便viewpager展示数据操作
		list.add(imageView);
	}
	
	/**
	 * 根据图片的张数创建点的个数
	 * 
	 * 2016年12月23日 下午3:44:38
	 */
	private void createDot() {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(R.drawable.shape_guide_gray_dot);
	
		//因为layout_marginRight中layout_表示父控件提供的操作，imageView要添加到LinearLayout中
		//所以必须使用LinearLayout的android:layout_marginRight属性进行设置，所以才使用LinearLayout.LayoutParams
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//相当于属性中的android:layout_marginRight
		//params.rightMargin = 10;//单位是px
		params.rightMargin = DensityUtil.dip2px(this, 10);//dip2px单位是dp
		//将layoutparams设置给imageView，使用其中的属性生效
		imageView.setLayoutParams(params);
		
		
		//添加到LinearLayout中显示
		mLLDots.addView(imageView);
	}
	
	/**ViewPager的Adapter**/
	private class Myadapter extends PagerAdapter{

		//设置viewpager的条目的个数
		@Override
		public int getCount() {
			return list.size();
		}

		//判断界面是否应该显示   
		//参数1：界面的View对象
		//参数2：显示界面的view对象
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		//添加显示viewpager的条目
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//根据条目的索引获取要显示的imageView
			ImageView imageView = list.get(position);
			container.addView(imageView);//添加到viewpager中
			//添加那个view对象，就要显示那个view对象，所以就要返回添加的view对象
			return imageView;
		}
		
		//删除viewpager条目的操作
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//super.destroyItem(container, position, object);//抛出异常
			container.removeView((View) object);
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * 开始体验按钮的点击事件
	 * 
	 * 2016年12月23日 下午5:18:39
	 */
	public void start(View view){
		//保存用户不是第一次进入的状态
		SharedPreferencesUtil.saveBoolean(this, Constants.ISFIRSTCENTER, false);
		//跳转到首页
		startActivity(new Intent(this,HomeActivity.class));
		
		//移除引导界面
		finish();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
