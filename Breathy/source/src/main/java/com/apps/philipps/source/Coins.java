package com.apps.philipps.source;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */
public class Coins {
    private static int amount = 2600;
    private static boolean initialized = false;

    /**
     * Buy any Object must call this method to change amount of Coins
     *
     * @param coins the privce of the Object to buy
     * @return true for object is bought. Amount of coins is subptracted
     */
    public static boolean buy(int coins) {
        if (Coins.amount >= coins)
            amount -= coins;
        else return false;
        return true;
    }

    /**
     * Init.
     */
    public static void init( int coins) {
        if (!initialized) {
           amount = coins;
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
