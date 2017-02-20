package com.apps.philipps.source.interfaces;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by Jevgenij Huebert on 27.01.2017. Project Breathy
 */
public interface IBootable {
    /**
     * Start Activity
     *
     * @return true if the Activity started
     */
    public Object start();

    /**
     * End Activity
     *
     * @return true if the Activity ended
     */
    public boolean end(); //TODO: Ich weiß noch nicht ob diese Methode sinnvoll ist.

    /**
     * Pops the Toast with the given <code>text</code>
     *
     * @param text the text to show in the Toast
     */
    public void makeToast(String text);
}
