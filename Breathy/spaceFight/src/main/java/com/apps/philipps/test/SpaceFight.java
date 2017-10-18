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
        game = new SpaceFlightStart(context);
        options = new SpaceFlightOptions(context);
        this.bought = bought;
    }
    @Override
    public Integer getPreview() {
        return R.raw.spacefightpreview;
    }
}
