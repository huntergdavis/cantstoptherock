package com.hunterdavis.cantstoptherock.types;

import android.content.Context;
import android.graphics.Rect;

/**
 * Created by hunter on 1/23/16.
 */
public class FlyingHero extends OurLittleBossHero {
    public FlyingHero(Context context, int x, int y, int siz, int dontGoPastThisXPos, int healthPerBalloonHit, int canvasHeight) {
        super(context, x, y, siz, dontGoPastThisXPos, healthPerBalloonHit, canvasHeight);
    }


    @Override
    public void moveHero(Balloon[] balloons) {
        dest  = new Rect(xLocation-bossSize/2,yLocation-bossSize/2,xLocation+bossSize/2, yLocation+bossSize/2);
    }
}
