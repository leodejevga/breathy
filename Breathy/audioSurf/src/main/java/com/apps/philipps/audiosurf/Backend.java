package com.apps.philipps.audiosurf;

import android.content.Context;
import android.widget.Button;

import com.apps.philipps.source.Options;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */
public class Backend {
    /**
     * The Options.
     */
    public static Options<AsParameter, Boolean> options;
    /**
     * The constant highscore.
     */
    public static int highscore;
    private static boolean init=false;

    /**
     * The type As parameter.
     */
    public static class AsParameter{
        private String name;
        private int price;

        /**
         * Gets price.
         *
         * @return the price
         */
        public int getPrice() {
            return price;
        }

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Instantiates a new As parameter.
         *
         * @param name  the name
         * @param price the price
         */
        public AsParameter(String name, int price){
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return name + " = " + price + " Coins";
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
            options = new Options();
            options.set(new AsParameter("First Skin", 20), false);
            options.set(new AsParameter("Second Skin", 40), false);
            options.set(new AsParameter("Third Skin", 30), false);
            options.set(new AsParameter("Fourth Skin", 70), false);
            init = true;
            return true;
        }
        return false;
    }
}
