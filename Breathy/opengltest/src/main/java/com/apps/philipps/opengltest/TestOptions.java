package com.apps.philipps.opengltest;

import android.content.Context;

import com.apps.philipps.source.implementations.BreathyGameComponent;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class TestOptions extends BreathyGameComponent {

    public TestOptions(Context context){
        this.context = context;
    }

    /**
     * Start Activity. Provide here your states with the <code>AppState</code> Class
     *
     * @return true if the Activity started
     */
    @Override
    public boolean start() {
        return false;
    }
}

