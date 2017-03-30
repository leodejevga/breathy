package com.apps.philipps.source;

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

}
