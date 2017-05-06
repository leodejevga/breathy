package com.apps.philipps.source;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by qwert on 03/05/2017.
 */

public abstract class AbstractBackendOptions {
    public static String gameName = "";
    private static String key = gameName + "Options";
    private static Context context;
    /**
     * The GameOptions.
     */
    public static OptionManager<String, Boolean> options;

    static SaveData<ArrayList> saveOptions;
    static SaveData<Integer> saveCredit;
    /**
     * The constant highscore.
     */
    public static int highscore;

    /**
     * Reinitialize <code>Backend</code> of the game.
     */
    public static void reinit() throws NotImplementedException {
        throw new NotImplementedException("method not overridden");
    }
    /**
     * Initialize <code>Backend</code> of the game.
     */
    public static boolean init(String gameName) throws NotImplementedException {
        throw new NotImplementedException("method not overridden");
    }

    public static class NotImplementedException extends Throwable {
        public NotImplementedException(String s) {
        }
    }

    public static void loadGameOptions(){
        saveOptions =  new SaveData<>(context);
        ArrayList gameOptions = saveOptions.readObject(key);
        if (gameOptions != null)
            options.setOptions(gameOptions);
    }

    public static void saveGameOptions(){
        saveOptions =  new SaveData<>(context);
        saveOptions.writeObject(key , options.getOptions());
    }

    public static void saveCredit(){
        saveCredit =  new SaveData<>(context);
        saveCredit.writeObject("credit" , Coins.getAmount());
    }

    public static void setContext(Context context) {
        AbstractBackendOptions.context = context;
    }
}
