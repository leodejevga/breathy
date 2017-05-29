package com.apps.philipps.source.cachemanager;

import android.content.Context;

import com.apps.philipps.source.Coins;
import com.apps.philipps.source.SaveData;


public class CacheManager {
    private final int START_CREDIT = 2600;
    private SaveData<Boolean> saveGame;
    private SaveData<Integer> saveCredit;
    private SaveData<UserData> saveUserdata;

    public CacheManager(Context context) {
        saveGame = new SaveData<>(context);
        saveCredit = new SaveData<>(context);
        saveUserdata = new SaveData<>(context);
    }

    public boolean isIGameBought(String gameName) {
        if (saveGame.readObject(gameName) == null) {
            return false;
        }
        return saveGame.readObject(gameName);
    }

    public void saveIGameStatusToCache(String gameName, boolean isBought) {
        saveGame.writeObject(gameName, isBought);
    }

    public void saveCreditToCache() {
        saveCredit.writeObject("credit", Coins.getAmount());
    }

    public void saveUserdata(UserData userData){
        saveUserdata.writeObject("userData", userData);
    }
    public UserData readUserdata(){
        return saveUserdata.readObject("userData");
    }
}
