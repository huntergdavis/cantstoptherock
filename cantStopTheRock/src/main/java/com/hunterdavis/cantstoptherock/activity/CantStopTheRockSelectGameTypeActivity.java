package com.hunterdavis.cantstoptherock.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.hunterdavis.cantstoptherock.R;
import com.hunterdavis.cantstoptherock.gametypes.GameType;
import com.hunterdavis.gameutils.title.TitleScreen;

public class CantStopTheRockSelectGameTypeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gametype_select_screen);

		ImageButton vanillaButton = (ImageButton) findViewById(R.id.vanillaButton);
		ImageButton musicNotesButton = (ImageButton) findViewById(R.id.musicNotesButton);

        vanillaButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                selectSpeed(v.getContext(), GameType.VANILLA);
			}
		});

        musicNotesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                selectSpeed(v.getContext(), GameType.MUSIC_NOTES);
			}
		});
		
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

		// create a title screen and throw it up
		TitleScreen.startTitleScreen(getApplicationContext(),
				R.raw.title,
				R.drawable.cantstoptherocktitle, true/* touchToExit */,
				true /* exitOnWavComplete */, 100000/* timeout */, false /*
																	 * landscape
																	 * mode
																	 */);
	}

	public void selectSpeed(Context context, GameType gameType) {
		CantStopTheRockSelectSpeedActivity.startCantStopTheRockSelectSpeedScreen(context,gameType);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
}
