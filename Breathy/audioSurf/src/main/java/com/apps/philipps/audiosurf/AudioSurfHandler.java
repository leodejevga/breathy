package com.apps.philipps.audiosurf;

import android.content.Context;

import com.apps.philipps.source.abstracts.AbstractHandler;

/**
 * Created by leode on 27.01.2017.
 */

public class AudioSurfHandler extends AbstractHandler{
    public AudioSurfHandler(Context c){
        game = new AudioSurf(c);
        options = new AudioSurfOptions(c);
    }
}
