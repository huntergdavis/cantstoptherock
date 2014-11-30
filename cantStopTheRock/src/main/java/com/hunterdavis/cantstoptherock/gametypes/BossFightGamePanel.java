package com.hunterdavis.cantstoptherock.gametypes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

import com.hunterdavis.cantstoptherock.types.OurLittleBossHero;
import com.hunterdavis.cantstoptherock.types.OurLittleHero;
import com.hunterdavis.cantstoptherock.util.AudioUtils;

/**
 * Created by hunter on 11/30/14.
 */
public class BossFightGamePanel extends BaseStopTheRockPanel {

    public BossFightGamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public OurLittleHero getHero() {
        return new OurLittleBossHero(50,50,heroSize, mWidth/2, 64/gameTickSpeed);
    }

    protected void updateCurrentRockPositionTick() {
        if(hero.updateCurrentPositionAndPopOverlapsAndPlayNotes(balloons)) {

            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    AudioUtils.playPoppingSound(mContext, audioManager);
                    return null;
                }
            }.execute();


            if(((OurLittleBossHero)hero).health < 1) {
                gameOver = true;
            }

           };
    }

}