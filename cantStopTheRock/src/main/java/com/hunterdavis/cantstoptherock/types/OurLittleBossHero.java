package com.hunterdavis.cantstoptherock.types;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.hunterdavis.cantstoptherock.R;
import com.hunterdavis.gameutils.rendering.renderMath;

/**
 * Created by hunter on 11/30/14.
 */
public class OurLittleBossHero extends OurLittleHero{

    public static final int DEFAULT_HEALTH_PER_BALLOON = 2;
    public static final int TOTAL_HEALTH = 200;

    int xPosNotToGoPast = 200;
    public int health = TOTAL_HEALTH;
    int healthPerBalloon = DEFAULT_HEALTH_PER_BALLOON;

    Bitmap ourBmp;

    Rect src;
    Rect dest;

    public OurLittleBossHero(Context context, int x, int y, int siz, int dontGoPastThisXPos, int healthPerBalloonHit) {
        super(x, y, siz);
        xPosNotToGoPast = dontGoPastThisXPos;
        healthPerBalloon = healthPerBalloonHit;

        // pre-calculate and cache our graphics
        int bossSize = 100;
        ourBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.dragon),bossSize,bossSize,true);
        dest  = new Rect(xLocation-bossSize/2,yLocation-bossSize/2,xLocation+bossSize/2, yLocation+bossSize/2);
    }


    @Override
    public void moveHero(Balloon[] balloons) {
        super.moveHero(balloons);

        if(xLocation > xPosNotToGoPast) {
            xLocation -= 2 * heroMoveSpeed;
        }
    }


    @Override
    public boolean tryToPopABalloon(Balloon[] balloons) {
        boolean poppedOne = super.tryToPopABalloon(balloons);
        if(poppedOne) {
            health-=healthPerBalloon;
        }
        return poppedOne;
    }


    @Override
    public void drawHero(Canvas canvas, Paint paint) {

        // draw our boss hero
        canvas.drawBitmap(ourBmp,null,dest,null);
    }

    // override this to actually get the index of the FURTHEST balloon!
    // this causes the boss to run away from the closest bullet
    @Override
    public int getIndexOfClosestBalloon(Balloon[] balloons) {
        int index = 0;
        float distance;
        for(Balloon b : balloons) {
            if(b.isBalloonActive()) {
                distance = renderMath.fdistance(b.xLocation, b.yLocation, xLocation, yLocation);

                if (closestDistance < distance) {
                    closestDistance = distance;
                    closestIndex = index;
                }
            }
            index++;
        }

        return closestIndex;
    }

}
