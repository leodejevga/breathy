package com.apps.philipps.audiosurf;

import com.apps.philipps.source.AbstractBackendOptions;
import com.apps.philipps.source.GameOptions;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */
public class Backend extends AbstractBackendOptions{
    private static boolean init=false;
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
            options = new GameOptions<>();
            options.add("First Skin", false, 20);
            options.add("Second Skin", false, 40);
            options.add("Third Skin", false, 30);
            options.add("Fourth Skin", false, 70);
            init = true;
            loadGameOptions();
            return true;
        }
        return false;
    }
}
