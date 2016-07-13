package com.oss.comon.network;

import java.util.List;
import java.util.Map;
/**
 * Detault NetworkRequestListener
 * @author bianlijun
 *
 */
public class NetworkCommonListener implements NetworkRequestListener{
	
	@Override
	public boolean onConnStart() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onResponseCode(int code) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onReceivedHeaders(Map<String, List<String>> headers) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onReceivedDataAtMainThread(String receivedData) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onReceivedDataAtSubThread(String receivedData) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onConnShutdown() {
		// TODO Auto-generated method stub
		return true;
	}

}
