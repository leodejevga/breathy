package com.apps.philipps.source.abstracts;

import android.app.Activity;
import android.content.Context;

/**
 * Created by leode on 27.01.2017.
 */

public abstract class GameObject {
    protected String name;
    protected Context context;
    protected Activity activity;

    @Override
    public String toString() {
        return name;
    }
}
