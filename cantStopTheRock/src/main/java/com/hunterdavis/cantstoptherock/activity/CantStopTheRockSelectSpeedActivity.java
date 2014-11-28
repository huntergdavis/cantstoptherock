package com.hunterdavis.cantstoptherock.activity;


import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.hunterdavis.cantstoptherock.R;
import com.hunterdavis.cantstoptherock.types.Difficulty;
import com.hunterdavis.cantstoptherock.types.GameType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class CantStopTheRockSelectSpeedActivity extends Activity {

    public static final String GAME_TYPE_EXTRA = "gameType";

    private GameType gameType = GameType.VANILLA;

    public static final void startCantStopTheRockSelectSpeedScreen(Context context,
                                                                   GameType gameType) {
        // create the new title screen intent
        Intent launchIntent = new Intent(context, CantStopTheRockSelectSpeedActivity.class);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchIntent.putExtra(GAME_TYPE_EXTRA, gameType);

        // start title screen.
        context.startActivity(launchIntent);
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Intent gameTypeIntent = getIntent();
        Bundle extras = gameTypeIntent.getExtras();
        GameType gameTypeSelect = (GameType) extras.get(GAME_TYPE_EXTRA);
        if(gameTypeSelect != null) {
            gameType = gameTypeSelect;
        }

		setContentView(R.layout.game_speed_select_screen);

		ImageButton easyButton = (ImageButton) findViewById(R.id.easyButton);
		ImageButton mediumButton = (ImageButton) findViewById(R.id.mediumButton);
		ImageButton hardButton = (ImageButton) findViewById(R.id.hardButton);

		easyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCantStopTheRock(v.getContext(), Difficulty.EASY, gameType);
			}
		});

		mediumButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCantStopTheRock(v.getContext(), Difficulty.MEDIUM, gameType);
			}
		});

		hardButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCantStopTheRock(v.getContext(), Difficulty.HARD, gameType);
			}
		});
		
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

	}

	public void startCantStopTheRock(Context context, Difficulty difficulty, GameType gameType) {
		CantStopTheRockActivity.startCantStopTheRockGameScreen(context, difficulty, gameType);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
}
