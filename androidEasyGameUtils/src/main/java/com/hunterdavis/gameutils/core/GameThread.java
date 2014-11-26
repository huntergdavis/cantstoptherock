package com.hunterdavis.gameutils.core;


// TODO: Auto-generated Javadoc
/**
 * The Class GameCanvasThread.
 */
public abstract class GameThread extends Thread {
		
	/** The _run. */
	private boolean _run = false;
	// for consistent rendering
	/** The sleep time. */
	private long sleepTime;
	// amount of time to sleep for (in milliseconds)
	/** The delay. */
	private long delay = 35;
	
	private SharedGameData sharedGameData = null;

	/**
	 * Instantiates a new game  thread.
	 *
	 * @param renderDelay the render delay
	 */
	public GameThread(SharedGameData shareData,
			long renderDelay) {
		delay = renderDelay;
		sharedGameData = shareData;
	}

	/**
	 * Sets the running.
	 *
	 * @param run the new running
	 */
	public void setRunning(boolean run) {
		_run = run;
	}

	/**
	 * Sets the delay.
	 *
	 * @param delayToSet the new delay
	 */
	public void setDelay(long delayToSet) {
		delay = delayToSet;
	}

	/**
	 * Gets the running.
	 *
	 * @return the running
	 */
	public boolean getRunning() {
		return _run;
	}
	
	protected abstract void updateGameState();

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		// UPDATE
		while (_run) {
			// time before update
			long beforeTime = System.nanoTime();
			// This is where we update the game engine
			updateGameState();
			

			// SLEEP
			// Sleep time. Time required to sleep to keep game consistent
			// This starts with the specified delay time (in milliseconds) then
			// subtracts from that the
			// actual time it took to update and render the game. This allows
			// our game to render smoothly.
			this.sleepTime = delay
					- ((System.nanoTime() - beforeTime) / 1000000L);

			try {
				// actual sleep code
				if (sleepTime > 0) {
					GameThread.sleep(sleepTime);
				}
			} catch (InterruptedException ex) {

			}
		}

	}

}
