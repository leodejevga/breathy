package com.apps.philipps.source.abstracts;

import android.content.Context;

import com.apps.philipps.source.interfaces.IGame;
import com.apps.philipps.source.interfaces.IHandler;
import com.apps.philipps.source.interfaces.IOptions;
import com.apps.philipps.source.interfaces.IPreview;

import java.io.IOException;

/**
 * Created by leode on 27.01.2017.
 */

public abstract class AbstractHandler implements IHandler{
    protected AbstractGame game;
    protected AbstractOptions options;
    protected AbstractPreview preview;
    @Override
    public IGame getGame() {
        return game;
    }

    @Override
    public IOptions getOptions() {
        return options;
    }

    @Override
    public IPreview getPreview() {
        return preview;
    }
    @Override
    public String toString() {
        return game.toString();
    }
}
