package com.apps.philipps.audiosurf;

import android.content.Context;
import android.content.Intent;
import android.view.View;

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
    public View start() {

        //TODO: Videoview zurück geben
        return null;
    }
}
