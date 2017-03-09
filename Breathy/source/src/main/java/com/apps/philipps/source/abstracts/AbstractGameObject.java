package com.apps.philipps.source.abstracts;

import android.content.Context;
import android.graphics.Movie;
import android.widget.Toast;

import com.apps.philipps.source.interfaces.IBootable;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */
public abstract class AbstractGameObject implements IBootable {
    /**
     * The Context.
     */
    protected Context context;

    @Override
    public void message(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
