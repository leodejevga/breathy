package com.apps.philipps.source.interfaces;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */
public interface IBootable {

    /**
     * Start Activity. Provide here your states with the <code>AppState</code> Class
     *
     * @return true if the Activity started
     */
    boolean start();

    /**
     * Pops the Toast with the given <code>text</code>
     *
     * @param text the text to show in the Toast
     */
    void message(String text);
}
