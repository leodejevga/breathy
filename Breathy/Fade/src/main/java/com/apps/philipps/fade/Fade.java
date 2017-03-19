package com.apps.philipps.fade;

import android.content.Context;
import android.support.annotation.RawRes;

import com.apps.philipps.source.abstracts.AbstractGame;
public class Fade extends AbstractGame {
    Context context;
    /**
     * Instantiates a new Audio surf Object.
     *
     * @param context the context from Main Activity
     */
    public Fade(Context context){
        this.context = context;
        price = 2500;
        name = "Fade";
    }

    /**
     * Start the preview.
     *
     * @return true if the preview successfully started
     */
    @Override
    public @RawRes Integer getPreview(){
        return null;
    }
}
