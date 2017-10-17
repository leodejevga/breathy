package com.apps.philipps.test;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.implementations.BreathyGameComponent;
import com.apps.philipps.test.activities.Game;

/**
 * Created by Jevgenij Huebert on 19.03.2017. Project Breathy
 */

public class SpaceFlightStart extends BreathyGameComponent {

    public SpaceFlightStart(Context context){
        this.context = context;
    }

    @Override
    public boolean start() {
        Intent i = new Intent(context, Game.class);
        context.startActivity(i);
        return true;
    }
}
