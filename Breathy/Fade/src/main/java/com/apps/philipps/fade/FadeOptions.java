package com.apps.philipps.fade;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.fade.activities.OptionsFade;
import com.apps.philipps.source.implementations.BreathyGameComponent;

/**
 * Created by Var on 30.03.2017.
 */

public class FadeOptions extends BreathyGameComponent {

    /**
     * Instantiates a new Fade options.
     *
     * @param context the context
     */
    public FadeOptions(Context context){
        this.context = context;
    }
    @Override
    public boolean start() {
        if(context==null)
            return false;
        Intent i = new Intent(context, OptionsFade.class);
        context.startActivity(i);
        return true;
    }
}
