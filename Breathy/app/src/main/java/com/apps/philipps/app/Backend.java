package com.apps.philipps.app;

import android.content.Context;

import com.apps.philipps.audiosurf.AudioSurf;
import com.apps.philipps.source.interfaces.IGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leode on 27.01.2017.
 */

public class Backend {
    public static List<IGame> games;
    public static IGame selected;

    public static boolean init(Context c){
        games = new ArrayList<>();
        Backend.games.add(new AudioSurf(c)); //TODO: Automatisches Füllen der Spiele in die Liste
        return true;
    }

}
