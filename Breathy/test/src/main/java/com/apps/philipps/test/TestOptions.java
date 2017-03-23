package com.apps.philipps.test;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.source.implementations.BreathyGameComponent;
import com.apps.philipps.test.activities.Options;

/**
 * Created by Jevgenij Huebert on 19.03.2017. Project Breathy
 */

public class TestOptions extends BreathyGameComponent {

    public TestOptions (Context context){
        this.context = context;
    }

    @Override
    public boolean start() {
        Intent i = new Intent(context, Options.class);
        context.startActivity(i);
        return true;
    }
}
