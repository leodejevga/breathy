package com.apps.philipps.spaceFight;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.source.implementations.BreathyGameComponent;
import com.apps.philipps.spaceFight.activities.FlightOptions;

/**
 * Created by Jevgenij Huebert on 19.03.2017. Project Breathy
 */

public class SpaceFlightOptions extends BreathyGameComponent {

    public SpaceFlightOptions(Context context) {
        this.context = context;
    }

    @Override
    public boolean start() {
        Intent i = new Intent(context, FlightOptions.class);
        context.startActivity(i);
        return true;
    }
}
