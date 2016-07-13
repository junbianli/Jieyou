package com.oss.comon.network.body.func;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oss.jieyou.BaseModule;
import com.oss.jieyou.MainActivity;
import com.oss.jieyou.R;

/**
 * 添加语音、图片附件的界面
 * @author bianlijun
 *
 */
public class Func extends BaseModule{
	
	private ImageView mImage;
	private ImageView mVoice;
	private ImageView mAdd;
	private LinearLayout mFuncLayout;
	public Func(MainActivity activity) {
		this.mActivity = activity;
	}

	@Override
	public void initView(MainActivity activity) {
		// TODO Auto-generated method stub
		if(null == mInstance) {
			mInstance = new Func(activity);
		}
		setUpViews();
	}

	@Override
	public void destroyView() {
		// TODO Auto-generated method stub
		
	}

	private void setUpViews() {
		mImage = (ImageView) mActivity.findViewById(R.id.mImage);
		mVoice = (ImageView) mActivity.findViewById(R.id.mVoice);
		mAdd = (ImageView) mActivity.findViewById(R.id.mAdd);
		mFuncLayout = (LinearLayout) mActivity.findViewById(R.id.mFuncLayout);
		
		mImage.setOnClickListener(this);
		mVoice.setOnClickListener(this);
		mAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0 == mAdd) {
			handleAdd();
		}
		if(arg0 == mImage) {
			Intent intent = new Intent();
	        /* 开启Pictures画面Type设定为image */
	        intent.setType("image/*");
	        /* 使用Intent.ACTION_GET_CONTENT这个Action */
	        intent.setAction(Intent.ACTION_GET_CONTENT); 
	        /* 取得相片后返回本画面 */
	        mActivity.startActivityForResult(intent, 1);
		}
		if(arg0 == mVoice) {
			mActivity.mBody.handleVoiClick();
		}
	}

	@Override
	public void showView() {
		// TODO Auto-generated method stub
		super.showView();
		if(View.INVISIBLE == mAdd.getVisibility()) {
			mAdd.setVisibility(View.VISIBLE);
		}
		hideChildView();
	}

	@Override
	public void hideView() {
		// TODO Auto-generated method stub
		super.hideView();
		if(View.VISIBLE == mAdd.getVisibility()) {
			mAdd.setVisibility(View.INVISIBLE);
		}
		hideChildView();
	}
	
	/**
	 * 隐藏图片、语音按钮
	 */
	public void hideChildView() {
		if(View.VISIBLE == mFuncLayout.getVisibility()) {
			mFuncLayout.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 显示图片、语音按钮
	 */
	public void showChildView() {
		if(View.INVISIBLE == mFuncLayout.getVisibility()) {
			mFuncLayout.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Add按钮事件处理
	 */
	public void handleAdd() {
		if(View.VISIBLE == mFuncLayout.getVisibility()) {
			hideChildView();
		}else {
			showChildView();
		}
	}
}
