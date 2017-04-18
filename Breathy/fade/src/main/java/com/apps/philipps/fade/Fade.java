package com.apps.philipps.fade;

import android.content.Context;
import android.support.annotation.RawRes;

import com.apps.philipps.source.implementations.BreathyGame;

public class Fade extends BreathyGame {
    Context context;

    public Fade() {
        price = 0;
        name = "Fade";
    }

    @Override
    public void init(Context context, boolean bought) {
        this.bought = bought;
        this.context = context;
        options = new FadeOptions(context);
        game = new FadeGame(context);
    }

    /**
     * Start the preview.
     *
     * @return true if the preview successfully started
     */
    @Override
    public
    @RawRes
    Integer getPreview() {
        return R.raw.preview3;
    }
}
