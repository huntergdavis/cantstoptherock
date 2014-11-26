package com.hunterdavis.gameutils.credits;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.hunterdavis.gameutils.rendering.GameSurfaceView;

// TODO: Auto-generated Javadoc
/**
 * The Class CreditsPanel.
 */
class CreditsPanel extends GameSurfaceView implements SurfaceHolder.Callback {
	// values
	/** The Text tick value. */
	private static float TextTickValue = (float) 6.0f;

	/** The Constant TextTickFastForwardSpeed. */
	private static final float TextTickFastForwardSpeed = 32.0f;

	/** The Constant TextTickSlowSpeed. */
	private static final float TextTickSlowSpeed = 6.0f;

	// member variables
	/** The surface created. */
	public Boolean surfaceCreated;

	/** The m context. */
	public Context mContext;

	/** The m width. */
	private int mWidth = 0;

	/** The m height. */
	private int mHeight = 0;

	/** The game over. */
	private boolean gameOver = false;

	/** The current credit top line item. */
	private int currentCreditTopLineItem = 0;

	/** The number of lines on screen. */
	private int numberOfLinesOnScreen = 1;

	/** The credits. */
	List<creditsLineItem> credits = new ArrayList<creditsLineItem>();

	/** The paint. */
	Paint paint = null;

	/** The final score. */
	private String finalScore = "";

	/* CG Functions reference */
	private int creditsEndingImage = -1;

	private Bitmap finalImageBitmap = null;

	// each credits line is a tiny inner class for storing credits lines
	/**
	 * The Class creditsLineItem.
	 */
	class creditsLineItem {

		/** The line. */
		String line;

		/** The age. */
		int age;

		/** The accumulated height ticks. */
		float accumulatedHeightTicks;

		/**
		 * Instantiates a new credits line item.
		 * 
		 * @param lineToStore
		 *            the line to store
		 */
		creditsLineItem(String lineToStore) {
			line = lineToStore;
			age = 0;
			accumulatedHeightTicks = 0;
		}

		/**
		 * Instantiates a new credits line item.
		 * 
		 * @param lineToStore
		 *            the line to store
		 * @param ageToInit
		 *            the age to init
		 * @param yPosInitial
		 *            the y pos initial
		 */
		creditsLineItem(String lineToStore, int ageToInit, int yPosInitial) {
			line = lineToStore;
			age = ageToInit;
			accumulatedHeightTicks = yPosInitial;
		}
	}

	/**
	 * Read in credits txt.
	 * 
	 * @param creditsReference
	 *            the credits reference
	 * @param scoreText
	 *            the score text
	 */
	public void readInCreditsTxt(int creditsReference, String scoreText) {
		InputStream inputStream = getResources().openRawResource(
				creditsReference);
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String eachLine = "";
		while (eachLine != null) {
			credits.add(new creditsLineItem(eachLine));
			try {
				eachLine = bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if ((scoreText != null) && !scoreText.equals("")) {
			finalScore = scoreText;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		synchronized (getHolder()) {

			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				TextTickValue = TextTickFastForwardSpeed;
				if (gameOver == true) {
					doLose();
				}
				return true;
			} else if (action == MotionEvent.ACTION_MOVE) {

				return true;
			} else if (action == MotionEvent.ACTION_UP) {
				TextTickValue = TextTickSlowSpeed;
				return true;
			}

			return true;
		}
	}

	/**
	 * Instantiates a new credits panel.
	 * 
	 * @param context
	 *            the context
	 * @param attrs
	 *            the attrs
	 */
	public CreditsPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		surfaceCreated = false;

		getHolder().addCallback(this);
		setFocusable(true);
	}

	public void setCreditsEndingImage(int creditsImageRef) {
		creditsEndingImage = creditsImageRef;
		Resources res = getResources();
		finalImageBitmap = BitmapFactory.decodeResource(res, creditsImageRef);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder
	 * , int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder
	 * )
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//
		if (surfaceCreated == false) {
			createThread(holder);
			// Bitmap kangoo = BitmapFactory.decodeResource(getResources(),
			// R.drawable.kangoo);
			surfaceCreated = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.
	 * SurfaceHolder)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		surfaceCreated = false;

	}

	// we update the position of the text lines on screen in updateGameState
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hunterdavis.fiveseconds.gameutils.rendering.GameSurfaceView#
	 * updateGameState()
	 */
	public void updateGameState() {

		if (gameOver == true) {
			return;
		}

		// update current line a tick
		updateCurrentLineTick();
	}

	/**
	 * Update current line tick.
	 */
	public void updateCurrentLineTick() {

		if (credits.size() <= currentCreditTopLineItem) {
			gameOver = true;
			return;
		}

		if (credits.get(currentCreditTopLineItem).accumulatedHeightTicks > ((mHeight / 15) * numberOfLinesOnScreen)) {
			numberOfLinesOnScreen++;
		}

		for (int i = 0; i < numberOfLinesOnScreen; i++) {
			if ((currentCreditTopLineItem + i) < credits.size()) {
				credits.get(currentCreditTopLineItem + i).age++;
				credits.get(currentCreditTopLineItem + i).accumulatedHeightTicks += TextTickValue;
			}
		}

		if (credits.get(currentCreditTopLineItem).accumulatedHeightTicks > mHeight) {
			currentCreditTopLineItem++;
			// numberOfLinesOnScreen--;

		}

	}

	/**
	 * Do lose.
	 */
	public void doLose() {
		// quit to mainmenu
		((Activity) mContext).finish();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hunterdavis.fiveseconds.gameutils.rendering.GameSurfaceView#onDraw
	 * (android.graphics.Canvas)
	 */
	@Override
	public void onDraw(Canvas canvas) {

		mWidth = canvas.getWidth();
		mHeight = canvas.getHeight();

		if (paint == null) {
			paint = new Paint();
			paint.setTextAlign(Paint.Align.CENTER);
		}
		paint.setColor(Color.BLACK);
		// clear the screen with the black painter.
		canvas.drawRect(0, 0, mWidth, mHeight, paint);

		// draw game over if game over
		if (gameOver == true) {
			if (finalImageBitmap != null) {
				canvas.drawBitmap(finalImageBitmap, (mWidth / 2)
						- finalImageBitmap.getWidth() / 2, (mHeight / 2)
						- finalImageBitmap.getHeight() / 2, paint);
			}
			paint.setColor(Color.WHITE);
			paint.setTextSize(30);
			canvas.drawText("Game Over", (mWidth / 2), mHeight / 4, paint);
			canvas.drawText(finalScore, mWidth / 2, (mHeight / 4) * 3, paint);
		} else {
			paint.setColor(Color.WHITE);
			paint.setTextSize(30);

			for (int i = 0; i < numberOfLinesOnScreen; i++) {
				if (currentCreditTopLineItem + i < credits.size()) {
					creditsLineItem currentLineItem = credits
							.get(currentCreditTopLineItem + i);
					canvas.drawText(currentLineItem.line, (mWidth / 2),
							(mHeight - currentLineItem.accumulatedHeightTicks),
							paint);
				}
			}
		}
	}

} // end class