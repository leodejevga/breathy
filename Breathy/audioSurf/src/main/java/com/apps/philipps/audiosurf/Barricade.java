package com.apps.philipps.audiosurf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.apps.philipps.audiosurf.activities.Game2D;

/**
 * This class provides barricade objects
 */

public class Barricade {
    int x,y;
    private Bitmap bmp;
    private Game2D gameView;
    private Rect player;
    private Rect barricade;

    public Barricade(Game2D gameview, Bitmap barricadebmp, int x, int y){
        this.gameView = gameview;
        this.bmp = barricadebmp;
        this.x = x;
        this.y = y;
    }

    public boolean checkCollision(Rect player, Rect barricade){
        this.barricade = barricade;
        this.player = player;

        return Rect.intersects(player, barricade);
    }

    public void draw(Canvas canvas) {

    }
}
