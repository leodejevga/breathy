package com.apps.philipps.source.abstracts;

import android.app.Activity;
import android.content.Context;

import com.apps.philipps.source.interfaces.IGame;

/**
 * Created by leode on 27.01.2017.
 */

public abstract class AbstractGame extends GameObject implements IGame {
    protected int price = 0;
    protected boolean bought;

    public String getName() {
        return null;
    }

    public boolean isBought() {
        return bought;
    }

    public boolean buy(int money) {
        if(money<price || bought)
            return false;
        bought = true;
        return false;
    }

    @Override
    public boolean end() {
        return false;
    }
}
