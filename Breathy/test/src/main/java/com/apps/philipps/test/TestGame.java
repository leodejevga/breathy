package com.apps.philipps.test;

import android.content.Context;
import android.content.Intent;

import com.apps.philipps.source.abstracts.AbstractGameObject;
import com.apps.philipps.test.activities.Game;

/**
 * Created by Jevgenij Huebert on 19.03.2017. Project Breathy
 */

public class TestGame extends AbstractGameObject {

    public TestGame(Context context){
        this.context = context;
    }

    @Override
    public boolean start() {
        Intent i = new Intent(context, Game.class);
        context.startActivity(i);
        return true;
    }
}
