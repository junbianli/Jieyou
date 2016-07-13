package com.oss.jieyou;

import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;

public abstract class BaseModule implements OnClickListener{
	protected BaseModule mInstance = null;
	protected MainActivity mActivity;
	
	public abstract void initView(MainActivity activity);
	public abstract void destroyView();
	public void showView(){
	};
	public void hideView(){};
	
	public void onActivityResult(Intent intent){};
}
