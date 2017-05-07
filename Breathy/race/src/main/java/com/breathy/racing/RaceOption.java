package com.breathy.racing;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.source.implementations.BreathyGameComponent;
import com.breathy.racing.activities.Options;

/**
 * Created by Jürgen on 26.03.2017.
 */

public class RaceOption extends BreathyGameComponent {

    public RaceOption(Context context) {
        this.context = context;
    }

    @Override
    public boolean start() {
        Intent i = new Intent(context, Options.class);
        context.startActivity(i);
        return true;
    }
}