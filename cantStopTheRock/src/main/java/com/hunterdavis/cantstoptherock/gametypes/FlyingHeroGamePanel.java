package com.hunterdavis.cantstoptherock.gametypes;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.hunterdavis.cantstoptherock.R;
import com.hunterdavis.cantstoptherock.types.FlyingHero;
import com.hunterdavis.cantstoptherock.types.OurLittleBossHero;
import com.hunterdavis.cantstoptherock.types.OurLittleHero;
import com.hunterdavis.cantstoptherock.util.Dpad;
import com.hunterdavis.gameutils.credits.CreditsScreen;

/**
 * Created by hunter on 1/23/16.
 */
public class FlyingHeroGamePanel extends BossFightGamePanel {


    Dpad mDpad = new Dpad();

    public FlyingHeroGamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public OurLittleHero getHero() {
        return new FlyingHero(mContext,150,150,heroSize, mWidth/2, 64/gameTickSpeed, mHeight);
    }

    public void moveHero(int x, int y) {
        hero.xLocation += x*10;
        hero.yLocation += y*10;
        hero.recalculateDrawableRect();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = false;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {

            if (gameStarted == false) {
                gameStarted = true;
            }

            if (event.getRepeatCount() == 0) {
                // Handle gamepad and D-pad button presses to
                // navigate the hero
                // Check if this event if from a D-pad and process accordingly.
                if (Dpad.isDpadDevice(event)) {

                    int press = mDpad.getDirectionPressed(event);
                    switch (press) {
                        case Dpad.LEFT:
                            // Do something for LEFT direction press
                            moveHero(-1,0);
                            return true;
                        case Dpad.RIGHT:
                            // Do something for RIGHT direction press
                            moveHero(1,0);
                            return true;
                        case Dpad.UP:
                            // Do something for UP direction press
                            moveHero(0,1);
                            return true;
                        case Dpad.DOWN:
                            moveHero(0,-1);
                            return true;
                    }
                }

                switch (keyCode) {
                    default:
                        if (isFireKey(keyCode)) {
                            // Update the hero object to fire something
                            handled = true;
                        }
                        break;
                }
            }
            if (handled) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private static boolean isFireKey(int keyCode) {
        // Here we treat Button_A and DPAD_CENTER as the primary action
        // keys for the game.
        return keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_BUTTON_A;
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
                            R.drawable.hunterredbaloon, getGameOverText());

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


    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        // Check that the event came from a game controller
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
                InputDevice.SOURCE_JOYSTICK &&

                ((event.getAction() == MotionEvent.ACTION_MOVE) || (event.getAction() == MotionEvent.ACTION_DOWN))) {

            // Process all historical movement samples in the batch
            final int historySize = event.getHistorySize();

            // Process the movements starting from the
            // earliest historical position in the batch
            for (int i = 0; i < historySize; i++) {
                // Process the event at historical position i
                processJoystickInput(event, i);
            }

            // Process the current movement sample in the batch (position -1)
            processJoystickInput(event, -1);
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    private static float getCenteredAxis(MotionEvent event,
                                         InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range =
                device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value =
                    historyPos < 0 ? event.getAxisValue(axis):
                            event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

    private void processJoystickInput(MotionEvent event,
                                      int historyPos) {

        InputDevice mInputDevice = event.getDevice();

        // Calculate the horizontal distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        float x = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_X, historyPos);
        if (x == 0) {
            x = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_HAT_X, historyPos);
        }
        if (x == 0) {
            x = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_Z, historyPos);
        }

        // Calculate the vertical distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat switch, or the right control stick.
        float y = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_Y, historyPos);
        if (y == 0) {
            y = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_HAT_Y, historyPos);
        }
        if (y == 0) {
            y = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_RZ, historyPos);
        }



        moveHero((int) x, (int) y);
    }

}
