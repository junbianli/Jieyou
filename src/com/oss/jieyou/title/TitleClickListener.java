package com.oss.jieyou.title;

import java.util.List;

/**
 * 标题点击接听器
 * @author bianlijun
 *
 */
public interface TitleClickListener {
	public void onLeftTitleClick();
	public void onRightTitleClick();
	public List<TitleData> getTitleDataArr();
}
