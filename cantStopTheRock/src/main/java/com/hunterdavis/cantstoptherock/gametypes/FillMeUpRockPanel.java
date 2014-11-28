package com.hunterdavis.cantstoptherock.gametypes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

import com.hunterdavis.cantstoptherock.types.Balloon;
import com.hunterdavis.cantstoptherock.util.AudioUtils;

/**
 * Created by hunter on 11/28/14.
 */
public class FillMeUpRockPanel extends BaseStopTheRockPanel {

    public int heroWinSize = 300;

    public FillMeUpRockPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void updateCurrentRockPositionTick() {
        if(hero.updateCurrentPositionAndPopOverlapsAndPlayNotes(balloons)) {
            //audioManager.pauseSong();
            hero.size ++;
            if(hero.size > heroWinSize) {
                gameOver = true;
            }

            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    AudioUtils.playSoundForBalloonPop(mContext, audioManager, new Balloon(hero.xLocation, hero.yLocation, 0, 0), mHeight);
                    return null;
                }
            }.execute();
        };
    }
}
