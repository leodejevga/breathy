package com.apps.philipps.audiosurf;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.apps.philipps.audiosurf.activities.Game2D;

/**
 * This class provieds the survaceView to draw your game
 */

public class GameView extends SurfaceView{
    SurfaceHolder holder;
    Game2D gameActivity;
    Context context;

    public GameView(Context context) {
        super(context);
        this.context = context;


        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

    }
}
