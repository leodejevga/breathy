package com.apps.philipps.source;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */
public class Coins {
<<<<<<< HEAD

    private static int amount;
=======
    private static int amount = 2600;
>>>>>>> 413d70a38cfdc4142708ac149f12649707934203
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
<<<<<<< HEAD
    public static void init(int amount) {
        if (!initialized) {
            Coins.amount = amount;
            //TODO: initialisirung des Kontostands
=======
    public static void init( int coins) {
        if (!initialized) {
           amount = coins;
>>>>>>> 413d70a38cfdc4142708ac149f12649707934203
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
