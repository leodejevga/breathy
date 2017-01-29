package com.apps.philipps.audiosurf;

import android.content.Context;

import com.apps.philipps.source.abstracts.AbstractGame;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */

/**
 * Initializes the Audio Surf game
 */
public class AudioSurf extends AbstractGame {
    /**
     * Instantiates a new Audio surf Object.
     *
     * @param context the context from Main Activity
     */
    public AudioSurf(Context context){
        price = 2500;
        name = "Audio Surf";
        game = new AudioSurfGame(context);
        options = new AudioSurfOptions(context);
        preview = new AudioSurfPreview(context);
        Backend.init();
    }
}
