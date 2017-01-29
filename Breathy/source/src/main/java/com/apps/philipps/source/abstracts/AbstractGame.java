package com.apps.philipps.source.abstracts;

import android.widget.Toast;

import com.apps.philipps.source.Coins;
import com.apps.philipps.source.interfaces.IBootable;
import com.apps.philipps.source.interfaces.IGame;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */
public abstract class AbstractGame implements IGame {
    /**
     * The Game.
     */
    protected AbstractGameObject game;
    /**
     * The Options.
     */
    protected AbstractGameObject options;
    /**
     * The Preview.
     */
    protected AbstractGameObject preview;
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
                game.makeToast("Congratulations! You bought " + name);
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
        if(bought)
            return game.start();
        else
            game.makeToast("The game " + name + " was not bought");
        return bought;
    }
    @Override
    public boolean startOptions() {
        if(bought)
            return options.start();
        else
            game.makeToast("The game " + name + " was not bought");
        return bought;
    }
    @Override
    public boolean startPreview() {
        return preview.start();
    }
    @Override
    public String toString() {
        return name;
    }
}
