package com.apps.philipps.test;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;


/**
 * Created by Jevgenij Huebert on 05.09.2017. Project Breathy.
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
