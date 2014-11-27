package com.hunterdavis.cantstoptherock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.hunterdavis.easyaudiomanager.EasyAudioManager;
import com.hunterdavis.gameutils.rendering.renderMath;


/**
 * Created by hunter on 11/25/14.
 */
public class OurLittleHero {

    /** The x location. */
    int xLocation;

    /** The y location. */
    int yLocation;

    /** The size. */
    int size;

    /** The age. */
    int age;

    /** The Color */
    int color = Color.GREEN;

    /** The drawable rect. */
    RectF drawableRect;

    /** The Hero Move Speed */
    int heroMoveSpeed = 2;

    //distance calculations
    float closestDistance;
    int closestIndex;

    // our little hero is a cool little guy
    public OurLittleHero(int x, int y, int siz) {
        xLocation = x;
        yLocation = y;
        size = siz;
        age = 0;
        recalculateDrawableRect();
    }

    public void updateMoveSpeed(int moveSpeed) {
        heroMoveSpeed = moveSpeed;
    }

    private void recalculateDrawableRect() {
        drawableRect = new RectF(xLocation - size, yLocation + size, xLocation + size, yLocation
                - size);
    }

    public void drawHero(Canvas canvas, Paint paint) {
        // first paint the black background
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawOval(drawableRect, paint);
        paint.setColor(color);
        canvas.drawOval(new RectF(drawableRect.left + 1,drawableRect.top - 1,
                drawableRect.right - 1,drawableRect.bottom + 1), paint);
    }

    /*
    * returns true if any balloons were popped
     */
    public boolean updateCurrentPositionAndPopOverlapsAndPlayNotes(Balloon[] balloons) {

        boolean poppedAny = false;

        boolean foundActive = false;
        for(Balloon b : balloons) {
            if(b.isBalloonActive()) {
                foundActive = true;
                break;
            }
        }

        if(!foundActive) {
            return poppedAny;
        }

        // move closer to the closest balloon
        closestDistance = 100000;
        closestIndex = 0;

        int index = 0;
        for(Balloon b : balloons) {
            if(b.isBalloonActive()) {
                float distance = renderMath.fdistance(b.xLocation, b.yLocation, xLocation, yLocation);

                // if we're overlapping a balloon, pop that sucker
                if(distance < (size + b.size + heroMoveSpeed - 1)) {
                    poppedAny = true;
                    b.pop();
                }

                if (closestDistance > distance) {
                    closestDistance = distance;
                    closestIndex = index;
                }
            }
            index++;
        }

        Balloon winningBalloon = balloons[closestIndex];

        if(winningBalloon.xLocation > xLocation) {
            xLocation = xLocation + heroMoveSpeed;
        }else if(winningBalloon.xLocation < xLocation) {
            xLocation = xLocation - heroMoveSpeed;
        }
        if(winningBalloon.yLocation > yLocation) {
            yLocation = yLocation + heroMoveSpeed;
        }else if (winningBalloon.yLocation < yLocation) {
            yLocation = yLocation - heroMoveSpeed;
        }

        recalculateDrawableRect();
        return poppedAny;
    }
}
