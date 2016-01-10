package com.floyd.diamond.notice;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class XPushHandlerThread {
	private static class LazyHolder {
		private static final XPushHandlerThread INSTANCE = new XPushHandlerThread();
	}

	public static XPushHandlerThread getInstance() {
		return LazyHolder.INSTANCE;
	}

	private Handler mHandler;

	private XPushHandlerThread() {
//		init();
	}

	public Handler getHandler() {
		return mHandler;
	}

	public Looper getLooper() {
		return mHandler.getLooper();
	}

	public void init() {
		HandlerThread thread = new HandlerThread("NotificationHandlerThread");
		thread.start();
		mHandler = new Handler(thread.getLooper());
	}

}
