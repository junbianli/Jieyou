package com.oss.comon.network.body;
import java.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.oss.comon.network.body.box.Box;
import com.oss.comon.network.body.feedback.FeedBack;
import com.oss.comon.network.body.func.Func;
import com.oss.comon.network.body.write.Write;
import com.oss.jieyou.BaseModule;
import com.oss.jieyou.MainActivity;
import com.oss.jieyou.Utils;

/**
 * Body模块
 * @author bianlijun
 *
 */
public class Body extends BaseModule{
	private Func mFunc;
	private Write mWrite;
	private FeedBack mFeedBack;
	private BaseModule mLastModule;
	private Box mBox;
	public Body(MainActivity activity) {
		mActivity = activity;
		mWrite = new Write(mActivity);
		mFunc = new Func(activity);
		mFeedBack = new FeedBack(activity);
		mBox = new Box(activity);
	}
	
	@Override
	public  void initView(MainActivity activity) {
		// TODO Auto-generated method stub
		mWrite.initView(activity);
		mFunc.initView(activity);
		mFeedBack.initView(activity);
		mBox.initView(activity);
		mWrite.showView();
		mFunc.showView();
		mFeedBack.hideView();
		mBox.hideView();
		mLastModule = mWrite;
		mActivity.setTitleClickListener(mWrite);
	}

	@Override
	public void destroyView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void showWritePage() {
		mActivity.setTitleClickListener(mWrite);
		mWrite.showView();
		if(!mLastModule.equals(mWrite)) {
			mLastModule.hideView();
		}
		mLastModule = mWrite;
	}
	
	public void showFeedBack() {
		mActivity.setTitleClickListener(mFeedBack);
		mFeedBack.showView();
		if(!mLastModule.equals(mFeedBack)) {
			mLastModule.hideView();
		}
		mLastModule = mFeedBack;
	}
	
	public void showBox() {
		mActivity.setTitleClickListener(mBox);
		mBox.showView();
		if(!mLastModule.equals(mBox)) {
			mLastModule.hideView();
		}
		mLastModule = mBox;
	}
	
	/**
	 * 语音按钮点击事件透传
	 */
	public void handleVoiClick() {
		mWrite.handleVoiClick();
	}

	/**
	 * 选择图片intent事件透传
	 */
	@Override
	public void onActivityResult(Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(intent);
		mLastModule.onActivityResult(intent);
	}
	
	/**
	 * 发送的消息加入已发送列表，只作函数透传
	 * @param id
	 * @param textStr
	 * @param picStr
	 * @param voiStr
	 */
	public void addMessageToSended(int id, String textStr, String picStr, String voiStr) {
		mBox.addMessageToSended(id, textStr, picStr, voiStr);
	}
}
