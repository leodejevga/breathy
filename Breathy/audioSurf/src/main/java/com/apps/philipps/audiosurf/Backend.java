package com.apps.philipps.audiosurf;

import com.apps.philipps.source.GameOptions.Option;
import com.apps.philipps.source.GameOptions;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */
public class Backend {
    /**
     * The GameOptions.
     */
    public static GameOptions<AsOption, Boolean> options;
    /**
     * The constant highscore.
     */
    public static int highscore;
    private static boolean init=false;

    /**
     * The type As parameter.
     */
    public static class AsOption extends Option{

        /**
         * Instantiates a new As parameter.
         *
         * @param name  the name
         * @param price the price
         */
        public AsOption(String name, int price, Boolean check){
            super(name, check, price);
        }

        @Override
        public String toString() {
            return Parameter + " = " + Price + " Coins";
        }
    }

    /**
     * Reinitialize <code>Backend</code> of the game.
     */
    public static void reinit(){
        init = false;
        init();
    }
    /**
     * Initialize <code>Backend</code> of the game.
     */
    public static boolean init() {
        if (!init) {
            options = new GameOptions();
            options.add(new AsOption("First Skin", 20, false));
            options.add(new AsOption("Second Skin", 40, false));
            options.add(new AsOption("Third Skin", 30, false));
            options.add(new AsOption("Fourth Skin", 70, false));
            init = true;
            return true;
        }
        return false;
    }
}
