package com.esimtek.esim.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.esimtek.esim.R;

/**
 * 扫描声音播放工具类
 *
 * @author wang
 * @date 2018/12/12
 */
public class BeeperUtil {

    public static final int BEEPER = 1;
    public static final int BEEPER_SHORT = 2;
    private static final SoundPool SOUND_POOL = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);
    private static boolean mQuite = false;
    private static boolean mBeepInventoried = false;
    private static boolean mBeepPerTag = false;

    public enum BeepMode {
        QUITE,
        BEEP_INVENTORIED,
        BEEP_PER_TAG
    }

    public static void init(Context context) {
        SOUND_POOL.load(context, R.raw.beeper, BEEPER);
        SOUND_POOL.load(context, R.raw.beeper_short, BEEPER_SHORT);
    }

    public static void beep(int soundID) {
        if (mQuite) {
            return;
        }
        if (soundID == BEEPER && mBeepInventoried) {
            SOUND_POOL.play(BEEPER, 1, 1, 0, 0, 1);
            mBeepInventoried = false;
        } else if (soundID == BEEPER_SHORT && mBeepPerTag) {
            SOUND_POOL.play(BEEPER_SHORT, 1, 1, 0, 0, 1);
        } else if (soundID == BEEPER_SHORT && !mBeepPerTag) {
            mBeepInventoried = true;
        } else {

        }
    }

    /**
     * Set the beep mode.
     *
     * @param beepMode
     */
    public static void setBeepMode(BeepMode beepMode) {
        switch (beepMode) {
            case QUITE:
                mQuite = true;
                mBeepInventoried = mBeepPerTag = false;
                break;
            case BEEP_INVENTORIED:
                mBeepInventoried = mQuite = mBeepPerTag = false;
                break;
            case BEEP_PER_TAG:
                mBeepPerTag = true;
                mQuite = mBeepInventoried = false;
                break;
            default:
                mBeepInventoried = mQuite = mBeepPerTag = false;
                break;
        }

    }

    public static void release() {
        if (SOUND_POOL != null) {
            SOUND_POOL.release();
        }
    }
}
