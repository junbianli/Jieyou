package com.oss.comon.network.body.box;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表数据
 * @author bianlijun
 *
 */
public class BoxData {
	public String mFrom;
	public String mTo;
	public int mTime;
	public BoxData(String from, String to, int time) {
		mFrom = from;
		mTo = to;
		mTime = time;
	}
}
