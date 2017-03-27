package com.apps.philipps.test;

import android.content.Context;
import android.support.annotation.RawRes;

import com.apps.philipps.source.implementations.BreathyGame;

/**
 * Created by Jevgenij Huebert on 19.03.2017. Project Breathy
 */
public class Test extends BreathyGame {

    public Test(Context context){
        game = new TestGame(context);
        options = new TestOptions(context);
        price = 1;
        name = "2D Minispiel zum Testen";
    }
    @Override
    public @RawRes Integer getPreview() {
        return R.raw.preview;
    }
}
