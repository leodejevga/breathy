package com.apps.philipps.source.implementations;

import android.content.Context;

import com.apps.philipps.source.Coins;
import com.apps.philipps.source.OptionManager;
import com.apps.philipps.source.SaveData;

import java.util.ArrayList;

/**
 * Created by qwert on 03/05/2017.
 */

public abstract class AbstractBackendOptions {
    protected static String gameName = "";
    private static String key = gameName + "Options";
    /**
     * The GameOptions.
     */
    public static OptionManager<String, Boolean> options;

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

    protected static void loadGameOptions(Context context){
        SaveData<ArrayList> saveOptions =  new SaveData<>(context);
        ArrayList gameOptions = saveOptions.readObject(key);
        if (gameOptions != null)
            options.setOptions(gameOptions);
    }

    public static void saveGameOptions(Context context){
        SaveData<ArrayList> saveOptions =  new SaveData<>(context);
        saveOptions.writeObject(key , options.getOptions());
    }
}
