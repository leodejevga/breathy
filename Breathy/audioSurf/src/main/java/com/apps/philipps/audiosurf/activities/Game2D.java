package com.apps.philipps.audiosurf.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.TextView;

import com.apps.philipps.audiosurf.Barricade;
import com.apps.philipps.audiosurf.Player;
import com.apps.philipps.audiosurf.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.GameUtil;
import com.apps.philipps.source.abstracts.AbstractGameActivity;

import java.util.ArrayList;

public class Game2D extends AbstractGameActivity {

    TextView framerate;
    TextView frameCount;
    TextView dataDisplay;

    // display parameter
    int displayWidth;
    int displayHeight;
    Canvas canvas;


    // Bitmaps
    Bitmap playerbmp = BitmapFactory.decodeResource(getResources(), R.drawable.ship);
    Bitmap barricadebmp = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);

    // Game stats
    Player player;
    ArrayList<Barricade> barricades = new ArrayList<Barricade>();
    double nextEmeny;
    double timerNextEmeny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //get display width and height
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayWidth = size.x;
        displayHeight = size.y;

        // ini Game
        player = new Player(this, playerbmp, displayHeight-20, displayWidth/2 - 8);

        setContentView(R.layout.as_game2d);
        AppState.recordData = AppState.inGame = true;
    }

    @Override
    protected void draw() {
        String data = BreathData.getAsString(0, 5);
        dataDisplay.setText(data);
        framerate.setText(frameRate + " fps");
        frameCount.setText("frame " + frame);
        player.draw(canvas);
        for(Barricade b: barricades){
            b.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        player.onTouch(event);

        return false;
    }

    @Override
    protected void init() {
        framerate = (TextView) findViewById(R.id.as_framerate);
        dataDisplay = (TextView) findViewById(R.id.as_data);
        frameCount = (TextView) findViewById(R.id.as_frameCount);
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