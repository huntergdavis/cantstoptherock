package com.hunterdavis.cantstoptherock.gametypes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.hunterdavis.cantstoptherock.util.AudioUtils;
import com.hunterdavis.cantstoptherock.types.Balloon;
import com.hunterdavis.cantstoptherock.types.OurLittleHero;
import com.hunterdavis.cantstoptherock.R;
import com.hunterdavis.easyaudiomanager.EasyAudioManager;
import com.hunterdavis.gameutils.credits.CreditsScreen;
import com.hunterdavis.gameutils.rendering.GameSurfaceView;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hunter on 11/27/14.
 */
public class BaseStopTheRockPanel extends GameSurfaceView implements SurfaceHolder.Callback  {

    /** The surface created. */
    public Boolean surfaceCreated;

    /** The m context. */
    public Context mContext;

    /** The m width. */
    private int mWidth = 0;

    /** The m height. */
    int mHeight = 0;

    /** the balloon tick value */
    private int gameTickSpeed = 1;

    /** The game over. */
    private boolean gameOver = false;

    /** The first run. */
    private boolean firstRun = true;

    /** The game started. */
    private boolean gameStarted = false;

    /** are we updating positions still? (slow cpu) */
    private AtomicBoolean updatingPositions;

    /** The baloons. */
    Balloon[] balloons;

    /** The Hero */
    OurLittleHero hero;

    /** The Hero Size */
    private int heroSize = 20;

    /** The Number of Balloons */
    private int numberOfBalloons = 200;

    /** Our Soothing Background Color */
    private int backgroundColorIndex = 0;
    private int currentBackgroundColor = Color.rgb(194, 225, 229);

    private int[] soothingColors = {
            Color.rgb(194,225,229),
            Color.rgb(169,204,101),
            Color.rgb(239,208,204),
            Color.rgb(180,183,176),
            Color.rgb(216,200,175),
    };

    /** Our Random */
    Random random = new Random();

    /** The paint. */
    private Paint paint = null;

    /** The audio manager. */
    EasyAudioManager audioManager;

    public BaseStopTheRockPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        surfaceCreated = false;

        updatingPositions = new AtomicBoolean(false);

        balloons = new Balloon[numberOfBalloons];

        // init empty balloons
        for(int i = 0; i < numberOfBalloons; i++) {
            balloons[i] = new Balloon(0,0,0,0);
        }

        hero = new OurLittleHero(50,50,heroSize);

        getHolder().addCallback(this);
        setFocusable(true);
    }


    public void updateGameSpeed(int gameSpeed) {
        gameTickSpeed = gameSpeed;
        hero.updateMoveSpeed(gameSpeed + 1);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {

            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                if (gameStarted == false) {
                    gameStarted = true;
                    //         gameClockTimer.start();
                } else if (!gameOver) {
                    dropABaloon(event);
                } else if (gameOver) {
                    CreditsScreen.startCreditScreen(getContext(),
                            R.raw.credits,
                            R.raw.cantstopcredits,
                            R.drawable.hunterredbaloon, "You Stopped The Rock!");

                    winAndFinishActivity();
                }
                return true;
            } else if (action == MotionEvent.ACTION_MOVE) {
                return true;
            } else if (action == MotionEvent.ACTION_UP) {
                return true;
            }
            return true;
        }
    }

    private void dropABaloon(MotionEvent event) {

        for(Balloon b : balloons) {
            if(b.isBalloonActive()) {
                if (b.isPointWithinBaloon(event.getX(), event.getY())) {
                    return;
                }
            }
        }

        for(Balloon b : balloons) {
            if(!b.isBalloonActive()) {
                b.xLocation = (int)event.getX();
                b.yLocation = (int)event.getY();
                b.color = getColorBasedOnGameSpeed();
                b.size = 15 + random.nextInt(20);
                b.popped = false;

                return;
            }
        }

    }

    private int getColorBasedOnGameSpeed() {
        if(gameTickSpeed < 10) {
            return Color.BLUE;
        }else if (gameTickSpeed < 20) {
            return Color.CYAN;
        }else if (gameTickSpeed < 40) {
            return Color.RED;
        }
        return Color.DKGRAY;
    }

    private void winAndFinishActivity() {
        ((Activity) mContext).finish();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //
        if (surfaceCreated == false) {
            createThread(surfaceHolder);

            surfaceCreated = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        surfaceCreated = false;

    }

    @Override
    public void updateGameState() {

        if (gameOver == true) {
            return;
        }

        // if first game state, init
        if ((mWidth > 0) && firstRun) {
            initGameState();
        }

        if (gameStarted && updatingPositions.weakCompareAndSet(false, true)) {
            // update current rock a tick
            updateCurrentRockPositionTick();
            updateBalloonPositions();
            updatingPositions.set(false);
        }
    }

    protected void updateCurrentRockPositionTick() {
        if(hero.updateCurrentPositionAndPopOverlapsAndPlayNotes(balloons)) {
            //audioManager.pauseSong();

            new AsyncTask<Void, Void, Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    AudioUtils.playPoppingSound(mContext, audioManager);
                    //AudioUtils.playSoundForBalloonPop(mContext, audioManager, new Balloon(hero.xLocation, hero.yLocation, 0, 0), mHeight);
                    return null;
                }
            }.execute();


            //AudioUtils.playSoundForBalloonPop(mContext,audioManager,);
            //AudioUtils.playSoundForBalloonPop(mContext,audioManager,new Balloon(hero.xLocation,hero.yLocation,0,0),mHeight);
        };
    }

    private synchronized void updateBalloonPositions() {
        if (firstRun == true) {
            return;
        }

        for(Balloon b : balloons) {
            if(b.isBalloonActive()) {
                // only remove balloon once off screen so there's no pop-out
                if (b.xLocation + 20 > 0) {
                    b.age++;
                    b.updateXandYLoc(b.xLocation - gameTickSpeed, b.yLocation);
                } else {
                    b.pop();
                }
            }
        }
    }

    private void initGameState() {
        firstRun = false;
    }

    @Override
    public void onDraw(Canvas canvas) {

        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();


        if (paint == null) {
            paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
        }

        paint.setColor(getSoothingBackgroundColor());
        // clear the screen with the background painter.
        canvas.drawRect(0, 0, mWidth, mHeight, paint);

        // draw the balloons and hero
        if ((!gameOver) && (firstRun == false) && gameStarted) {
            drawHero(canvas,paint);
            drawBalloons(canvas, paint);
        }

        if (gameStarted == false) {
            paint.setColor(Color.MAGENTA);
            paint.setTextSize(30);
            canvas.drawText("Tap To Drop Balloons", (mWidth / 2), mHeight / 4, paint);
        }

        // draw game over if game over
        if (gameOver == true) {
            paint.setTextSize(60);
            paint.setColor(Color.BLUE);
            canvas.drawText("YOU WIN!", (mWidth / 2), mHeight / 4, paint);
        }

        canvas.drawText("In Play: " + getNumberOfBalloonsInPlay(), (mWidth - (mWidth / 10)),
                mHeight / 30, paint);


    }

    /*
    * 1. find optimum color at current index
    * 2. step 1 towards that color from current color
    * 3. if color == optimum, update index
    * 4. return color
     */
    private int getSoothingBackgroundColor() {
        // find optimum color at current index
        int optimumColor = soothingColors[backgroundColorIndex];
        int optimumRed = Color.red(optimumColor);
        int optimumBlue = Color.blue(optimumColor);
        int optimumGreen = Color.green(optimumColor);

        int currentRed = Color.red(currentBackgroundColor);
        int currentBlue = Color.blue(currentBackgroundColor);
        int currentGreen = Color.green(currentBackgroundColor);


        // update current background color
        int newRed = currentRed;
        int newBlue = currentBlue;
        int newGreen = currentGreen;

        // only make 1 step each iteration.  Either red, blue, or green
        newRed = stepTowardsValue(currentRed,optimumRed);
        if(newRed == currentRed) {
            newBlue = stepTowardsValue(currentBlue, optimumBlue);
            if(newBlue == currentBlue) {
                newGreen = stepTowardsValue(currentGreen, optimumGreen);
            }

        }

        currentBackgroundColor = Color.rgb(newRed,newGreen,newBlue);

        // update index
        if(optimumColor == currentBackgroundColor) {
            backgroundColorIndex = (backgroundColorIndex + 1) % (soothingColors.length - 1);
        }

        return currentBackgroundColor;
    }

    private int stepTowardsValue(int stepper, int targetVal) {
        int retVal = stepper;

        if(stepper < targetVal) {
            retVal++;
        }else if (stepper > targetVal) {
            retVal--;
        }

        return retVal;
    }

    private int getNumberOfBalloonsInPlay() {
        int numballoons = 0;
        for(Balloon b : balloons) {
            if(b.isBalloonActive()) {
                numballoons++;
            }
        }

        return numballoons;
    }

    /**
     * Draw baloons.
     *
     * @param canvas
     *            the canvas
     * @param paint
     *            the paint
     */
    public void drawBalloons(Canvas canvas, Paint paint) {
        for (Balloon b : balloons) {
            if(b.isBalloonActive()) {
                b.drawBaloon(canvas, paint);
            }
        }
    }

    public void drawHero(Canvas canvas, Paint paint) {
        hero.drawHero(canvas,paint);
    }


    // setAudioManager is a setter method for the audio manager
    /**
     * Sets the audio manager.
     *
     * @param audioM
     *            the new audio manager
     */
    public void setAudioManager(EasyAudioManager audioM) {
        audioManager = audioM;
    }

}
