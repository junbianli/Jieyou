package com.oss.comon.network;

import java.nio.charset.Charset;
import java.util.HashMap;

public class NetworkManager {
	private static NetworkManager mInstance;
	private NetworkSchedule mNetworkSchedule;
	private NetworkManager() {
		mNetworkSchedule = new NetworkSchedule(this);
		
	}
	
	public static NetworkManager getInstance() {
		if(null == mInstance) {
			mInstance = new NetworkManager();
		}
		return mInstance;
	}
	
	public void startRequest(String requestUrl, String requestType, Charset requestCharset, HashMap<Object, Object> requestDataMap, NetworkRequestListener requestListener, boolean handleAtMainThreadFlag) {
		NetworkRequestData requestData = new NetworkRequestData(requestType, requestUrl, requestDataMap, requestCharset);
		NetworkRequest request = new NetworkRequest(this, requestListener, requestData);
		NetworkWorker networkWorker = new NetworkWorker(this, mNetworkSchedule, request);
		mNetworkSchedule.startRequest(networkWorker);
	}
}
