package com.oss.jieyou.title;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oss.comon.network.body.box.BoxData;
import com.oss.jieyou.BaseModule;
import com.oss.jieyou.MainActivity;
import com.oss.jieyou.R;
import com.oss.jieyou.Utils;

/**
 * 标题栏
 * @author bianlijun
 *
 */
public class Title extends BaseModule{
	
	private RelativeLayout mTitleLayout;
	private TextView mTopLeft;
	private TextView mTopMiddle;
	private TextView mTopRight;
	private GridView mGridView;
	private TitleClickListener mListener;
	private List<TitleData> mTitleDataArr;
	private ExtAdapter mExtAdapter;
	private RecordPlayer mRecordPlayer;
	public Title(MainActivity activity) {
		mActivity = activity;
		mRecordPlayer = new RecordPlayer();
		mTitleDataArr = new ArrayList<TitleData>();
		mExtAdapter = new ExtAdapter();
	}
	
	@Override
	public void initView(MainActivity activity) {
		// TODO Auto-generated method stub
		if(null == mInstance) {
			mInstance = new Title(activity);
		}
		setUpViews();
	}

	@Override
	public void destroyView() {
		// TODO Auto-generated method stub
		
	}
	
	private void setUpViews() {
		mTitleLayout = (RelativeLayout) mActivity.findViewById(R.id.mTopLayout);
		mTopLeft = (TextView) mActivity.findViewById(R.id.mTopLeft);
		mTopMiddle = (TextView) mActivity.findViewById(R.id.mTopMiddle);
		mTopRight = (TextView) mActivity.findViewById(R.id.mTopRight);
		mGridView = (GridView) mActivity.findViewById(R.id.mExtGrid);
		mTopMiddle.setText(R.string.write_message);
		mTopLeft.setOnClickListener(this);
		mTopRight.setOnClickListener(this);
		
		mTopLeft.setText(mActivity.getString(R.string.send));
		mTopRight.setText("Right");
		
		mGridView.setAdapter(mExtAdapter);
	}
	
	public void setTitleClickListener(TitleClickListener listener) {
		mListener = listener;
		mTitleDataArr = mListener.getTitleDataArr();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(null != mListener) {
			if(arg0.equals(mTopLeft)) {
				mListener.onLeftTitleClick();
			}
			if(arg0.equals(mTopRight)) {
				mListener.onRightTitleClick();
			}
		}
		if(arg0.getId() == R.id.mExtItem) {
			Log.i(Utils.TAG, "onClick");
			TitleData titleData = (TitleData)arg0.getTag();
			if(Utils.FILE_TYPE_VOI == titleData.type) {
				mRecordPlayer.play(titleData.content);
			}
		}
	}
	
	/**
	 * 设置标题栏显示内容
	 * @param title
	 */
	public void setMiddleTitle(String title) {
		mTopMiddle.setText(title);
	}
	
	/**
	 * 通知标题栏数据已经更新
	 */
	public void notifyTitleDataChange() {
		mTitleDataArr = mListener.getTitleDataArr();
		if(null == mListener.getTitleDataArr()) {
			mTopRight.setText("Right");
		}else {
			mTopRight.setText(Integer.toString(mTitleDataArr.size()));
		}
		mExtAdapter.notifyDataSetChanged();
	}
	
	public class ExtAdapter extends BaseAdapter{
		private LayoutInflater mInflater;

		public ExtAdapter() {
			mInflater = LayoutInflater.from(mActivity);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTitleDataArr.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			TitleData titleData = mTitleDataArr.get(arg0);
			view = mInflater.inflate(R.layout.ext_item, null);
			ImageView mExtItem = (ImageView) view.findViewById(R.id.mExtItem);
			Bitmap bitmap = null;
			if(titleData.type == Utils.FILE_TYPE_PIC) {
				bitmap = Utils.convertStrToBitmap(titleData.content);
				if(null == bitmap) {
					Log.i(Utils.TAG, "bitmap == null");
					mExtItem.setBackgroundResource(R.drawable.box);
				}else {
					mExtItem.setImageBitmap(bitmap);
				}
			}else {
				mExtItem.setBackgroundResource(R.drawable.box);
			}
			mExtItem.setTag(titleData);
			mExtItem.setOnClickListener(Title.this);
			return view;
		}
	}
	
	/**
	 * 如果显示就隐藏，如果隐藏就显示
	 */
	public void hideOrShowExt() {
		if(View.VISIBLE == mGridView.getVisibility()) {
			mGridView.setVisibility(View.INVISIBLE);
		}else {
			mGridView.setVisibility(View.VISIBLE);
		}
	}
}
