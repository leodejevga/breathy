package com.apps.philipps.source.cachemanager;

import android.content.Context;

import com.apps.philipps.source.Coins;
import com.apps.philipps.source.SaveData;

import java.util.ArrayList;
import java.util.Collections;


public class CacheManager {
    private final int START_CREDIT = 2600;
    private SaveData<Boolean> saveGame;
    private SaveData<Integer> saveCredit;
    private SaveData<UserData> saveUserdata;
    private SaveData<ArrayList> saveOptions;
    private SaveData<ArrayList> saveHighscores;

    public CacheManager(Context context) {
        saveGame = new SaveData<>(context);
        saveCredit = new SaveData<>(context);
        saveUserdata = new SaveData<>(context);
        saveOptions = new SaveData<>(context);
        saveHighscores = new SaveData<>(context);
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

    public void saveUserdata(UserData userData) {
        saveUserdata.writeObject("userData", userData);
    }

    public UserData readUserdata() {
        return saveUserdata.readObject("userData");
    }

    public void saveGameOptions(String gameName, ArrayList options) {
        String keyword = gameName.replaceAll("[ ]*", "");
        keyword = keyword + "Options";
        saveOptions.writeObject(keyword, options);
    }

    public ArrayList loadGameOptions(String gameName) {
        String keyword = gameName.replaceAll("[ ]*", "");
        keyword = keyword + "Options";
        ArrayList gameOptions = saveOptions.readObject(keyword);
        if (gameOptions != null)
            return gameOptions;
        else return new ArrayList();
    }

    public ArrayList loadHighScore(String gameName) {
        String keyword = gameName.replaceAll("[ ]*", "");
        keyword = keyword + "Highscore";
        ArrayList hs = saveHighscores.readObject(keyword);
        if (hs != null)
            return hs;
        else {
            ArrayList list = new ArrayList();
            list.add(0);
            return list;
        }
    }

    public void saveHighScore(String gameName, int score) {
        String keyword = gameName.replaceAll("[ ]*", "");
        keyword = keyword + "Highscore";
        ArrayList highscores = loadHighScore(gameName);
        highscores.add(score);
        Collections.sort(highscores);
        Collections.reverse(highscores);
        while (highscores.size() > 5)
            highscores.remove(highscores.size() - 1);
        saveHighscores.writeObject(keyword, highscores);
    }

}
