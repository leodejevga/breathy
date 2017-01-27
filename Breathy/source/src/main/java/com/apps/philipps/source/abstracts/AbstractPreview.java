package com.apps.philipps.source.abstracts;

import android.app.Activity;

import com.apps.philipps.source.interfaces.IPreview;

/**
 * Created by leode on 27.01.2017.
 */

public abstract class AbstractPreview extends GameObject implements IPreview{
    @Override
    public Activity getPreview() {
        return activity;
    }
}
