package com.breathy.racing;

import android.content.Context;
import android.support.annotation.RawRes;

import com.apps.philipps.source.implementations.BreathyGame;

/**
 * Created by Jürgen on 26.03.2017.
 */

public class Race extends BreathyGame {
    public Race() {
        price = 1;
        name = "Racing Game";
    }

    @Override
    public void init(Context context, boolean bought) {
        game = new RaceGame(context);
        options = new RaceOption(context);
        this.bought = bought;
    }

    @Override
    public
    @RawRes
    Integer getPreview() {
        return R.raw.preview;
    }
}

