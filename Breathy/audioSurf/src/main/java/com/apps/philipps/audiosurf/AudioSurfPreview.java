package com.apps.philipps.audiosurf;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.audiosurf.activities.Preview;
import com.apps.philipps.source.abstracts.AbstractGameObject;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */
public class AudioSurfPreview extends AbstractGameObject {
    /**
     * Instantiates a new Audio surf preview.
     *
     * @param context the context
     */
    public AudioSurfPreview(Context context) {
        this.context = context;
    }
    @Override
    public boolean start() {
        if (context == null)
            return false;
        Intent i = new Intent(context, Preview.class);
        context.startActivity(i);
        return true;
    }
}
