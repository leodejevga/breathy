package com.apps.philipps.opengltest;

import android.content.Context;

import com.apps.philipps.source.OptionManager;
import com.apps.philipps.source.SaveData;

import java.util.ArrayList;

public class Backend {
    private static boolean init = false;
    private static int default_music_resource_id = R.raw.ascartheft;
    public static String gName;
    public static OptionManager<String, Boolean> options;
    private static String key;
    public static float rotateSpeed = 360f;
    public static float limitedAngle = 20f;
    public static int life;
    public static int score;



    /**
     * The constant highscore.
     */
    public static int highscore = 0;
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
        life = 3;
        score = 0;
        if (!init) {
            options = new OptionManager<>();
            options.add("White Car", false, 20);
            options.add("Police Car", false, 40);
            options.add("Ambulance Car", false, 30);
            init = true;
            loadGameOptions(context, gName);
            loadHighScore(context, gName);
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
    protected static void loadGameOptions(Context context, String keyword){
        keyword = keyword.replaceAll("[ ]*", "");
        key = keyword + "Options";
        SaveData<ArrayList> saveOptions =  new SaveData<>(context);
        ArrayList gameOptions = saveOptions.readObject(key);
        if (gameOptions != null)
            options.setOptions(gameOptions);
    }

    public static void saveGameOptions(Context context, String keyword){
        keyword = keyword.replaceAll("[ ]*", "");
        key = keyword + "Options";
        SaveData<ArrayList> saveOptions =  new SaveData<>(context);
        saveOptions.writeObject(key , options.getOptions());
    }

    public static void saveHighScore(Context context, String keyword){
        keyword = keyword.replaceAll("[ ]*", "");
        key = keyword + "Highscore";
        SaveData<Integer> saveHighscore =  new SaveData<>(context);
        saveHighscore.writeObject(key , highscore);
    }

    public static void loadHighScore(Context context, String keyword){
        keyword = keyword.replaceAll("[ ]*", "");
        key = keyword + "Highscore";
        SaveData<Integer> loadHighscore =  new SaveData<>(context);
        Integer hs = loadHighscore.readObject(key);
        if (hs != null)
            highscore = hs;
    }
}
