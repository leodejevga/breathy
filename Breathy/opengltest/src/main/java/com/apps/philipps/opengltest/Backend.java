package com.apps.philipps.opengltest;

import android.content.Context;

import com.apps.philipps.source.OptionManager;
import com.apps.philipps.source.cachemanager.CacheManager;

import java.util.ArrayList;

public class Backend {
    private static boolean init = false;
    private static int default_music_resource_id = R.raw.ascartheft;
    public static String gName;
    public static OptionManager<String, Boolean> options;
    public static float rotateSpeed = 360f;
    public static int score;
    public static int minusScore;
    public static CacheManager cacheManager;


    /**
     * The constant highscore.
     */
    public static int highscore = 0;
    public static ArrayList<Integer> highscores;

    /**
     * Reinitialize <code>Backend</code> of the game.
     */
    public static void reinit(Context context, String name) {
        init = false;
        init(context, name);
    }

    /**
     * Initialize <code>Backend</code> of the game.
     */
    public static boolean init(Context context, String name) {
        gName = name;
        score = 0;
        cacheManager = new CacheManager(context);
        if (!init) {
            options = new OptionManager<>();
            options.add("Car", false, 20);
            options.add("Van", false, 40);
            options.add("Bus", false, 30);
            init = true;
            loadGameOptions();
            loadHighScores();
            highscore = highscores.get(0);
            return true;
        }
        return false;
    }

    public static int getDefault_music_resource_id() {
        return default_music_resource_id;
    }

    public static void setDefault_music_resource_id(int default_music_resource_id) {
        Backend.default_music_resource_id = default_music_resource_id;
    }

    protected static void loadGameOptions() {
        ArrayList gameOptions = cacheManager.loadGameOptions(gName);
        if (gameOptions.size() != 0)
            options.setOptions(gameOptions);
    }

    public static void saveGameOptions() {
        cacheManager.saveGameOptions(gName, options.getOptions());
    }

    public static void saveHighScore(String gameName, int score) {
        cacheManager.saveHighScore(gameName, score);
    }

    public static void loadHighScores() {
        highscores = cacheManager.loadHighScore(gName);
    }
}
