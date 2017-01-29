package com.apps.philipps.audiosurf;

import android.content.Context;
import android.content.Intent;
import com.apps.philipps.audiosurf.activities.Options;
import com.apps.philipps.source.abstracts.AbstractGameObject;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */
public class AudioSurfOptions extends AbstractGameObject {

    /**
     * Instantiates a new Audio surf options.
     *
     * @param context the context
     */
    public AudioSurfOptions(Context context){
        this.context = context;
    }
    @Override
    public boolean start() {
        if(context==null)
            return false;
        Intent i = new Intent(context, Options.class);
        context.startActivity(i);
        return true;
    }
}
