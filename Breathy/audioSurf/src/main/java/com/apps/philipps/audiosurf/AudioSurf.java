package com.apps.philipps.audiosurf;

import android.content.Context;

import com.apps.philipps.source.abstracts.AbstractGame;

/**
 * Created by leode on 27.01.2017.
 */

public class AudioSurf extends AbstractGame {
    public AudioSurf(Context c){
        price = 2500;
        name = "Audio Surf";
        game = new AudioSurfGame(c);
        options = new AudioSurfOptions(c);
        preview = new AudioSurfPreview(c);
        Backend.init();
    }
}
