package com.apps.philipps.source.interfaces;

/**
 * Created by leode on 27.01.2017.
 */

public interface IGame extends IBootable {
    public String getName();
    public boolean isBought();
    public boolean buy(int money);
}
