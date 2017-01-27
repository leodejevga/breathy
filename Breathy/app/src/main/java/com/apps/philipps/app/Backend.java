package com.apps.philipps.app;

import com.apps.philipps.source.interfaces.IHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leode on 27.01.2017.
 */

public class Backend {
    public static List<IHandler> games;

    public static boolean init(){
        games = new ArrayList<>();
        return true;
    }
}
