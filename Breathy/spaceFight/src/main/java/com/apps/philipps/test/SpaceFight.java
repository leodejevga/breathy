package com.apps.philipps.test;

import android.content.Context;
import android.support.annotation.RawRes;

import com.apps.philipps.source.implementations.BreathyGame;

/**
 * Created by Jevgenij Huebert on 19.03.2017. Project Breathy
 */
public class SpaceFight extends BreathyGame {
    public SpaceFight() {
        price = 0;
        name = "Space Fight";
    }

    @Override
    public void init(Context context, boolean bought) {
        game = new TestGame(context);
        options = new TestOptions(context);
        this.bought = bought;
    }
    @Override
    public @RawRes Integer getPreview() {
        return R.raw.preview1;
    }
}
