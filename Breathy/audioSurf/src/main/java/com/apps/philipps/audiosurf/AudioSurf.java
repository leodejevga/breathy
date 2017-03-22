package com.apps.philipps.audiosurf;

import android.content.Context;
import android.support.annotation.RawRes;

import com.apps.philipps.source.abstracts.AbstractGame;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */

/**
 * Initializes the Audio Surf game
 */
public class AudioSurf extends AbstractGame {

    Context context;
    /**
     * Instantiates a new Audio surf Object.
     *
     * @param context the context from Main Activity
     */
    public AudioSurf(Context context){
        this.context = context;
        price = 2500;
        name = "Audio Surf";
        game = new AudioSurfGame(context);
        options = new AudioSurfOptions(context);
        Backend.init();
    }

    /**
     * Start the preview.
     *
     * @return true if the preview successfully started
     */
    @Override
    public @RawRes Integer getPreview(){
        return R.raw.preview;
    }
}
