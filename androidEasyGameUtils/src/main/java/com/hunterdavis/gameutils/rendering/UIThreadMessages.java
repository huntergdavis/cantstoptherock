package com.hunterdavis.gameutils.rendering;

/*
 * UiThreadMessages is a "java esque" enum
 * Each of the thread messages needs to be
 * global, able to be accessed from any
 * classes included, and used as a set of 
 * unique identifiers for each of the cross
 * thread handler message types 
 */
public enum UIThreadMessages {
	UPDATEFPS ( 11),
	SCREENDRAWN (22),
	SCREENRESIZED (23);
	
	private final int messageNum;
	UIThreadMessages(int messageNum) {
		this.messageNum = messageNum;
	}
	
	public int value() {
		return this.messageNum;
	}
}
