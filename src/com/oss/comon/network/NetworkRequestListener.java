package com.oss.comon.network;

import java.util.List;
import java.util.Map;

public interface NetworkRequestListener {
	/**
	 * Notice the caller that the connection has started
	 * @return
	 */
	boolean onConnStart();
	
	/**
	 * Use this method to check whether the response is right
	 * @param code
	 * @return
	 */
	boolean onResponseCode(int code);
	
	/**
	 * HttpUrlConnection received header will call this method
	 * @param headers
	 * @return
	 */
	boolean onReceivedHeaders(Map<String, List<String>> headers);
	
	/**
	 * Receive data at MainThread, notice this is UI thread, do not handle long time consuming work at this method
	 * @param receivedData
	 * @return
	 */
	boolean onReceivedDataAtMainThread(String receivedData);
	
	/**
	 * Receive data at SubThtread, notice you can not do view related work at this method
	 * @param receivedData
	 * @return
	 */
	boolean onReceivedDataAtSubThread(String receivedData);
	
	/**
	 * Check whether need close connection after this network request
	 * @return
	 */
	boolean onConnShutdown();
}
