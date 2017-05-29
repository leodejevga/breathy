package com.apps.philipps.audiosurf;


import android.content.Context;

import com.apps.philipps.source.implementations.AbstractBackendOptions;
import com.apps.philipps.source.OptionManager;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */

//public class Backend {
//    /**
//     * The OptionManager.
//     */
//    public static OptionManager<String, Boolean> options;
//    /**
//     * The constant highscore.
//     */
//    public static int highscore;
public class Backend extends AbstractBackendOptions {
    private static boolean init = false;
    private static int defaut_music_resource_id = R.raw.asmario;


    /**
     * Reinitialize <code>Backend</code> of the game.
     */
    public static void reinit(Context context) {
        init = false;
        init(context);
    }

    /**
     * Initialize <code>Backend</code> of the game.
     */
    public static boolean init(Context context) {
        if (!init) {
            //TODO: Die Werte (gekauft, nicht gekauft) sollten mit SaveData ausgelesen werden

            options = new OptionManager<>();
            options.add("First Skin", false, 20);
            options.add("Second Skin", false, 40);
            options.add("Third Skin", false, 30);
            options.add("Fourth Skin", false, 70);
            init = true;
            loadGameOptions(context);
            return true;
        }
        return false;
    }

    public static int getDefaut_music_resource_id() {
        return defaut_music_resource_id;
    }

    public static void setDefaut_music_resource_id(int defaut_music_resource_id) {
        Backend.defaut_music_resource_id = defaut_music_resource_id;
    }
}
