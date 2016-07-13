package com.oss.jieyou.stat;

import java.util.HashMap;

import org.apache.http.client.methods.HttpPost;

import android.util.Log;

import com.oss.comon.network.NetworkManager;
import com.oss.jieyou.MainActivity;
import com.oss.jieyou.Utils;

/**
 * 统计类
 * @author bianlijun
 *
 */
public class Stat {
	private MainActivity mActivity;
	
	public Stat(MainActivity activity) {
		mActivity = activity;
	}
	
	/**
	 * 统计应用程序打开
	 */
	public void statOpen() {
		HashMap<Object, Object> dataMap = new HashMap<Object, Object>();
		dataMap.put("cid", Utils.getDeviceId(mActivity));
		dataMap.put("type", Utils.STAT_START);
		String requestUrl = Utils.BASE_URL + Utils.ADD_STATICS;
		Log.i(Utils.TAG, "requestUrl = " + requestUrl);
		NetworkManager.getInstance().startRequest(requestUrl, HttpPost.METHOD_NAME, null, dataMap,  null, false);
	}
}
