package com.apps.philipps.test;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Leo on 01.10.2017.
 */

public class SoundManager {
    private static MediaPlayer shoot;
    private static boolean initialized;

    public static void init(Context context) {
        shoot = MediaPlayer.create(context, R.raw.shoot);
        shoot.setLooping(false);
        initialized = true;
    }

    public static void shoot() {
        if (initialized) {
            if (shoot.isPlaying())
                shoot.seekTo(0);
            else
                shoot.start();
        }
    }
}
