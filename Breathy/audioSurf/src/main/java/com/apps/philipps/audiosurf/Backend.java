package com.apps.philipps.audiosurf;


import android.content.Context;

import com.apps.philipps.source.OptionManager;
import com.apps.philipps.source.SaveData;

import java.util.ArrayList;

public class Backend {
    private static boolean init = false;
    private static int default_music_resource_id = R.raw.asmario;
    public static String gName;
    public static OptionManager<String, Boolean> options;
    private static String key;
    /**
     * The constant highscore.
     */
    public static int highscore;


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
        if (!init) {
            //TODO: Die Werte (gekauft, nicht gekauft) sollten mit SaveData ausgelesen werden
            options = new OptionManager<>();
            options.add("First Skin", false, 20);
            options.add("Second Skin", false, 40);
            options.add("Third Skin", false, 30);
            options.add("Fourth Skin", false, 70);
            init = true;
            loadGameOptions(context, gName);
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
}
