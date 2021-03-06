package com.floyd.diamond.channel.threadpool;

public interface ExecutedTask {

	/**
	 * 取消执行
	 */
	void cancel();

	/**
	 * 是否已经设置cancelled
	 * 
	 * @return
	 */
	boolean isCancelled();

}
