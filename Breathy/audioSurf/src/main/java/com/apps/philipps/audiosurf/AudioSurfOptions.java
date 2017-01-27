package com.apps.philipps.audiosurf;

import android.content.Context;
import android.content.Intent;
import com.apps.philipps.audiosurf.activities.Options;
import com.apps.philipps.source.abstracts.AbstractOptions;

/**
 * Created by leode on 27.01.2017.
 */
public class AudioSurfOptions extends AbstractOptions {

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
