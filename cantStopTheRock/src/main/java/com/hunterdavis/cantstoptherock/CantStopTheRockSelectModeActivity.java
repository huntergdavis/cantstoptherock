package com.hunterdavis.cantstoptherock;


import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.hunterdavis.gameutils.title.TitleScreen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class CantStopTheRockSelectModeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_select_screen);

		ImageButton easyButton = (ImageButton) findViewById(R.id.easyButton);
		ImageButton mediumButton = (ImageButton) findViewById(R.id.mediumButton);
		ImageButton hardButton = (ImageButton) findViewById(R.id.hardButton);

		easyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCantStopTheRock(v.getContext(), CantStopTheRockActivity.Difficulty.EASY);
			}
		});

		mediumButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCantStopTheRock(v.getContext(), CantStopTheRockActivity.Difficulty.MEDIUM);
			}
		});

		hardButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCantStopTheRock(v.getContext(), CantStopTheRockActivity.Difficulty.HARD);
			}
		});
		
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());


		// create a title screen and throw it up
		TitleScreen.startTitleScreen(getApplicationContext(),
				R.raw.title,
				R.drawable.cantstoptherocktitle, false/* touchToExit */,
				true /* exitOnWavComplete */, 100000/* timeout */, false /*
																	 * landscape
																	 * mode
																	 */);
	}

	public void startCantStopTheRock(Context context, CantStopTheRockActivity.Difficulty difficulty) {
		CantStopTheRockActivity.startCantStopTheRockGameScreen(context, difficulty);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
}
