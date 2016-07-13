package com.oss.comon.network.body.feedback;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.oss.jieyou.BaseModule;
import com.oss.jieyou.MainActivity;
import com.oss.jieyou.R;
import com.oss.jieyou.title.TitleClickListener;
import com.oss.jieyou.title.TitleData;

/**
 * 反馈界面
 * @author bianlijun
 *
 */
public class FeedBack extends BaseModule implements TitleClickListener{
	
	private EditText mFeedBack;
	private RelativeLayout mFeedLayout;
	
	public FeedBack(MainActivity activity) {
		mActivity = activity;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView(MainActivity activity) {
		// TODO Auto-generated method stub
		setUpViews();
	}

	@Override
	public void destroyView() {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public void showView() {
		// TODO Auto-generated method stub
		super.showView();
		if(View.INVISIBLE == mFeedLayout.getVisibility()) {
			mFeedLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideView() {
		// TODO Auto-generated method stub
		super.hideView();
		if(View.VISIBLE == mFeedLayout.getVisibility()) {
			mFeedLayout.setVisibility(View.INVISIBLE);
		}
	}

	private void setUpViews() {
		mFeedBack = (EditText) mActivity.findViewById(R.id.mFeedBack);
		mFeedLayout = (RelativeLayout) mActivity.findViewById(R.id.mFeedLayout);
	}

	@Override
	public void onLeftTitleClick() {
		// TODO Auto-generated method stub
		Toast.makeText(mActivity, "FeedBack Left", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onRightTitleClick() {
		// TODO Auto-generated method stub
		Toast.makeText(mActivity, "FeedBack Right", Toast.LENGTH_SHORT).show();
	}

	@Override
	public List<TitleData> getTitleDataArr() {
		// TODO Auto-generated method stub
		return new ArrayList<TitleData>();
	}
}
