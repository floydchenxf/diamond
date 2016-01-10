package com.floyd.diamond.notice;

import android.text.TextUtils;

import java.io.Serializable;

public class NoticeVO implements Serializable {

	private static final long serialVersionUID = -8608282117161157561L;
	public int ticketIconId = NotificationAppConfig.appIcon; // 应用的图标id
	public long msgId;
	public String appId; // 应用id
	public String title; // 提醒标题
	public String content; // 提醒内容
	public String iconUrl; // 图标地址
	public OpenAppVO openAppVO; //打开应用信息
	public long notifyTime; // 提醒时间
	public boolean vibrate; // 是否震动
	public boolean tip; // 是否提示

	public String getTitle() {
		return TextUtils.isEmpty(title) ? appId : title;
	}
}
