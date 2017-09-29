package com.breathy.racing;

import android.view.View;
import android.view.ViewGroup;

/**
 * This class provides some useful methods for your game
 */

public class GameUtil {

    /**
     * returns a random double out of an triangular Distribution
     * @param a min
     * @param b max
     * @param c mean
     * @return random Double
     */
    public static double triangularDistribution(double a, double b, double c) {
        double F = (c - a) / (b - a);
        double rand = Math.random();
        if (rand < F) {
            return a + Math.sqrt(rand * (b - a) * (c - a));
        } else {
            return b - Math.sqrt((1 - rand) * (b - a) * (b - c));
        }
    }

    public static int getRandomNumber(int Min, int Max){

        return Min + (int)(Math.random() * ((Max - Min) + 1));
    }

    public static void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    public static int getRandomCar(){
        int random = getRandomNumber( 0, 5 );
        switch (random){
            case 0: return R.drawable.slow_car;
            case 1: return R.drawable.slow_car_02;
            case 2: return R.drawable.slow_car_03;
            case 3: return R.drawable.slow_car_04;
            case 4: return R.drawable.slow_car_05;
        }
        return R.drawable.slow_car;
    }

}
