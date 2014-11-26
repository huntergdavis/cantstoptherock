package com.hunterdavis.gameutils.core;

public abstract class SharedGameData {

	private long frames = 0;
	private double fps = 0.0;
	
	
	public synchronized void updateFrames() {
		frames++;
	}
	public synchronized long getFrames(){
		return frames;
	}
	
	public synchronized void updateFps(Double newFPS) {
		fps = newFPS;
	}
	
	public synchronized double getFps(){
		return fps;
	}
	
	public abstract Object getGameData();
}
