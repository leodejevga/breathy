package com.apps.philipps.spaceFight;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.Random;


/**
 * Created by Jevgenij Huebert on 05.09.2017. Project Breathy.
 */

public class SoundManager {
    private static MediaPlayer shoot;
    private static MediaPlayer bang1;
    private static Random r;
    private static boolean initialized;

    public static void init(Context context) {
        shoot = MediaPlayer.create(context, R.raw.shoot);
        bang1 = MediaPlayer.create(context, R.raw.bang1);
        r = new Random();
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

    public static void bang() {
        if (initialized) {
            if (bang1.isPlaying())
                bang1.seekTo(70);
            else {
                bang1.seekTo(70);
                bang1.start();
            }
        }
    }
}
