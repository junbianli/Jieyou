package com.oss.comon.network;

/**
 * 
 * @author bianlijun
 * 
 */
public class NetworkCode{
	public static final int NONE = 0;
	
	public static final int Url_Null = 1;
	public static final int Create_Connect_Fail = 2;
	public static final int Connect_Fail = 3;
	public static final int Post_Data_Fail = 4;
	public static final int Get_Response_Data_Fail = 5;
	public static final int Check_Response_Data_Fail = 6;
	public static final int Get_Data_Fail = 7;
	public static final int Disconnect_Fail = 8;
	public static final int Connect_Shutdown_Fail = 9;
	
	private int mCode;
	private String mNote;
	private String mReceivedData;
	
	public NetworkCode(int code, String note) {
		mCode = code;
		mNote = note;
		mReceivedData = null;
	}
	
	public void set(int code, String note) {
		mCode = code;
		mNote = note;
	}
	
	public void setContent(String pReceivedData) {
		mReceivedData = pReceivedData;
	}
	
	public int getCode() {
		return mCode;
	}
	
	public String getNote() {
		return mNote;
	}
	
	public String getContent() {
		return mReceivedData;
	}
}
