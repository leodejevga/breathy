package com.apps.philipps.test;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.implementations.BreathyGameComponent;
import com.apps.philipps.test.activities.Game;

/**
 * Created by Jevgenij Huebert on 19.03.2017. Project Breathy
 */

public class TestGame extends BreathyGameComponent {

    public TestGame(Context context){
        this.context = context;
    }

    @Override
    public boolean start() {
        if(PlanManager.getCurrentPlan()!=null) {
            Intent i = new Intent(context, Game.class);
            context.startActivity(i);
        }
        else
            message("Bitte einen Plan auswählen!");
        return true;
    }
}
