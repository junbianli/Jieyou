package com.oss.jieyou.record;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oss.jieyou.MainActivity;
import com.oss.jieyou.Utils;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

/**
 * 录音封装
 * @author bianlijun
 *
 */
public class Recorder {
	private Record mRecord;
	private RecordListener mRecordListener;
	private MediaRecorder recorder;
    private String fileName;
    private String fileFolder = Environment.getExternalStorageDirectory()
            .getPath() + "/JieYou/";
    private boolean isRecording = false;
    private String suffix = ".amr";
    private MainActivity mActivity;
    private String currentFilePath;
	public Recorder(MainActivity activity, RecordListener recordListener) {
		mActivity = activity;
		mRecordListener = recordListener;
	}
	
	/**
	 * 准备录音
	 */
	public void prepareRecord() {
		File file = new File(fileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName = getCurrentDate();
        recorder = new MediaRecorder();
        currentFilePath = fileFolder + fileName + suffix;
        recorder.setOutputFile(currentFilePath);
        Log.i(Utils.TAG, "filePath = " + currentFilePath);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);// 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置MediaRecorder录制音频的编码为amr
	}
	
	/**
	 * 开始录音
	 */
	public void startRecord() {
		if (!isRecording) {
            try {
                recorder.prepare();
                recorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
 
            isRecording = true;
        }
	}
	
	/**
	 * 录音结束
	 */
	public void stopRecord() {
		if (isRecording) {
            recorder.stop();
            recorder.release();
            isRecording = false;
        }
	}
	
	/**
	 * 取消录音
	 */
	public void cancelRecord() {
		File file = new File(currentFilePath);
        file.delete();
		mRecordListener.onRecordCanceled();
		
	}
	
	/**
	 * 录音结束
	 */
	public void finishRecord() {
		mRecordListener.onRecordFinished(currentFilePath);
	}
	
	// 以当前时间作为文件名
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
