package com.hunterdavis.gameutils.time;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.hunterdavis.gameutils.core.SharedGameData;

// game clock countdown timer
/**
 * The Class GameCountDown.
 */
public class FPSClockTimer extends CountDownTimer {

	public float gameClock;
	public boolean gameOver = false;
	SharedGameData sharedGameData;
	TextView fpsView;

	/**
	 * Instantiates a new title count down.
	 * 
	 * @param millisInFuture
	 *            the millis in future
	 * @param countDownInterval
	 *            the count down interval
	 */
	public FPSClockTimer(long millisInFuture, long countDownInterval, Activity activeView, int resId, SharedGameData sharedGameDataToUse) {
		super(millisInFuture, countDownInterval);
		fpsView = (TextView) activeView.findViewById(resId);
		sharedGameData = sharedGameDataToUse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.CountDownTimer#onFinish()
	 */
	@Override
	public void onFinish() {
		gameOver = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.CountDownTimer#onTick(long)
	 */
	@Override
	public void onTick(long millisUntilFinished) {
		if(sharedGameData == null) {
			return;
		}
		fpsView.setText(BigDecimal.valueOf(sharedGameData.getFps()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
	}
}
