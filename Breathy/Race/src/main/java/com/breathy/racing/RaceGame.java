package com.breathy.racing;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.implementations.BreathyGameComponent;
import com.breathy.racing.activities.Application;
import com.breathy.racing.activities.Game;

/**
 * Created by Jürgen on 26.03.2017.
 */

public class RaceGame extends BreathyGameComponent {

    public RaceGame(Context context) {
        this.context = context;
    }

    @Override
    public boolean start() {
       if( PlanManager.getCurrentPlan()!=null) {
            Intent i = new Intent(context, Application.class);
            context.startActivity(i);
        }
        else
            message("Bitte einen Plan auswählen!");
        return true;
    }
}