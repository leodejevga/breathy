package com.apps.philipps.opengltest;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.opengltest.activities.TGame;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.implementations.BreathyGameComponent;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class TestGame extends BreathyGameComponent {

    public TestGame(Context context) {
        this.context = context;
    }

    /**
     * Start Activity. Provide here your states with the <code>AppState</code> Class
     *
     * @return true if the Activity started
     */
    @Override
    public boolean start() {
        if (PlanManager.getCurrentPlan() != null) {
            Intent i = new Intent(context, TGame.class);
            context.startActivity(i);
            return true;
        }else{
            message("Bitte ein Plan auswählen");
            return false;
        }
    }
}