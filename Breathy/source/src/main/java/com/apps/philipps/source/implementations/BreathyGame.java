package com.apps.philipps.source.implementations;

import android.content.Context;

import com.apps.philipps.source.Coins;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.interfaces.IGame;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */
public abstract class BreathyGame implements IGame {
    /**
     *
     */
    boolean initialized;
    /**
     * The Game.
     */
    protected BreathyGameComponent game;
    /**
     * The GameOptions.
     */
    protected BreathyGameComponent options;
    /**
     * The Price for this Game.
     */
    protected int price;
    /**
     * The Name of this Game.
     */
    protected String name;
    /**
     * True if this game was bought.
     */
    protected boolean bought;

    @Override
    public boolean isBought() {
        return bought;
    }

    @Override
    public boolean buy() {
        if(!bought) {
            bought = Coins.buy(price);
            if(bought)
                game.message("Congratulations! You bought " + name);
        }
        else return false;

        return bought;
    }

    @Override
    public String getName() {
        return "Breathy: " + this;
    }

    @Override
    public boolean startGame() {
        AppState.inGame = true;
        if(bought) {
            if(!game.start())
                game.message("Game " + name + " could not started");
        }
        else
            game.message("The game " + name + " was not bought");
        return bought;
    }
    @Override
    public boolean startOptions() {
        if(bought)
            options.start();
        else
            game.message("The game " + name + " was not bought");
        return bought;
    }
    @Override
    public String toString() {
        return name;
    }


}
