package com.hunterdavis.cantstoptherock.gametypes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;

import com.hunterdavis.cantstoptherock.types.Balloon;
import com.hunterdavis.cantstoptherock.types.OurLittleHero;
import com.hunterdavis.cantstoptherock.types.OurLittleLineSweepingHero;
import com.hunterdavis.cantstoptherock.util.AudioUtils;

/**
 * Created by hunter on 11/27/14.
 */
public class MusicalNotesRockPanel extends BaseStopTheRockPanel{

    public MusicalNotesRockPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public OurLittleHero getHero() {
        return new OurLittleLineSweepingHero(50,50,heroSize,mHeight, mWidth);
    }

    @Override
    public void drawBalloons(Canvas canvas, Paint paint) {
        for (Balloon b : balloons) {
            if(b.isBalloonActive()) {
                b.drawBalloonWithText(canvas,paint,
                        AudioUtils.getSoundPoolValueForBalloon(b, mHeight).name());
            }
        }
    }

    @Override
    protected void updateCurrentRockPositionTick() {
        if(hero.updateCurrentPositionAndPopOverlapsAndPlayNotes(balloons)) {
            //audioManager.pauseSong();

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
