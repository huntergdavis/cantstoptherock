package com.hunterdavis.gameutils.title;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hunterdavis.androideasygameutils.R;
import com.hunterdavis.easyaudiomanager.EasyAudioManager;

// TODO: Auto-generated Javadoc
/**
 * The Class TitleScreen.
 */
public class TitleScreen extends Activity implements
		MediaPlayer.OnCompletionListener {
	
	/** The media player. */
	MediaPlayer mediaPlayer;

	/** The Constant wavReferenceIDString. */
	public static final String wavReferenceIDString = "wavreference";
	
	/** The Constant imageReferenceIDString. */
	public static final String imageReferenceIDString = "imgreference";
	
	/** The Constant touchToExitBooleanID. */
	public static final String touchToExitBooleanID = "touchToExit";
	
	/** The Constant exitOnWavePlayBooleanID. */
	public static final String exitOnWavePlayBooleanID = "exitOnWavPlay";
	
	/** The Constant timeoutIntegerID. */
	public static final String timeoutIntegerID = "timeout";
	
	public static final String landscapeModeID = "landscape";

	/** The img reference. */
	private int imgReference = -1;
	
	/** The wav reference. */
	private int wavReference = -1;
	
	/** The touch to exit. */
	private boolean touchToExit = true;
	
	/** The exit on wav play. */
	private boolean exitOnWavPlay = false;
	
	/** The timeout. */
	private int timeout = -1;
	
	private boolean landscapeMode = false;
	
	/** The audio manager. */
	private EasyAudioManager audioManager;

	/**
	 * Start title screen.
	 *
	 * @param context the context
	 * @param wavRefId the wav ref id
	 * @param imageRefId the image ref id
	 * @param touchToExit the touch to exit
	 * @param exitOnWavComplete the exit on wav complete
	 * @param timeout the timeout
	 */
	public static final void startTitleScreen(Context context, int wavRefId,
			int imageRefId, boolean touchToExit, boolean exitOnWavComplete,
			int timeout, boolean landscapeMode) {
		// create the new title screen intent
		Intent titleIntent = new Intent(context, TitleScreen.class);
		titleIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		titleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		if (wavRefId != -1) {
			titleIntent.putExtra(TitleScreen.wavReferenceIDString, wavRefId);
		}
		if (imageRefId != -1) {
			titleIntent
					.putExtra(TitleScreen.imageReferenceIDString, imageRefId);
		}
		if(landscapeMode == true) {
			titleIntent.putExtra(TitleScreen.landscapeModeID, true);
		}

		titleIntent.putExtra(TitleScreen.exitOnWavePlayBooleanID,
				exitOnWavComplete);
		titleIntent.putExtra(TitleScreen.touchToExitBooleanID, touchToExit);
		if (timeout > 0) {
			titleIntent.putExtra(TitleScreen.timeoutIntegerID, timeout);
		}

		// start title screen.
		context.startActivity(titleIntent);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent startIntent = getIntent();
		if (startIntent != null) {
			imgReference = startIntent.getIntExtra(imageReferenceIDString, -1);
			wavReference = startIntent.getIntExtra(wavReferenceIDString, -1);
			touchToExit = startIntent.getBooleanExtra(touchToExitBooleanID,
					true);
			exitOnWavPlay = startIntent.getBooleanExtra(
					exitOnWavePlayBooleanID, false);
			landscapeMode = startIntent.getBooleanExtra(landscapeModeID, false);
			timeout = startIntent.getIntExtra(timeoutIntegerID, -1);
		}
		
		if(landscapeMode == true) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

		// Set window fullscreen and remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.titlescreen);

		// at this point the layout should be inflated, so
		// maximize the title screen logo here
		if (imgReference != -1) {
			ImageView imageView = (ImageView) findViewById(R.id.titlescreen);
			imageView.setImageResource(imgReference);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			if (touchToExit) {
				OnClickListener buttonClick = new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				};

				imageView.setOnClickListener(buttonClick);
			}
		}

		if (wavReference != -1) {
			// create the audioManager
			audioManager = new EasyAudioManager(this);
			audioManager.setSongAndOnComplete(this, wavReference,
					this);
			audioManager.playSong();
		}
		if (timeout > 0) {
			TitleCountDown localTitleCounter = new TitleCountDown(timeout, 1000);
			localTitleCounter.start();
		}

	}

	/* (non-Javadoc)
	 * @see android.media.MediaPlayer.OnCompletionListener#onCompletion(android.media.MediaPlayer)
	 */
	public void onCompletion(MediaPlayer arg0) {
		if (exitOnWavPlay) {
			finish();
		}
	}

	// countdowntimer is an abstract class, so extend it and fill in methods
	/**
	 * The Class TitleCountDown.
	 */
	public class TitleCountDown extends CountDownTimer {
		
		/**
		 * Instantiates a new title count down.
		 *
		 * @param millisInFuture the millis in future
		 * @param countDownInterval the count down interval
		 */
		public TitleCountDown(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onFinish()
		 */
		@Override
		public void onFinish() {
			finish();
		}

		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onTick(long)
		 */
		@Override
		public void onTick(long millisUntilFinished) {

		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mediaPlayer = null;
	}

}
