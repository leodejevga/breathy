package com.apps.philipps.fade;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.fade.activities.Game;
import com.apps.philipps.source.implementations.BreathyGameComponent;

public class FadeGame extends BreathyGameComponent {
    /**
     * Instantiates a new FadeGame.
     *
     * @param context the context
     */
    public FadeGame(Context context){
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

}
