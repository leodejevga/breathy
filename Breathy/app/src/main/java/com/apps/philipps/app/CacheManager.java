package com.apps.philipps.app;

import com.apps.philipps.source.Coins;
import com.apps.philipps.source.SaveData;

/**
 * Created by qwert on 27/03/2017.
 */

public class CacheManager {
    private static final int START_CREDIT = 2650;
    public static SaveData<Boolean> saveData = new SaveData<>(Backend.GLOBAL_CONTEXT);
    public static SaveData<Integer> saveCredit = new SaveData<>(Backend.GLOBAL_CONTEXT);

    public static boolean isIGameBought(String gameName) {
        if (saveData.readObject(gameName) == null) {
            return false;
        }
        return saveData.readObject(gameName);
    }

    public static void saveIGameStatusToCache(String gameName, boolean isBought) {
        saveData.writeObject(gameName, isBought);
    }

    public static void saveCreditToCache() {
        saveCredit.writeObject("credit", Coins.getAmount());
    }

    public static int getCreditFromCache() {
        if (saveCredit.readObject("credit") == null) {
            return START_CREDIT;
        }
        return (int) saveCredit.readObject("credit");
    }
}
