package com.oss.jieyou.record;
import com.oss.jieyou.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * 录音按钮
 * @author bianlijun
 *
 */
public class RecordView extends Button{
	private int mState;
	private int STATE_RECORD = 1;
	private int STATE_UNRECORD = 2;
	private float mDownY;
	private Recorder mRecorder;
	private boolean mIsCancel = false;
	private Context mContext;
	public RecordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		// TODO Auto-generated constructor stub
	}
	
	public void setRecorder(Recorder recorder) {
		mRecorder = recorder;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(null == mRecorder) {
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: // 按下按钮
            if (mState != STATE_RECORD) {
                mDownY = event.getY();
                mRecorder.prepareRecord();
                mRecorder.startRecord();
                mState = STATE_RECORD;
                this.setText(R.string.unrecord);
            }
            break;
        case MotionEvent.ACTION_MOVE: // 滑动手指
            float moveY = event.getY();
            if (mDownY - moveY > 50) {
            	mIsCancel = true;
            }
            if (mDownY - moveY < 20) {
            	mIsCancel = false;
            }
            break;
        case MotionEvent.ACTION_UP: // 松开手指
            if (mState == STATE_RECORD) {
            	mState = STATE_UNRECORD;
            	mRecorder.stopRecord();
                if (mIsCancel) {
                	mRecorder.cancelRecord();
                } else {
                    mRecorder.finishRecord();
                }
                mIsCancel = false;
                this.setText(mContext.getString(R.string.record));
            }
            break;
        }
		return true;
	}

	
}
