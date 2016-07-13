package com.oss.jieyou;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

/**
 * 工具类
 * @author bianlijun
 *
 */
public class Utils {
	
	//body页面控制
	public static final int PAGE_BOX = 0;
	public static final int PAGE_WRITE = 1;
	public static final int PAGE_FEEDBACK = 2;
	
	//url定义
	public static final String BASE_URL = "http://jieyouzahuopu.duapp.com/index.php?r=api/";
	public static final String ADD_STATICS = "add-statics";
	public static final String SEND_MSG = "send-msg";
	public static final String UPLOAD_FILE = "upload-file";
	
	//消息类型
	public static final int TYPE_SEND = 0;
	
	//接口成功标识
	public static final int INTERFACE_SUCCESS = 0;
	
	//统计类型
	public static final int STAT_START = 1;
	public static final int STAT_PAY = 2;
	public static final int STAT_PAYED = 3;
	
	//文件类型
	public static final int FILE_TYPE_PIC = 1;
	public static final int FILE_TYPE_VOI = 2;
	
	
	//图片大小
	public static final int FILE_WH_SIZE = 120;
	public static final String TAG = "JieYou";
	
	/**
	 * 获取设备id
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
		String deviceId = tm.getDeviceId();
		return deviceId;
	}
	
	/**
	 * 图片转化为二进制字符串
	 */
	public static String getPicBinaryStr(Bitmap bitmap) {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOutput);
	    try {
			byteOutput.flush();
			byteOutput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        byte[] bitmapBytes = byteOutput.toByteArray();
        return Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
	}
	
	public static String getFileBinaryStr(String path) {
		String result = null;
		try{
			File file = new File(path);
			FileInputStream inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int)file.length()];
			inputFile.read(buffer);
			inputFile.close();
			result = Base64.encodeToString(buffer,Base64.DEFAULT);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 字符串将+替换为-，/替换为_
	 * @param previousStr
	 * @return
	 */
	public static String convertToUploadFormat(String previousStr) {
		return previousStr.replace('+', '-').replace('/', '_');
	}
	
	public static Bitmap convertStrToBitmap(String picStr) {
		Log.i(Utils.TAG, "===picStr = " + picStr);
		byte[] picBytes = Base64.decode(picStr.getBytes(), Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
		bitmap = Bitmap.createScaledBitmap(bitmap, Utils.FILE_WH_SIZE, Utils.FILE_WH_SIZE, true);
		return bitmap;
	}
}
