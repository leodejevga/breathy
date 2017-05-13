package com.apps.philipps.source;

import android.content.Context;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */
public class Coins {
    private static int amount;
    private static boolean initialized = false;

    /**
     * Buy any Object must call this method to change amount of Coins
     *
     * @param coins the privce of the Object to buy
     * @return true for object is bought. Amount of coins is subptracted
     */
    public static boolean buy(int coins, Context context) {
        SaveData<Integer> credits = new SaveData<>(context);
        if (Coins.amount >= coins) {
            amount -= coins;
            credits.writeObject("credits", amount);
        }
        else return false;
        return true;
    }

    /**
     * Init.
     */
    public static void init(Context context) {
        SaveData<Integer> credits = new SaveData<>(context);
        if (!initialized) {
            // TODO bugFix credits.readObject("credits")
            amount = 10000;
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
     */
    public void addCoins(int coins) {
        Coins.amount += coins;
    }
}
