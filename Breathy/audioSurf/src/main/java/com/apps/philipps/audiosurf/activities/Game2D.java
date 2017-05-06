package com.apps.philipps.audiosurf.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.TextView;

import com.apps.philipps.audiosurf.Barricade;
import com.apps.philipps.audiosurf.GameUtil;
import com.apps.philipps.audiosurf.GameView;
import com.apps.philipps.audiosurf.Player;
import com.apps.philipps.audiosurf.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper._2D.Activity2D;

import java.util.ArrayList;

public class Game2D extends Activity2D {

    TextView framerate;
    TextView frameCount;
    TextView dataDisplay;

    // display parameter
    int displayWidth;
    int displayHeight;
    Canvas canvas;

    // Display
    GameView gameview;

    // Bitmaps
    Bitmap playerbmp = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
    Bitmap barricadebmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);

    // Game stats
    Player player;
    ArrayList<Barricade> barricades = new ArrayList<Barricade>();
    double nextEmeny;
    double timerNextEmeny;
    int speed = 1;

    public void touch(MotionEvent event){

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameview = new GameView(this);

        setContentView(gameview);

        //get display width and height
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayWidth = size.x;
        displayHeight = size.y;

        // ini Game
        player = new Player(this, playerbmp, displayHeight-20, displayWidth/2 - 8);


        AppState.recordData = AppState.inGame = true;
    }

    @Override
    protected void draw() {
        String data = BreathData.getAsString(0, 5);
        canvas = gameview.getHolder().lockCanvas();
        if(canvas != null) {
            //dataDisplay.setText(data);
            //framerate.setText(frameRate + " fps");
            //frameCount.setText("frame " + frame);
            player.draw(canvas);
            for (Barricade b : barricades) {
                b.draw(canvas);
            }
            generateAIObjects();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        player.onTouch(event);

        return false;
    }

    @Override
    protected void init() {
        //framerate = (TextView) findViewById(R.id.as_framerate);
        //dataDisplay = (TextView) findViewById(R.id.as_data);
        //frameCount = (TextView) findViewById(R.id.as_frameCount);
    }

    public void generateAIObjects(){
        timerNextEmeny++;

        if(timerNextEmeny>nextEmeny){
            Barricade b = new Barricade(this, barricadebmp, 20, 0); //ToDo richtige Werte übergeben
            barricades.add(b);
            nextEmeny = GameUtil.triangularDistribution(20, 60, 40);
            timerNextEmeny=0;
        }
    }

    public int getDisplayWidth() {
        return displayWidth;
    }
}