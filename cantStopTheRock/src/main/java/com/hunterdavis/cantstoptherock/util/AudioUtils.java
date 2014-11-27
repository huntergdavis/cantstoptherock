package com.hunterdavis.cantstoptherock.util;

import android.content.Context;

import com.hunterdavis.cantstoptherock.R;
import com.hunterdavis.cantstoptherock.types.Balloon;
import com.hunterdavis.easyaudiomanager.EasyAudioManager;


public class AudioUtils {

    /** The Sound Pool */
    public static final int[] soundPool = {R.raw.balloonpop, R.raw.a, R.raw.b, R.raw.c, R.raw.d, R.raw.e, R.raw.f, R.raw.g};

    public static enum SOUND_POOL_VALUES {
        BALLOONPOP,
        a,
        b,
        c,
        d,
        e,
        f,
        g
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
        audioMan.playSound(value.ordinal(), context);
    }

    public static void playSoundForBalloonPop(Context context, EasyAudioManager audioMan, Balloon b, int screenheight) {
        playSoundFromSoundPool(context,audioMan, getSoundPoolValueForBalloon(b, screenheight));
    }


}
