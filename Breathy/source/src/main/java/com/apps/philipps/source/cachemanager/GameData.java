package com.apps.philipps.source.cachemanager;

import java.io.Serializable;

/**
 * Created by qwert on 26/03/2017.
 */

public class GameData implements Serializable {
    private boolean bought;

    public boolean getBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    private int credit;

}
