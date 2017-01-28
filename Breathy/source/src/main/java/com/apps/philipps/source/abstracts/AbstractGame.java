package com.apps.philipps.source.abstracts;

import android.widget.Toast;

import com.apps.philipps.source.Coins;
import com.apps.philipps.source.interfaces.IBootable;
import com.apps.philipps.source.interfaces.IGame;

/**
 * Created by leode on 27.01.2017.
 */

public abstract class AbstractGame implements IGame {
    protected AbstractGameObject game;
    protected AbstractGameObject options;
    protected AbstractGameObject preview;
    protected int price;
    protected String name;
    protected boolean bought;

    @Override
    public boolean isBought() {
        return bought;
    }

    @Override
    public boolean buy() {
        if(!bought)
            bought = Coins.buy(price);
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
            game.start();
        else
            game.makeToast("The game " + name + " was not bought");
        return bought;
    }
    @Override
    public boolean startOptions() {
        return options.start();
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
