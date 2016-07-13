package com.oss.jieyou.title;

import java.io.IOException;

import android.media.MediaPlayer;

public class RecordPlayer {
	private MediaPlayer mPlayer;
	public void play(String path) {
		if(null != mPlayer) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(path);
			mPlayer.prepare();
			mPlayer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop() {
		mPlayer.stop();
		mPlayer.release();
		mPlayer = null;
	}
}
