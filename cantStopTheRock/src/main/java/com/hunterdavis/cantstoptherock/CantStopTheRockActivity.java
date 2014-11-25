package com.hunterdavis.cantstoptherock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hunterdavis.easyaudiomanager.EasyAudioManager;

public class CantStopTheRockActivity extends Activity {

	/** The Constant gameSpeed. */
	public static final String gameSpeed = "gameSpeed";

	/** The audio manager. */
	EasyAudioManager audioManager;

	/** The game panel. */
	StopTheRockPanel stopTheRockPanel;

	/** The times resumed. */
	private int timesResumed = 0;

    // the game speed
    private int gameSpeedSelected = 3;

	/**
	 * Start pop x color baloons screen.
	 * 
	 * @param context
	 *            the context
	 * @param difficulty
	 *            the number baloons to match
	 */
	public static final void startCantStopTheRockGameScreen(Context context,
                                                            int difficulty) {
		// create the new title screen intent
		Intent launchIntent = new Intent(context, CantStopTheRockActivity.class);
		launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		launchIntent.putExtra(gameSpeed, difficulty);

		// start title screen.
		context.startActivity(launchIntent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent baloonIntent = getIntent();
		Bundle extras = baloonIntent.getExtras();
		int gameSpeedSelect = extras.getInt(gameSpeed, -1);
		if (gameSpeedSelect > 0) {
            gameSpeedSelected = gameSpeedSelect;
		}

		// Set window fullscreen and remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_cant_stop_the_rock);
		// at this point the layout should be inflated, so
		stopTheRockPanel = (StopTheRockPanel) findViewById(R.id.SurfaceView01);
        stopTheRockPanel.updateGameSpeed(gameSpeedSelected);

		// create the audioManager
		int[] soundBites = new int[1];
		soundBites[0] = R.raw.balloonpop;
		audioManager = new EasyAudioManager(this, soundBites);
		audioManager.setSongAndOnComplete(this,
                R.raw.popxcolorballoonsgametheme, new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.seekTo(0);
                        mp.start();
                    }

                });
		audioManager.playSong();

		stopTheRockPanel.setAudioManager(audioManager);
		if (stopTheRockPanel.surfaceCreated == true) {
			stopTheRockPanel.createThread(stopTheRockPanel.getHolder());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (stopTheRockPanel != null) {
			stopTheRockPanel.terminateThread();
		}
		if ((audioManager != null) && (audioManager.songPlaying)) {
			audioManager.pauseSong();
		}
		System.gc();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		timesResumed++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (audioManager != null) {
			if (audioManager.songPlaying) {
				audioManager.pauseSong();
			}
			audioManager = null;
		}
	}

}
