package com.floyd.diamond.notice;

public class Sequence {
	
	private static int seq;
	
	public static synchronized int getCurrentSeq() {
		return seq++;
	}

}
