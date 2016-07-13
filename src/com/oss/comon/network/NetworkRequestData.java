package com.oss.comon.network;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import android.annotation.SuppressLint;
import android.util.Log;

public class NetworkRequestData {
	/*HttpPost.METHOD_NAME or HttpGet.METHOD_NAME*/
	private static final String LOG_TAG = NetworkRequestData.class.getSimpleName();
	private String mRequestType;
	private String mRequestUrl;
	private byte[] mRequestDataInBytes;
	
	public NetworkRequestData(String requestType, String requestUrl, HashMap<Object, Object> requestMaps, Charset charset) {
		mRequestType = requestType;
		init(requestUrl, requestMaps, charset);
	}
	
	@SuppressLint("NewApi")
	private void init(String url, HashMap<Object, Object> dataMaps, Charset charset) {
		String requestData = null;
		Iterator iter = dataMaps.entrySet().iterator();
		String tmp = null;
		StringBuilder tmpStrBuilder = new StringBuilder();
		while (iter.hasNext()) {
		    Map.Entry entry = (Map.Entry) iter.next();
		    Object key = entry.getKey();
		    Object val = entry.getValue();
		    tmp = key + "=" + val + "&";
		    tmpStrBuilder.append(tmp);
		}
		//tmpStrBuilder.deleteCharAt(tmpStrBuilder.length() - 1);
		requestData = tmpStrBuilder.toString();
		Log.i(LOG_TAG, "requestData = " + requestData);
		if(null == charset) {
			mRequestDataInBytes = requestData.getBytes();
		}else {
			mRequestDataInBytes = requestData.getBytes(charset);
		}
		if(mRequestType == HttpPost.METHOD_NAME) {
			mRequestUrl = url;
		}else{
			mRequestType = HttpGet.METHOD_NAME;
			mRequestUrl = tmpStrBuilder.toString();
		}
		if(NetworkConstant.DEBUG) {
			Log.i(LOG_TAG, "url = " + url);
			Log.i(LOG_TAG, "mRequestUrl = " + mRequestUrl);
		}
	}
	
	public String getRequestType() {
		return mRequestType;
	}
	
	public String getRequestUrl() {
		return mRequestUrl;
	}
	
	public byte[] getRequestDataInByte() {
		return mRequestDataInBytes;
	}
}
