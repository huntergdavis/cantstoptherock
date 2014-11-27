package com.hunterdavis.cantstoptherock;

import android.content.Context;
import android.media.MediaPlayer;

import com.hunterdavis.easyaudiomanager.EasyAudioManager;


public class AudioUtils {

    /** The Sound Pool */
    protected static final int[] soundPool = {R.raw.balloonpop, R.raw.a, R.raw.b, R.raw.c, R.raw.d, R.raw.e, R.raw.f, R.raw.g,
                                            R.raw.easy,R.raw.medium,R.raw.hard};

    public static enum SOUND_POOL_VALUES {
        BALLOONPOP,
        a,
        b,
        c,
        d,
        e,
        f,
        g,
        easy,
        medium,
        hard
    };

    public static SOUND_POOL_VALUES getSoundPoolValueForBalloon(Balloon b, int screenHeight) {
        // 1-7 value for notes based on position in screen
        return SOUND_POOL_VALUES.values()[((int) Math.floor(b.yLocation / (screenHeight / 7))) + 1];
    }

    /**
     * Play popping sound.
     *
     * @param context
     *            the context
     */
    public static void playPoppingSound(Context context, EasyAudioManager audioMan) {
        playSoundFromSoundPool(context, audioMan,SOUND_POOL_VALUES.BALLOONPOP);
    }

    public static void playSoundFromSoundPool(Context context, EasyAudioManager audioMan, SOUND_POOL_VALUES value) {
        //audioMan.playSound(value.ordinal(), context);

        MediaPlayer.create(context,soundPool[value.ordinal()]).start();
    }

    public static void playSoundForBalloonPop(Context context, EasyAudioManager audioMan, Balloon b, int screenheight) {
        playSoundFromSoundPool(context,audioMan, getSoundPoolValueForBalloon(b, screenheight));


    }


}
