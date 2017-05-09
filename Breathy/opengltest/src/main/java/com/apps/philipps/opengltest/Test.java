package com.apps.philipps.opengltest;

import android.content.Context;

import com.apps.philipps.source.implementations.BreathyGame;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class Test extends BreathyGame {
    public Test() {
        this.name = "OpenGl Test";
        this.price = 20;
    }

    /**
     * Start the preview.
     *
     * @return true if the preview successfully started
     */
    @Override
    public Integer getPreview() {
        return null;
    }

    @Override
    public void init(Context context, boolean bought) {
        this.bought = bought;
        game = new TestGame(context);
        options = new TestOptions(context);
    }
}
