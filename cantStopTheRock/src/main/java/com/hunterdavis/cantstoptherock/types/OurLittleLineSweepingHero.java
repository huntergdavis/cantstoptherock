package com.hunterdavis.cantstoptherock.types;

import android.graphics.RectF;

import com.hunterdavis.gameutils.rendering.renderMath;

/**
 * Created by hunter on 11/28/14.
 */
public class OurLittleLineSweepingHero extends OurLittleHero {

    private int height = size;
    private int canvasWidth = size;

    public OurLittleLineSweepingHero(int x, int y, int width, int ySiz, int canvasWideness) {
        super(x, y, width);
        height = ySiz;
        canvasWidth = canvasWideness;
    }

    @Override
    public void recalculateDrawableRect() {
        drawableRect = new RectF(xLocation - size/2, height, xLocation + size/2, 0);
    }

    @Override
    public void moveHero(Balloon[] balloons) {
        xLocation = (xLocation + heroMoveSpeed) % canvasWidth;
    }

    @Override
    public boolean tryToPopABalloon(Balloon[] balloons) {
        for(Balloon b : balloons) {
            if(b.isBalloonActive()) {

                if(renderMath.fdistance(b.xLocation,0,xLocation,0) < (b.size + size + heroMoveSpeed)) {
                    b.pop();
                    return true;
                }
            }
        }
        return false;
    }
}
