package com.oss.jieyou.record;

public interface RecordListener {
	/**
	 * 录音结束后的回调，文件在path指定的位置
	 * @param path
	 */
	public void onRecordFinished(String path);
	/**
	 * 录音取消后的回调
	 */
	public void onRecordCanceled();
}
