package com.oss.comon.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;


public class NetworkWorker extends AsyncTask<String, Integer, String>{
	
	private static String LOG_TAG = NetworkWorker.class.getSimpleName();
	private NetworkManager mNetworkManager;
	private NetworkSchedule mNetworkSchedule;
	private Context mContext;
	private NetworkRequest mNetworkRequest;
	private int mCurrentWorkState = NetworkConstant.WORKER_IDLE;
	private NetworkCode networkCode;
	
	public NetworkWorker(NetworkManager networkManager, NetworkSchedule networkSchedule, NetworkRequest networkRequest) {
		mNetworkManager = networkManager;
		mNetworkSchedule = networkSchedule;
		mNetworkRequest = networkRequest;
		mCurrentWorkState = NetworkConstant.WORKER_IDLE;
		networkCode = null;
	}
	
	public int getWorkState() {
		return mCurrentWorkState;
	}
	
	@SuppressLint("NewApi")
	public void startWork() {
		Log.i(LOG_TAG, "startWork");
		String tmp = "";
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			execute(tmp);
		} else {
			executeOnExecutor(THREAD_POOL_EXECUTOR, tmp);
		}
	}
	
	@SuppressLint("NewApi")
	private void start(String... aParams) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			execute(aParams);
		} else {
			executeOnExecutor(THREAD_POOL_EXECUTOR, aParams);
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try{
			networkCode = mNetworkRequest.upload();
			Log.i(LOG_TAG, "id = " + networkCode.getCode() + " note = " + networkCode.getNote());
			//return upload();
		} catch(Exception e) {
			if(NetworkConstant.DEBUG) {
				Log.w(LOG_TAG, "doInBackground", e);
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		NetworkRequestListener requestListener = mNetworkRequest.getNetworkRequestListener();
		NetworkRequestData requestData = mNetworkRequest.getNetworkRequestData();
		if(null != requestListener && null != networkCode) {
			requestListener.onReceivedDataAtMainThread(networkCode.getContent());
		}
		super.onPostExecute(result);
	}
	
	
}
