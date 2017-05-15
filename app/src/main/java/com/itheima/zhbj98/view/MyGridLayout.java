package com.itheima.zhbj98.view;

import java.util.List;

import com.itheima.zhbj98.R;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
/**
 * 自定义的GridLayout实现拖拽效果
 * Author _ jjjzzzyyy
 * 2016年12月30日 上午9:44:07
 */
public class MyGridLayout extends GridLayout {

	public MyGridLayout(Context context) {
		//super(context);
		this(context,null);
	}
	
	public MyGridLayout(Context context, AttributeSet attrs) {
		//super(context, attrs);
		this(context,attrs,-1);
	}

	public MyGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		//设置GridLayout中的条目增加过渡动画
		this.setLayoutTransition(new LayoutTransition());
	}
	
	/**
	 * 提供给activity调用的，方便activity将数据传递给自定义Gridlayout，方便Gridlayout显示数据
	 * 
	 * 2016年12月30日 上午9:49:24
	 */
	public void setItemDatas(List<String> datas){
		//传递过来的是String类型的文本数据，但是展示的时候需要使用TextView进行展示，所以需要根据数据的个数创建相应个数的textview来展示数据
		for (String string : datas) {
			addTextView(string);
		}
	}

	/**
	 * 创建文本对应的textview，添加GridLayout中展示
	 * 
	 * 2016年12月30日 上午10:14:49
	 */
	public void addTextView(String string) {
		final TextView textView = new TextView(getContext());
		textView.setText(string);
		textView.setTextColor(Color.BLACK);
		textView.setBackgroundResource(R.drawable.selector_selectitem_textview_bg);
		textView.setGravity(Gravity.CENTER);
		textView.setPadding(15, 5, 15, 5);
		
		GridLayout.LayoutParams params = new LayoutParams();
		params.setMargins(5, 5, 5, 5);
		
		//设置params给TextView,使用margins效果生效
		textView.setLayoutParams(params);
		
		
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击当前的textview,需要实现效果是完成两个gridlayout的添加和删除效果
				//但是，textview的点击事件实在Gridlayout内部进行操作的，只能代表一个Gridlayout，不能操作另一个Gridlayout
				//但是，activity初始化两个Gridlayout,所以activity可以处理，所以可以将被点击的textview传递给activity，让activity进行处理
				if (listener != null) {
					listener.onItemClick(textView);
				}
			}
		});
		
		//根据activity传递过来的标示，判断是否可以设置拖拽操作
		if (isDrag) {
			//设置textview的长按事件，实现拖拽效果
			textView.setOnLongClickListener(longClickListener);
		}else{
			textView.setOnLongClickListener(null);
		}
		
		//设置Gridlayout的拖拽监听，监听Gridlayout中的textview的拖拽操作
		//1.设置gridlayout，方便监听所有的条目的拖拽操作
		//2.因为Textview是属于Gridlayout的，所以给textview设置拖拽，也就是给Gridlayout条目设置拖拽
		this.setOnDragListener(dragListener);
			
		
		//将textview添加Gridlayout中展示
		this.addView(textView);
	}
	
	/**拖拽监听操作**/
	private OnDragListener dragListener = new OnDragListener() {
		
		//当拖拽操作执行的时候调用的方法
		//参数1：拖拽的控件
		//参数2：拖拽的事件
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED://开始拖拽
				System.out.println("开始拖拽");
				//根据textview创建矩形
				createRect();
				break;
			case DragEvent.ACTION_DRAG_ENTERED://开始拖拽控件后，进入拖拽控件范围事件
				System.out.println("进入拖拽控件范围");
				break;
			case DragEvent.ACTION_DRAG_EXITED://开始拖拽控件后，离开拖拽控件范围事件
				System.out.println("离开拖拽控件范围");
				break;
			case DragEvent.ACTION_DRAG_LOCATION://开始拖拽控件后，移动控件或者是拖拽控件执行的操作
				System.out.println("拖拽控件移动");
				
				//根据拖拽的按下的坐标，获取拖拽控件移动的位置
				int index = getIndex(event);
				//MyGridLayout.this.getChildAt(index) != currentView : 判断移动到位置的textview和拖拽的textview是否一致，一致不进行移动，不一致进行移动操作
				if (index != -1 && currentView != null && MyGridLayout.this.getChildAt(index) != currentView) {
					//实现Gridlayout的移动操作
					//将原来位置的拖拽的特性tview删除
					MyGridLayout.this.removeView(currentView);
					//将拖拽的textview添加到移动的位置
					MyGridLayout.this.addView(currentView, index);//将view对象，添加到那个位置
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED://结束拖拽
				System.out.println("结束拖拽");
				
				//设置拖拽的textview背景改为黑色边框的背景
				if (currentView != null) {
					currentView.setEnabled(true);
				}
				break;
			case DragEvent.ACTION_DROP://结束拖拽，在控件范围内，松开手指，在控件范围外不执行操作
				System.out.println("松开手指");
				break;

			}
			return true;
		}
	};
	
	
	/**
	 * 根据textview创建矩形
	 * 
	 * 2016年12月30日 上午11:44:08
	 */
	protected void createRect() {
		//getChildCount() : 获取Gridlayout的子控件的个数
		rects = new Rect[this.getChildCount()];
		//根据孩子的个数，创建相应个数的矩形
		for (int i = 0; i < this.getChildCount(); i++) {
			//根据子控件的索引，获取子控件的view对象
			View view = this.getChildAt(i);
			//创建一个矩形
			//参数1,2：左上角的x和y的坐标
			//参数3,4：右下角的x和y的坐标
			Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
			
			//保存到就行到数组中
			rects[i] = rect;
		}
	}
	
	/**
	 * 根据拖拽按下的坐标，获取拖拽控件移动的位置
	 * 
	 * 2016年12月30日 上午11:50:20
	 */
	protected int getIndex(DragEvent event) {
		for (int i = 0; i < rects.length; i++) {
			//判断按下的坐标是否包含在矩形的坐标范围内容
			if (rects[i].contains((int)event.getX(), (int)event.getY())) {
				return i;
			}
		}
		return -1;
	}

	/**保存拖拽的textview**/
	private View currentView;
	
	/**textview的长按事件监听**/
	private OnLongClickListener longClickListener = new OnLongClickListener() {
		
		//长按控件调用的方法
		//参数：被长按的控件
		@Override
		public boolean onLongClick(View v) {
			
			//设置控件开始拖拽
			//参数1：拖拽要显示的数据，一般null
			//参数2：拖拽显示的阴影样式,DragShadowBuilder(v):根据拖拽的控件，设置拖拽阴影的样式效果
			//参数3：拖拽的控件的状态，一般null
			//参数4：拖拽的其他设置的标示，一般0
			v.startDrag(null, new DragShadowBuilder(v), null, 0);
			
			//拖拽textview，textview设置为不可用，显示红色虚线的背景
			v.setEnabled(false);//设置控件是否可用的
			
			//因为停止拖拽textview的时候，还要改变同一个textview的显示背景，所以在textview被拖拽的时候，可以先将textview保存起来，方便停止拖拽的时候方便处理textview的背景
			currentView = v;
			
			//返回true表示处理长按事件，false就是不处理
			return true;
		}
	};
	
	/**保存传递过来的是否可以拖拽的标示**/
	private boolean isDrag;
	/**
	 * 提供activity调用，用来控制当前的Gridlayout是否可以拖拽的效果的
	 * 
	 * 2016年12月30日 上午11:18:27
	 */
	public void setDrag(boolean isDrag){
		this.isDrag = isDrag;
		//如果Gridlayout不能进行拖拽，就不需要在设置拖拽的监听了
		if (isDrag) {
			this.setOnDragListener(dragListener);
		}else{
			this.setOnDragListener(null);
		}
	}
	
	private OnItemClickListener listener;

	private Rect[] rects;
	/**
	 * 提供给activity调用，负责让activity将接口的实现对象传递过来
	 * 
	 * 2016年12月30日 上午10:29:03
	 */
	public void setOnItemClickListener(OnItemClickListener itemClickListener){
		this.listener = itemClickListener;
	}
	
	public interface OnItemClickListener{
		public void onItemClick(TextView view);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
