package com.hunterdavis.cantstoptherock.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hunterdavis.cantstoptherock.R;
import com.hunterdavis.cantstoptherock.gametypes.BaseStopTheRockPanel;
import com.hunterdavis.cantstoptherock.gametypes.BossFightGamePanel;
import com.hunterdavis.cantstoptherock.gametypes.FillMeUpRockPanel;
import com.hunterdavis.cantstoptherock.types.Difficulty;
import com.hunterdavis.cantstoptherock.types.GameType;
import com.hunterdavis.cantstoptherock.gametypes.MusicalNotesRockPanel;
import com.hunterdavis.cantstoptherock.gametypes.VanillaStopTheRockPanel;
import com.hunterdavis.cantstoptherock.util.AudioUtils;
import com.hunterdavis.easyaudiomanager.EasyAudioManager;

public class CantStopTheRockActivity extends Activity {

	/** The Constant gameSpeed. */
	public static final String gameSpeed = "gameSpeed";

	/** The background audio player */
	EasyAudioManager audioManager;

	/** The game panel. */
	BaseStopTheRockPanel stopTheRockPanel;

	/** The times resumed. */
	private int timesResumed = 0;

    // the game speed
    private Difficulty gameSpeedSelected = Difficulty.MEDIUM;

    // the gametype
    private GameType gameType;

	/**
	 * Start pop x color baloons screen.
	 * 
	 * @param context
	 *            the context
	 * @param difficulty
	 *            the number baloons to match
	 */
	public static final void startCantStopTheRockGameScreen(Context context,
                                                            Difficulty difficulty,
                                                            GameType gameType) {
		// create the new title screen intent
		Intent launchIntent = new Intent(context, CantStopTheRockActivity.class);
		launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		launchIntent.putExtra(gameSpeed, difficulty);
        launchIntent.putExtra(CantStopTheRockSelectSpeedActivity.GAME_TYPE_EXTRA, gameType);

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
        Difficulty gameSpeedSelect = (Difficulty) extras.get(gameSpeed);
		if(gameSpeedSelect != null) {
            gameSpeedSelected = gameSpeedSelect;
        }
        GameType gameTypeSelect = (GameType) extras.get(CantStopTheRockSelectSpeedActivity.GAME_TYPE_EXTRA);
        if(gameTypeSelect != null) {
            gameType = gameTypeSelect;
        }

		// Set window fullscreen and remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_cant_stop_the_rock);

		// at this point the layout should be inflated, so
		stopTheRockPanel = getGamePanelBasedOnGameType(this,gameType);

        LinearLayout ourLayout = (LinearLayout) findViewById(R.id.cantstopPanelHolder);
        ourLayout.addView(stopTheRockPanel);

        switch (gameSpeedSelected) {
            case EASY:
                stopTheRockPanel.updateGameSpeed(8);
                break;
            case MEDIUM:
                stopTheRockPanel.updateGameSpeed(16);
                break;
            case HARD:
                stopTheRockPanel.updateGameSpeed(32);
                break;
            default:
                stopTheRockPanel.updateGameSpeed(16);
        }

		// create the audioManager
		audioManager = new EasyAudioManager(this, AudioUtils.soundPool);
		audioManager.setSongAndOnComplete(this,
                getSongReferenceBasedOnDifficulty(gameSpeedSelected), new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.seekTo(0);
                        mp.start();
                    }

                });

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                audioManager.playSong();
                return null;
            }
        }.execute();



		stopTheRockPanel.setAudioManager(audioManager);
		if (stopTheRockPanel.surfaceCreated == true) {
			stopTheRockPanel.createThread(stopTheRockPanel.getHolder());
		}

	}

    public BaseStopTheRockPanel getGamePanelBasedOnGameType(Context context, GameType gameType) {
        switch (gameType) {
            default:
            case VANILLA:
                return new VanillaStopTheRockPanel(context,null);
            case MUSIC_NOTES:
                return new MusicalNotesRockPanel(context,null);
            case FILL_ME_UP:
                return new FillMeUpRockPanel(context, null);
            case BOSS_FIGHT:
                return new BossFightGamePanel(context, null);
        }
    }

    public int getSongReferenceBasedOnDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return R.raw.easy;
            case MEDIUM:
                return R.raw.medium;
            case HARD:
                return R.raw.hard;
            default:
                return R.raw.medium;
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
