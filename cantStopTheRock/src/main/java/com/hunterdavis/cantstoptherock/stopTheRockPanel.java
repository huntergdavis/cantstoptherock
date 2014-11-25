package com.hunterdavis.cantstoptherock;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.hunterdavis.easyaudiomanager.EasyAudioManager;
import com.hunterdavis.gameutils.credits.CreditsScreen;
import com.hunterdavis.gameutils.rendering.GameSurfaceView;
import com.hunterdavis.gameutils.time.GameClockCountDownTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hunter on 11/25/14.
 */
public class stopTheRockPanel extends GameSurfaceView implements SurfaceHolder.Callback {

    /** The surface created. */
    public Boolean surfaceCreated;

    /** The m context. */
    public Context mContext;

    /** The m width. */
    private int mWidth = 0;

    /** The m height. */
    private int mHeight = 0;

    /** the balloon tick value */
    private int balloonTickValue = 1;

    /** The game over. */
    private boolean gameOver = false;

    /** The first run. */
    private boolean firstRun = true;

    /** The game started. */
    private boolean gameStarted = false;

    /** are we updating positions still? (slow cpu) */
    private AtomicBoolean updatingPositions;

    /** The baloons. */
    private ArrayList<Balloon> balloons;

    /** Our Random */
    Random random = new Random();

    /** The paint. */
    private Paint paint = null;

    /** The audio manager. */
    private EasyAudioManager audioManager;


    public stopTheRockPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        surfaceCreated = false;

        updatingPositions = new AtomicBoolean(false);

        balloons = new ArrayList<Balloon>();

        getHolder().addCallback(this);
        setFocusable(true);
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
                            R.raw.popxcolorballoonscreditstheme,
                            R.raw.popxcolorballoonscredits,
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
            if(b.isPointWithinBaloon(event.getX(),event.getY())) {
              return;
            }
        }

        balloons.add(new Balloon((int)event.getX(),(int)event.getY(), Color.RED,6 + random.nextInt(20)));
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

    private void updateCurrentRockPositionTick() {

    }

    private void updateBalloonPositions() {
        if (firstRun == true) {
            return;
        }

        moveAndAgeAndCheckForDeadBalloons();
    }

    private synchronized void moveAndAgeAndCheckForDeadBalloons() {

        for(Balloon b : balloons) {
            if(!b.popped) {
                // only remove balloon once off screen so there's no pop-out
                if (b.xLocation + 20 > 0) {
                    b.age++;
                    b.updateXandYLoc(b.xLocation - balloonTickValue, b.yLocation);
                } else {
                    b.pop();
                }
            }
        }

        // our balloon garbage collector
        ArrayList<Balloon> aliveBalloons = new ArrayList<Balloon>();
        for(Balloon b : balloons) {
            if(!b.popped) {
                aliveBalloons.add(b);
            }
        }
        balloons = aliveBalloons;
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

        paint.setColor(Color.WHITE);
        // clear the screen with the black painter.
        canvas.drawRect(0, 0, mWidth, mHeight, paint);

        // draw game over if game over
        if ((!gameOver) && (firstRun == false) && gameStarted) {
            drawBaloons(canvas, paint);
        }

        if (gameStarted == false) {
            paint.setColor(Color.MAGENTA);
            paint.setTextSize(30);
            canvas.drawText("Tap To Drop Balloons", (mWidth / 2), mHeight / 4, paint);
        }


        if (gameOver == true) {
            paint.setTextSize(60);
            paint.setColor(Color.BLUE);
            canvas.drawText("YOU WIN!", (mWidth / 2), mHeight / 4, paint);
        }

        canvas.drawText("In Play: " + balloons.size(), (mWidth - (mWidth / 10)),
                mHeight / 30, paint);


    }

    /**
     * Draw baloons.
     *
     * @param canvas
     *            the canvas
     * @param paint
     *            the paint
     */
    public void drawBaloons(Canvas canvas, Paint paint) {
        for (Balloon b : balloons) {
            b.drawBaloon(canvas,paint);
        }
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
