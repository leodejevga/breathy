package com.apps.philipps.audiosurf;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.apps.philipps.audiosurf.activities.Game2D;

/**
 * This class provides the opportunity to create a player object for the game
 */

public class Player {
    private int height;
    private int width;
    private Game2D gameView;
    private int x, y;
    private Bitmap playerbmp;

    public Player(Game2D gameView, Bitmap bmp, int x, int y){
        this.gameView = gameView;
        this.playerbmp = bmp;
        this.x = x;
        this.y = y;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();
    }

    public void draw(Canvas canvas){

        Rect src = new Rect(0,0,1 + width,1+height);
        Rect dst = new Rect(x,y,x+(width),y+(height));
        canvas.drawBitmap(playerbmp, src, dst, null);
    }

    public void onTouch(MotionEvent event) {
        int dx = (int)event.getX();
        int dy = (int)event.getY();

        // get the direction
        if( gameView.getDisplayWidth() / 2 < dx){
            movePlayer("right");
        }else{
            movePlayer("left");
        }
    }

    public void movePlayer(String direction){
        switch (direction){
            case "right":
                if (x==width/2) x = width * (2/3);
                else x = width / 2;
                //ToDo player bewegen
                break;
            case "left":
                if (x==width/2) x = width *(1/3);
                else x = width / 2;
                break;
        }
    }
}
