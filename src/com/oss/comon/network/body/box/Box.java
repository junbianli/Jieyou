package com.oss.comon.network.body.box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oss.comon.network.NetworkCommonListener;
import com.oss.comon.network.NetworkManager;
import com.oss.comon.network.body.write.Write;
import com.oss.comon.network.body.write.Write.SendNetworkListener;
import com.oss.jieyou.BaseModule;
import com.oss.jieyou.MainActivity;
import com.oss.jieyou.R;
import com.oss.jieyou.Utils;
import com.oss.jieyou.title.TitleClickListener;
import com.oss.jieyou.title.TitleData;

/**
 * 收件箱、发件箱等列表
 * @author bianlijun
 *
 */
public class Box extends BaseModule implements TitleClickListener, OnItemClickListener{
	private ListView mBoxList;
	private RelativeLayout mBoxLayout;
	private TextView mMessageText;
	private List<BoxData> mDataList;
	private MessageAdapter mMessageAdapter;
	private List<TitleData> mTitleDataArr;
	private String TAG = "Box";
	public Box(MainActivity activity) {
		mActivity = activity;
		mDataList = new ArrayList<BoxData>();
		mMessageAdapter = new MessageAdapter();
		mTitleDataArr = new ArrayList<TitleData>();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0.equals(mMessageText)) {
			hideMessageText();
		}
	}

	@Override
	public void initView(MainActivity activity) {
		// TODO Auto-generated method stub
		Log.i(Utils.TAG, "initView");
		mBoxLayout = (RelativeLayout) mActivity.findViewById(R.id.mBoxLayout);
		mBoxList = (ListView) mActivity.findViewById(R.id.mBoxList);
		mMessageText = (TextView) mActivity.findViewById(R.id.mMessageText);
		initListData();
		mMessageAdapter.notifyDataSetChanged();
		mBoxList.setAdapter(mMessageAdapter);
		mBoxList.setOnItemClickListener(this);
		mMessageText.setOnClickListener(this);
	}

	@Override
	public void destroyView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showView() {
		// TODO Auto-generated method stub
		super.showView();
		if(View.INVISIBLE == mBoxLayout.getVisibility()) {
			mBoxLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideView() {
		// TODO Auto-generated method stub
		super.hideView();
		if(View.VISIBLE == mBoxLayout.getVisibility()) {
			mBoxLayout.setVisibility(View.INVISIBLE);
		}
	}
	
	private void initListData() {
		mDataList.clear();
		
	}
	
	/**
	 * 发送消息
	 * @param content
	 */
	private void requestList(int mId) {
		HashMap<Object, Object> dataMap = new HashMap<Object, Object>();
		dataMap.put("cid", Utils.getDeviceId(mActivity));
		dataMap.put("type", Utils.TYPE_SEND);
		dataMap.put("textMsg", textStr);
		dataMap.put("picMsg", picStr);
		dataMap.put("voiMsg", voiStr);
		//String requestUrl = "http://jieyouzahuopu.duapp.com/index.php?r=api/upload-file";
		String requestUrl = Utils.BASE_URL + Utils.SEND_MSG;
		Log.i(TAG, "requestUrl = " + requestUrl);
		//String requestUrl = "http://bj.bcebos.com/jy-resource/575e32e397b3e?authorization=bce-auth-v1%2F05PhqPsRW9rpfjYgxDT1SOxg%2F2016-06-13T04%3A13%3A23Z%2F-1%2F%2F31d30ab50190d903de71e9b72f94ff7864c4feecc9446b33fb9051704a6365cf";
		SendNetworkListener requestListener = new SendNetworkListener();
		NetworkManager.getInstance().startRequest(requestUrl, HttpPost.METHOD_NAME, null, dataMap,  requestListener, false);
	}

	class SendNetworkListener extends NetworkCommonListener{
		
		@Override
		public boolean onReceivedDataAtMainThread(String receivedStr) {
			// TODO Auto-generated method stub
			Log.i(TAG, receivedStr);
			Log.i(TAG, "onReceivedDataAtMainThread  thread id = " + Thread.currentThread().getId());
			if(null != receivedStr) {
				JSONObject sendResult = null;
				try {
					sendResult = new JSONObject(receivedStr);
					if(sendResult.getInt("errCode") != Utils.INTERFACE_SUCCESS) {
						throw new Exception(receivedStr);
					}else {
						mId = sendResult.getJSONObject("data").getInt("mid");
						resetView();
						addMessageToSended();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(mActivity, mActivity.getString(R.string.send_failed), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			return true;
		}
		
		@Override
		public boolean onReceivedDataAtSubThread(String receivedStr) {
			// TODO Auto-generated method stub
			
			String result = new String(receivedStr);
			Log.i(TAG, result);
			Log.i(TAG, "onReceivedDataAtSubThread  thread id = " + Thread.currentThread().getId());
			return true;
		}
	}
	
	public class MessageAdapter extends BaseAdapter{
		private LayoutInflater mInflater;

		public MessageAdapter() {
			mInflater = LayoutInflater.from(mActivity);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mDataList.size();
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
			BoxData messageData = mDataList.get(arg0);
			view = mInflater.inflate(R.layout.box_item, null);
			ImageView icon = (ImageView) view.findViewById(R.id.mIcon);
			TextView name = (TextView) view.findViewById(R.id.mName);
			TextView num = (TextView) view.findViewById(R.id.mNum);
			
			name.setText(messageData.mTime);
			return view;
		}
		
	}

	@Override
	public void onLeftTitleClick() {
		// TODO Auto-generated method stub
		Toast.makeText(mActivity, "Box Left", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRightTitleClick() {
		// TODO Auto-generated method stub
		Toast.makeText(mActivity, "Box Right", Toast.LENGTH_SHORT).show();
	}

	@Override
	public List<TitleData> getTitleDataArr() {
		// TODO Auto-generated method stub
		return mTitleDataArr;
	}
	
	/**
	 * 将发送的消息加入已发送列表
	 */
	public void addMessageToSended(int id, String textStr, String picStr, String voiStr) {
		/*
		BoxData boxData = mDataList.get(3);
		//BoxData boxData = new BoxData("test123", R.drawable.box, 0);
		MessageData messageData = new MessageData();
		messageData.mid = id;
		messageData.mTextStr = textStr;
		if(null != picStr) {
			String[] picStrArr = picStr.split(",");
			for(int i = 0; i < picStrArr.length; i++) {
				TitleData picData = new TitleData(Utils.FILE_TYPE_PIC, picStrArr[i]);
				messageData.mTitleDataList.add(picData);
			}
		}
		if(null != voiStr) {
			String[] voiStrArr = voiStr.split(",");
			for(int i = 0; i < voiStrArr.length; i++) {
				TitleData voiData = new TitleData(Utils.FILE_TYPE_VOI, voiStrArr[i]);
				messageData.mTitleDataList.add(voiData);
			}
		}
		boxData.mMessageList.add(messageData);
		boxData.name = "Test123";
		Log.i(Utils.TAG, "size = " + boxData.mMessageList.size());
		Log.i(Utils.TAG, "mDataList.size = " + mDataList.get(3).mMessageList.size());
		mDataList.add(boxData);
		mMessageAdapter.notifyDataSetChanged();
		*/
	}
	
	/**
	 * 显示消息文本内容
	 */
	private void showMessageText() {
		if(View.VISIBLE != mMessageText.getVisibility()) {
			mMessageText.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 隐藏消息文本内容
	 */
	private void hideMessageText() {
		if(View.INVISIBLE != mMessageText.getVisibility()) {
			mMessageText.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		BoxData boxData = mDataList.get(arg2);
		/*
		if(boxData.mMessageList.size() > 0) {
			mTitleDataArr = boxData.mMessageList.get(0).mTitleDataList;
		}else {
			mTitleDataArr = new ArrayList<TitleData>();
		}
		mActivity.mTitle.notifyTitleDataChange();
		mMessageText.setText(boxData.mMessageList.get(0).mTextStr);
		*/
		showMessageText();
	}
}
