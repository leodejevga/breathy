package com.apps.philipps.opengltest;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.opengltest.activities.Options;
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
        if(context==null)
            return false;
        Intent i = new Intent(context, Options.class);
        context.startActivity(i);
        return true;
    }
}

