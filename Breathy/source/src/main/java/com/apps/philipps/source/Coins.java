package com.apps.philipps.source;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */
public class Coins implements Comparable<Integer> {
    private static int amount;
    private static boolean initialized = false;

    /**
     * Buy any Object must call this method to change amount of Coins
     *
     * @param coins the price of the Object to buy
     * @return true for object is bought. Amount of coins is subtracted
     */
    public static boolean buy(int coins, Context context) {
        if(initialized) {
            SaveData<Integer> credits = new SaveData<>(context);
            if(AppState.DEVELOPER)
                return true;
            if (Coins.amount >= coins) {
                amount -= coins;
                credits.writeObject("credits", amount);
                return true;
            }
        }
        return false;
    }

    /**
     * Init.
     */
    public static void init(Context context) {
        SaveData<Integer> credits = new SaveData<>(context);
        if (!initialized) {
            try {
                amount = credits.readObject("credits");
            } catch (Exception e) {
                amount = 1000;
            }
        }
        initialized = true;
    }

    /**
     * Amount of Coins the Player has
     *
     * @return Amount of coins
     */
    public static int getAmount() {
        return amount;
    }

    /**
     * Add coins.
     *
     * @param coins coins that have to be added
     * @return true if succeed false if not initialized
     */
    public static boolean addCoins(int coins) {
        if(initialized)
            Coins.amount += coins;
        else return false;
        return true;
    }

    @Override
    public int compareTo(@NonNull Integer o) {
        return Integer.compare(getAmount(), o);
    }
}
