package com.apps.philipps.audiosurf;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.audiosurf.activities.Main;
import com.apps.philipps.source.abstracts.AbstractGame;

/**
 * Created by leode on 27.01.2017.
 */

public class AudioSurf extends AbstractGame {

    public AudioSurf(Context context){
        this.context = context;
        name = "Audio Surf";
        price = 2500;
    }

    @Override
    public boolean start() {
        if(context==null)
            return false;
        Intent i = new Intent(context, Main.class);
        context.startActivity(i);
        return true;
    }
}
