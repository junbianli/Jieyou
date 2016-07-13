package com.oss.jieyou.bottom;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.oss.jieyou.BaseModule;
import com.oss.jieyou.MainActivity;
import com.oss.jieyou.R;
import com.oss.jieyou.Utils;

/**
 * 底栏类
 * @author bianlijun
 *
 */
public class Bottom extends BaseModule implements OnClickListener{
	private ImageView mBottomLeft;
	private ImageView mBottomMiddle;
	private ImageView mBottomRight;
	
	public Bottom(MainActivity activity) {
		mActivity = activity;
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
	
	private void setUpViews() {
		mBottomLeft = (ImageView) mActivity.findViewById(R.id.mBottomLeft);
		mBottomMiddle = (ImageView) mActivity.findViewById(R.id.mBottomMiddle);
		mBottomRight = (ImageView) mActivity.findViewById(R.id.mBottomRight);
		
		mBottomLeft.setOnClickListener(this);
		mBottomMiddle.setOnClickListener(this);
		mBottomRight.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0 == mBottomLeft) {
			mActivity.change(Utils.PAGE_BOX);
		}
		if(arg0 == mBottomMiddle) {
			mActivity.change(Utils.PAGE_WRITE);
		}
		if(arg0 == mBottomRight) {
			mActivity.change(Utils.PAGE_FEEDBACK);
		}
	}

}
