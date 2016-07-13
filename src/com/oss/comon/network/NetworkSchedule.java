package com.oss.comon.network;

import java.util.LinkedList;
import java.util.Queue;

public class NetworkSchedule {
	private static final String LOG_TAG = NetworkSchedule.class.getSimpleName();
	
	private Queue<NetworkWorker> mNetWorkerQueue;
	private NetworkWorker mCurrentWorker;
	private NetworkManager mNetworkManager;
	
	
	public NetworkSchedule(NetworkManager networkManager) {
		mNetworkManager = networkManager;
		mNetWorkerQueue = new LinkedList<NetworkWorker>();
	}
	
	public void startRequest(NetworkWorker networkWorker) {
		mNetWorkerQueue.offer(networkWorker);
		if(null == mCurrentWorker || mCurrentWorker.getWorkState() == NetworkConstant.WORKER_IDLE) {
			mCurrentWorker = mNetWorkerQueue.poll();
			mCurrentWorker.startWork();
		}
	}

	/**
	 * 释放内存
	 */
	public void stop() {
		mCurrentWorker.cancel(true);
		mNetWorkerQueue.clear();
		mNetWorkerQueue = null;
	}

	/**
	 * 循环
	 */
	public void loop() {
		mCurrentWorker = mNetWorkerQueue.poll();
		if(null != mCurrentWorker) {
			mCurrentWorker.startWork();
		}
	}
}
