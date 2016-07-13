package com.oss.jieyou;

import com.oss.comon.network.body.Body;
import com.oss.jieyou.bottom.Bottom;
import com.oss.jieyou.stat.Stat;
import com.oss.jieyou.title.Title;
import com.oss.jieyou.title.TitleClickListener;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	public Title mTitle;
	public Bottom mBottom;
	public Body mBody;
	public Stat mStat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	
	private void init() {
		mTitle = new Title(this);
		mTitle.initView(this);
		mBottom = new Bottom(this);
		mBottom.initView(this);
		mBody = new Body(this);
		mBody.initView(this);
		mStat = new Stat(this);
		mStat.statOpen();
	}
	
	/**
	 * 切换中间页面显示内容
	 * @param pageNum
	 */
	public void change(int pageNum) {
		switch(pageNum) {
		case Utils.PAGE_BOX:
			mBody.showBox();
			mTitle.setMiddleTitle(this.getString(R.string.box));
			break;
		case Utils.PAGE_WRITE:
			mTitle.setMiddleTitle(this.getString(R.string.write_message)); 
			mBody.showWritePage();
			break;
		case Utils.PAGE_FEEDBACK:
			mTitle.setMiddleTitle(this.getString(R.string.feedback));
			mBody.showFeedBack();
			break;
		}
	}
	
	/**
	 * 选择图片后的事件透传
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			Log.i(Utils.TAG, uri.toString());
			mBody.onActivityResult(data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 为title设置点击接听器
	 * @param listener
	 */
	public void setTitleClickListener(TitleClickListener listener) {
		mTitle.setTitleClickListener(listener);
	}
	
}
