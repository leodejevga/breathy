package com.apps.philipps.source.interfaces;

/**
 * Created by leode on 27.01.2017.
 */

public interface IGame {
    public boolean startGame();
    public boolean startOptions();
    public boolean startPreview();
    public boolean isBought();
    public boolean buy();
    public String getName();
}
