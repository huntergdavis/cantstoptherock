package com.hunterdavis.gameutils.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// TODO: Auto-generated Javadoc
/**
 * The Class GameSurfaceView.
 */
public abstract class GameSurfaceView extends SurfaceView {

	/** The canvasthread. */
	private GameCanvasThread canvasthread;
	
	
	/**
	 * Instantiates a new game surface view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super( context, attrs);
	}
	
	/**
	 * Update game state.
	 */
	public abstract void updateGameState();
	
	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	public abstract void onDraw(Canvas c);
	
	/**
	 * Creates the thread.
	 *
	 * @param holder the holder
	 */
	public void createThread(SurfaceHolder holder) {
		canvasthread = new GameCanvasThread(getHolder(), this, 35);
		canvasthread.setRunning(true);
		canvasthread.start();
	}
	

	/**
	 * Terminate thread.
	 */
	public void terminateThread() {
		if (canvasthread != null) {
			canvasthread.setRunning(false);
			try {
				canvasthread.join();
			} catch (InterruptedException e) {

			}
		}
	}

}
