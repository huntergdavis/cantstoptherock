package com.hunterdavis.cantstoptherock.types;

import com.hunterdavis.gameutils.rendering.renderMath;

/**
 * Created by hunter on 11/30/14.
 */
public class OurLittleBossHero extends OurLittleHero{

    public static final int DEFAULT_HEALTH_PER_BALLOON = 2;

    int xPosNotToGoPast = 200;
    public int health = 200;
    int healthPerBalloon = DEFAULT_HEALTH_PER_BALLOON;

    public OurLittleBossHero(int x, int y, int siz, int dontGoPastThisXPos, int healthPerBalloonHit) {
        super(x, y, siz);
        xPosNotToGoPast = dontGoPastThisXPos;
        healthPerBalloon = healthPerBalloonHit;
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
