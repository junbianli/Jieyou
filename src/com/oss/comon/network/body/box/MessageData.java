package com.oss.comon.network.body.box;

import java.util.ArrayList;
import java.util.List;

import com.oss.jieyou.title.TitleData;

public class MessageData {
	public int type;
	public String mTextStr;
	public List<TitleData> mTitleDataList;
	public int mid;
	
	public MessageData() {
		mid = 0;
		mTextStr = new String();
		mTitleDataList = new ArrayList<TitleData>();
	}
}
