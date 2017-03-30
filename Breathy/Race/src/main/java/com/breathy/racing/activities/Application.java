package com.breathy.racing.activities;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.GameUtil;
import com.apps.philipps.source.helper.Activity2D;
import com.apps.philipps.source.helper.GameObject2D;
import com.apps.philipps.source.helper.Vector;
import com.breathy.racing.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This Class provides the Game Logic of the racing Game
 */

public class Application extends Activity2D {
    GameObject2D car;
    List<GameObject2D> slowCar;
    RelativeLayout game;
    double deltaSpeed;
    long nextCar;
    long start;
    Random random;
    float[] xRoads;
    float yCar;
    double highscore = 0;
    double safedhighscore = 0;
    double gameScoreMultiplier = 1;
    TextView score;
    int bostPoints;
    ProgressBar lvlUpBar;
    int curXIndex;
    int faster=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.racing_gamescreen);
    }

    @Override
    protected void draw() {
        long delta = System.currentTimeMillis() - start;
        if(faster >= 5000){
            deltaSpeed += 10;
            faster = 0;
            gameScoreMultiplier += .1;
            for (int i = 0; i < slowCar.size(); i++) {
                slowCar.get(i).move((int) deltaSpeed);
            }
        }else faster ++;

        if (System.currentTimeMillis() - nextCar > 1000) {
            int xIndex = GameUtil.getRandomNumber(0, 2);
            slowCar.add(initObject(new ImageView(this), R.drawable.slow_car, 0, new Vector(xRoads[xIndex], (float) 10), new Vector(xRoads[xIndex], getScreenHeight()), (int) deltaSpeed));
            nextCar = System.currentTimeMillis();
        }
        for (int i = 0; i < slowCar.size(); i++) {
            boolean removed = false;

            if (slowCar.get(i).intersect(car)) {
                game.removeView(slowCar.get(i).getView());
                slowCar.remove(slowCar.get(i));
                removed = true;
                i--;
                highscore = 0;
            }
            if (!removed && !slowCar.get(i).isMoving()) {
                game.removeView(slowCar.get(i).getView());
                slowCar.remove(slowCar.get(i));
                highscore += 1 * gameScoreMultiplier;

            } else if (!removed)
                slowCar.get(i).update(delta);

        }

        car.update(delta);
        score.setText("Highscore: " + safedhighscore + " + " + highscore);
        start = System.currentTimeMillis();
        Integer testdata = BreathData.get(0) * GameUtil.getRandomNumber(0, 1);
        Integer breathdata = BreathData.get(0);
        if (testdata.equals(breathdata)) { //ToDo toleranz
            bostPoints += 10;
        } else if (testdata + 100 < breathdata && testdata - 100 > breathdata) {
            bostPoints = Math.max(bostPoints - 1, 0);
        }
        lvlUpBar.setProgress(bostPoints);

    }


    @Override
    protected void init() {
        AppState.framelimit = AppState.Framelimit.Sixty;


        //RelativeLayout game = new RelativeLayout(getBaseContext());
        game = (RelativeLayout) findViewById(R.id.gameArea);
        score = (TextView) findViewById(R.id.score);
        lvlUpBar = (ProgressBar) findViewById(R.id.progressBar);
        lvlUpBar.setProgress(0);
        lvlUpBar.setMax(10000);
        xRoads = new float[]{250, (int) getScreenWidth() / 2, (int) getScreenWidth() - 250};

        yCar = getScreenHeight() - 264;
        car = initObject(new ImageView(this), R.drawable.car, 1, new Vector(xRoads[1], yCar), new Vector(xRoads[1], yCar), 1200);
        curXIndex = 1;
        slowCar = new ArrayList<>();
        start = System.currentTimeMillis();
        nextCar = start;
        deltaSpeed = 220;
        random = new Random(5);
        if (game == null)
            brakeDraw();
    }

    @Override
    protected void touched(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int dx = (int) event.getX();
            int dy = (int) event.getY();
            // get the direction
            if (dy < getScreenHeight()/2 && bostPoints>= lvlUpBar.getMax()){
                safedhighscore += highscore * 1.2;
                bostPoints = 0;
                highscore = 0;
            }else
            if (getScreenWidth() / 2 < dx) {
                movePlayer("right");
            } else {
                movePlayer("left");
            }
        }
    }

    public void movePlayer(String direction) {
        switch (direction) {
            case "right":

                if (curXIndex == 0) {
                    car.move(new Vector(xRoads[1], yCar));
                    curXIndex = 1;
                } else if (curXIndex == 1) {
                    car.move(new Vector(xRoads[2], yCar));
                    curXIndex = 2;
                }
                break;
            case "left":
                if (curXIndex == 2) {
                    car.move(new Vector(xRoads[1], yCar));
                    curXIndex = 1;
                } else if (curXIndex == 1) {
                    car.move(new Vector(xRoads[0], yCar));
                    curXIndex = 0;
                }
                break;
        }
    }

    private GameObject2D initObject(ImageView view, @DrawableRes int content, int id, Vector position, Vector destination, int move) {
        view.setId(id);
        view.setImageResource(content);
        game.addView(view);
        GameObject2D result = new GameObject2D(view, position, destination);
        result.move(move);
        return result;
    }
}


