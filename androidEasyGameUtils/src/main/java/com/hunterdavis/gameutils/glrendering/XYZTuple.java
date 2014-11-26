package com.hunterdavis.gameutils.glrendering;

/*
 * a simple thread safe XYZ tuple with getters and setters
 */
public class XYZTuple {
	private float x;
	private float y;
	private float z;
	
	public XYZTuple() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public XYZTuple(float newX, float newY, float newZ) {
		x = newX;
		y = newY;
		z = newZ;
	}
	
	public XYZTuple(XYZTuple toSet) {
		setX(toSet.getX());
		setY(toSet.getY());
		setZ(toSet.getZ());
	}
	
	public synchronized void setX(float xToSet) {
		x = xToSet;
	}
	public synchronized void setY(float yToSet) {
		x = yToSet;
	}
	public synchronized void setZ(float zToSet) {
		x = zToSet;
	}
	
	public synchronized float getX() {
		return x;
	}
	public synchronized float getY() {
		return y;
	}
	public synchronized float getZ() {
		return z;
	}
}
