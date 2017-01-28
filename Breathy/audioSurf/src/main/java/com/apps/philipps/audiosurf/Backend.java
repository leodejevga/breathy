package com.apps.philipps.audiosurf;

import android.content.Context;
import android.widget.Button;

import com.apps.philipps.source.Options;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leode on 28.01.2017.
 */

public class Backend {
    public static Options<AsParameter, Boolean> options;
    public static int highscore;
    public static class AsParameter{
        private String name;
        private int price;

        public int getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }

        public AsParameter(String name, int price){
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return name + " = " + price + " Coins";
        }
    }
    public static void init(){
        options = new Options();
        options.set(new AsParameter("First Skin", 20), false);
        options.set(new AsParameter("Second Skin", 40), false);
        options.set(new AsParameter("Third Skin", 30), false);
        options.set(new AsParameter("Fourth Skin", 70), false);
    }
}
