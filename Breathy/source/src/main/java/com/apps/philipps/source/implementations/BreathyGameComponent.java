package com.apps.philipps.source.implementations;

import android.content.Context;
import android.widget.Toast;

import com.apps.philipps.source.interfaces.IStart;

/**
 * Created by Jevgenij Huebert on 28.01.2017. Project Breathy
 */
public abstract class BreathyGameComponent implements IStart {
    /**
     * The Context.
     */
    protected Context context;

    @Override
    public void message(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
