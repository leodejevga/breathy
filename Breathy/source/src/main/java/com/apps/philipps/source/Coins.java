package com.apps.philipps.source;

/**
 * Created by leode on 28.01.2017.
 */

public class Coins {
    private static int amount=100;

    public static boolean buy(int coins){
        if(Coins.amount >= coins)
            amount-=coins;
        else return false;
        return true;
    }
    public static int getAmount(){
        return amount;
    }
    public void addCoins(int coins){
        Coins.amount+=coins;
    }
}
