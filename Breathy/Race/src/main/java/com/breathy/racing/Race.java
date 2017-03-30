package com.breathy.racing;

import android.content.Context;
import android.support.annotation.RawRes;

import com.apps.philipps.source.implementations.BreathyGame;

/**
 * Created by Jürgen on 26.03.2017.
 */

public class Race extends BreathyGame {

    public Race(Context context) {
        game = new RaceGame(context);
        options = new RaceOption(context);
        price = 1;
        name = "Racing Game";
    }

    @Override
    public
    @RawRes
    Integer getPreview() {
        return null;//R.raw.preview; //ToDo wieder ändern
    }
}

