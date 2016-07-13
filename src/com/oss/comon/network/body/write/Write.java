package com.oss.comon.network.body.write;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.oss.comon.network.NetworkCommonListener;
import com.oss.comon.network.NetworkManager;
import com.oss.jieyou.BaseModule;
import com.oss.jieyou.MainActivity;
import com.oss.jieyou.R;
import com.oss.jieyou.Utils;
import com.oss.jieyou.record.Record;
import com.oss.jieyou.record.RecordListener;
import com.oss.jieyou.title.TitleClickListener;
import com.oss.jieyou.title.TitleData;

public class Write extends BaseModule implements TitleClickListener, RecordListener{
	private EditText mWrite;
	private RelativeLayout mWriteLayout;
	private List<TitleData> mTitleDataList;
	private String TAG = "Write";
	private int mUploadNum = 0;
	private int mCurrentType = 0;
	private Record mRecord;
	private String picStr;
	private String voiStr;
	private String textStr;
	private int mId;
	public Write(MainActivity activity) {
		mActivity = activity;
		mTitleDataList = new ArrayList<TitleData>();
		mRecord = new Record(mActivity, this);
		mUploadNum = 0;
		picStr = null;
		voiStr = null;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView(MainActivity activity) {
		// TODO Auto-generated method stub
		setUpViews();
		mRecord.initView(mActivity);
	}

	@Override
	public void destroyView() {
		// TODO Auto-generated method stub
		
	}
	
	private void setUpViews() {
		mWrite = (EditText) mActivity.findViewById(R.id.mWrite);
		mWriteLayout = (RelativeLayout) mActivity.findViewById(R.id.mWriteLayout);
	}

	@Override
	public void showView() {
		// TODO Auto-generated method stub
		super.showView();
		if(View.INVISIBLE == mWriteLayout.getVisibility()) {
			mWriteLayout.setVisibility(View.VISIBLE);
		}
		mWrite.setText("showView");
		Log.i(TAG, "visile = " + View.VISIBLE);
		Log.i(TAG, "witelayout = " + mWriteLayout.getVisibility());
		Log.i(TAG, "edittext = " + mWrite.getVisibility());
	}

	@Override
	public void hideView() {
		// TODO Auto-generated method stub
		super.hideView();
		if(View.VISIBLE == mWriteLayout.getVisibility()) {
			mWriteLayout.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 先上传图片，图片上传完毕后发送消息
	 */
	@Override
	public void onLeftTitleClick() {
		// TODO Auto-generated method stub
		uploadFile();
	}

	@Override
	public void onRightTitleClick() {
		// TODO Auto-generated method stub
		mActivity.mTitle.hideOrShowExt();
	}
	
	/**
	 * 重置界面
	 */
	private void resetView() {
		mWrite.setText(null);
	}
	
	/**
	 * 发送消息
	 * @param content
	 */
	private void sendWrite() {
		textStr = mWrite.getText().toString();
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

	/**
	 * 选择图片后的处理函数
	 */
	@Override
	public void onActivityResult(Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(intent);
		Uri uri = intent.getData();
		Log.i(Utils.TAG, uri.toString());
		ContentResolver cr = mActivity.getContentResolver();
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
			String picStr = Utils.getPicBinaryStr(bitmap);
		    Log.i(Utils.TAG, "picStr = " + picStr);
		    TitleData titleData = new TitleData(Utils.FILE_TYPE_PIC, picStr);
		    mTitleDataList.add(titleData);
		    notifyTitleDataChanged();
		} catch (FileNotFoundException e) {
			Log.e("Exception", e.getMessage(),e);
		}
	}

	@Override
	public List<TitleData> getTitleDataArr() {
		// TODO Auto-generated method stub
		return mTitleDataList;
	}
	
	/**
	 * 通知标题栏附件视图更新
	 */
	private void notifyTitleDataChanged() {
		mActivity.mTitle.notifyTitleDataChange();
	}

	/**
	 * 录音结束
	 */
	@Override
	public void onRecordFinished(String path) {
		// TODO Auto-generated method stub
	    TitleData titleData = new TitleData(Utils.FILE_TYPE_VOI, path);
		mTitleDataList.add(titleData);
		notifyTitleDataChanged();
	}
	
	/**
	 * 取消录音
	 */
	@Override
	public void onRecordCanceled() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 语音按钮点击事件透传
	 */
	public void handleVoiClick() {
		mRecord.handleVoiClick();
	}
	
	private void trySendMsag() {
		if(mUploadNum >= mTitleDataList.size()) {
			sendWrite();
		}
	}
	
	/**
	 * 上传文件
	 */
	private void uploadFile() {
		if(mUploadNum < mTitleDataList.size()) {
			TitleData titleData = mTitleDataList.get(mUploadNum);
			mCurrentType = titleData.type;
			if(Utils.FILE_TYPE_PIC == mCurrentType) {
				uploadPic(titleData.content);
			}
			if(Utils.FILE_TYPE_VOI == mCurrentType) {
				uploadVoi(titleData.content);
			}
		}
		if(mTitleDataList.size() == 0) {
			trySendMsag();
		}
	}
	
	/**
	 * 上传图片
	 * @param 图片内容
	 */
	private void uploadPic(String content) {
		content = Utils.convertToUploadFormat(content);
		HashMap<Object, Object> dataMap = new HashMap<Object, Object>();
		dataMap.put("cid", Utils.getDeviceId(mActivity));
		dataMap.put("content", content);
		//String requestUrl = "http://jieyouzahuopu.duapp.com/index.php?r=api/upload-file";
		String requestUrl = Utils.BASE_URL + Utils.UPLOAD_FILE;
		Log.i(TAG, "requestUrl = " + requestUrl);
		//String requestUrl = "http://bj.bcebos.com/jy-resource/575e32e397b3e?authorization=bce-auth-v1%2F05PhqPsRW9rpfjYgxDT1SOxg%2F2016-06-13T04%3A13%3A23Z%2F-1%2F%2F31d30ab50190d903de71e9b72f94ff7864c4feecc9446b33fb9051704a6365cf";
		UploadFileNetworkListener requestListener = new UploadFileNetworkListener();
		NetworkManager.getInstance().startRequest(requestUrl, HttpPost.METHOD_NAME, null, dataMap,  requestListener, false);
	}
	
	private void uploadVoi(String path) {
		String content = Utils.getFileBinaryStr(path);
		content = Utils.convertToUploadFormat(content);
		HashMap<Object, Object> dataMap = new HashMap<Object, Object>();
		dataMap.put("cid", Utils.getDeviceId(mActivity));
		dataMap.put("content", content);
		//String requestUrl = "http://jieyouzahuopu.duapp.com/index.php?r=api/upload-file";
		String requestUrl = Utils.BASE_URL + Utils.UPLOAD_FILE;
		Log.i(TAG, "requestUrl = " + requestUrl);
		//String requestUrl = "http://bj.bcebos.com/jy-resource/575e32e397b3e?authorization=bce-auth-v1%2F05PhqPsRW9rpfjYgxDT1SOxg%2F2016-06-13T04%3A13%3A23Z%2F-1%2F%2F31d30ab50190d903de71e9b72f94ff7864c4feecc9446b33fb9051704a6365cf";
		UploadFileNetworkListener requestListener = new UploadFileNetworkListener();
		NetworkManager.getInstance().startRequest(requestUrl, HttpPost.METHOD_NAME, null, dataMap,  requestListener, false);
	}
	
	class UploadFileNetworkListener extends NetworkCommonListener{
		
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
						JSONObject data = sendResult.getJSONObject("data");
						String url = data.getString("url");
						if(Utils.FILE_TYPE_PIC == mCurrentType) {
							picStr += url;
							picStr += ",";
						}
						if(Utils.FILE_TYPE_VOI == mCurrentType) {
							voiStr += url;
							voiStr += ",";
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(mActivity, mActivity.getString(R.string.send_failed), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			mUploadNum++;
			uploadFile();
			trySendMsag();
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
	
	private void addMessageToSended() {
		mActivity.mBody.addMessageToSended(mId, textStr, picStr, voiStr);
	}
}
