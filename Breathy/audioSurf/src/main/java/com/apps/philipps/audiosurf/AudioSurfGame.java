package com.apps.philipps.audiosurf;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.audiosurf.activities.Game;
import com.apps.philipps.source.abstracts.AbstractGameObject;

/**
 * Created by leode on 27.01.2017.
 */

public class AudioSurfGame extends AbstractGameObject {

    public AudioSurfGame(Context context){
        this.context = context;
    }
    @Override
    public boolean start() {

        if(context==null)
            return false;
        Intent i = new Intent(context, Game.class);
        context.startActivity(i);
        return true;
    }

    @Override
    public boolean end() {
        //TODO: Beende die Activity
        return false;
    }
}
