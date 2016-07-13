package com.oss.jieyou.record;

import android.view.View;

import com.oss.jieyou.BaseModule;
import com.oss.jieyou.MainActivity;
import com.oss.jieyou.R;

/**
 * 录音模块对外的类
 * @author bianlijun
 *
 */
public class Record extends BaseModule{
	
	private RecordView mRecordView;
	private Recorder mRecorder;
	private RecordListener mRecrodListener;
	
	public Record(MainActivity activity, RecordListener recordListener) {
		mActivity = activity;
		mRecrodListener = recordListener;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView(MainActivity activity) {
		// TODO Auto-generated method stub
		mRecorder = new Recorder(mActivity, mRecrodListener);
		mRecordView = (RecordView) mActivity.findViewById(R.id.mRecordView);
		mRecordView.setRecorder(mRecorder);
	}

	@Override
	public void destroyView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showView() {
		// TODO Auto-generated method stub
		super.showView();
		if(View.INVISIBLE == mRecordView.getVisibility()) {
			mRecordView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideView() {
		// TODO Auto-generated method stub
		super.hideView();
		if(View.VISIBLE == mRecordView.getVisibility()) {
			mRecordView.setVisibility(View.INVISIBLE);
		}
	}
	
	public void handleVoiClick() {
		if(View.VISIBLE == mRecordView.getVisibility()) {
			mRecordView.setVisibility(View.INVISIBLE);
		}else {
			mRecordView.setVisibility(View.VISIBLE);
		}
	}
}
