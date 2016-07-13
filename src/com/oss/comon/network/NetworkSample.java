package com.oss.comon.network;

import java.util.HashMap;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;


import android.util.Log;

public class NetworkSample {
	private final static String LOG_TAG = "NetworkSample";
	public void test(String content) {
		HashMap<Object, Object> dataMap = new HashMap<Object, Object>();
		dataMap.put("r", "api/upload-file");
		dataMap.put("content", content);
		dataMap.put("cid", "abcdeesseee");
		dataMap.put("test", "123");
		//String requestUrl = "http://jieyouzahuopu.duapp.com/index.php?r=api/upload-file";
		String requestUrl = "http://172.22.155.3/basic/index.php?r=api/upload-file&";
		Log.i(LOG_TAG, "requestUrl = " + requestUrl);
		//String requestUrl = "http://bj.bcebos.com/jy-resource/575e32e397b3e?authorization=bce-auth-v1%2F05PhqPsRW9rpfjYgxDT1SOxg%2F2016-06-13T04%3A13%3A23Z%2F-1%2F%2F31d30ab50190d903de71e9b72f94ff7864c4feecc9446b33fb9051704a6365cf";
		TestNetworkListener requestListener = new TestNetworkListener();
		NetworkManager.getInstance().startRequest(requestUrl, HttpPost.METHOD_NAME, null, dataMap,  requestListener, false);
	}
	
	class TestNetworkListener extends NetworkCommonListener{
		
		@Override
		public boolean onReceivedDataAtMainThread(String receivedStr) {
			// TODO Auto-generated method stub
			Log.i(LOG_TAG, receivedStr);
			Log.i(LOG_TAG, "onReceivedDataAtMainThread  thread id = " + Thread.currentThread().getId());
			return true;
		}
		
		@Override
		public boolean onReceivedDataAtSubThread(String receivedStr) {
			// TODO Auto-generated method stub
			
			String result = new String(receivedStr);
			Log.i(LOG_TAG, result);
			Log.i(LOG_TAG, "onReceivedDataAtSubThread  thread id = " + Thread.currentThread().getId());
			return true;
		}
		
	}
}
