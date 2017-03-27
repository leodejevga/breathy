package com.breathy.racing.activities;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
 * Created by Jürgen on 26.03.2017.
 */

public class Application extends Activity2D {
    GameObject2D car;
    List<GameObject2D> slowCar;
    RelativeLayout game;
    double deltaSpeed;
    long nextCar;
    long start;
    Random random;
    int[] xRoads;
    float yCar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.racing_gamescreen);
    }

    @Override
    protected void draw() {
        long delta = System.currentTimeMillis() - start;

        if (System.currentTimeMillis() - nextCar > 1000) {
            int xIndex = GameUtil.getRandomNumber(0, 2);
            slowCar.add(initObject(new ImageView(this), R.drawable.slow_car, 0, new Vector((float) xRoads[xIndex], (float) 10), new Vector((float) xRoads[xIndex], getScreenHeight()), 220));
            nextCar = System.currentTimeMillis();
        }
        for (int i = 0; i < slowCar.size(); i++) {
            boolean removed = false;

            if (!removed && !slowCar.get(i).isMoving()) {
                game.removeView(slowCar.get(i).getView());
                slowCar.remove(slowCar.get(i));
            } else if (!removed)
                slowCar.get(i).update(delta);

        }

        car.update(delta);

        start = System.currentTimeMillis();
    }

    @Override
    protected void init() {
        AppState.framelimit = AppState.Framelimit.Sixty;
        game = (RelativeLayout) findViewById(R.id.gameArea);
        xRoads = new int[]{25, (int) getScreenWidth() / 2, (int) getScreenWidth() - 25};
        yCar = getScreenHeight() - 264;
        car = initObject(new ImageView(this), R.drawable.ship, 1, new Vector((float) xRoads[1], yCar), new Vector((float) xRoads[1], yCar), 1200);
        slowCar = new ArrayList<>();
        start = System.currentTimeMillis();
        nextCar = start;
        deltaSpeed = 0.05;
        random = new Random(5);
        if (game == null)
            brakeDraw();
    }

    @Override
    protected void touched(MotionEvent event) {
        int dx = (int) event.getX();
        int dy = (int) event.getY();

        // get the direction
        if (getScreenWidth() / 2 < dx) {
            movePlayer("right");
        } else {
            movePlayer("left");
        }
    }

    public void movePlayer(String direction) {
        switch (direction) {
            case "right":
                if (car.getPosition().get(0) == xRoads[1])
                    car.move(new Vector((float) xRoads[2], yCar));
                else car.move(new Vector((float) xRoads[1], yCar));
                break;
            case "left":
                if (car.getPosition().get(0) == xRoads[1])
                    car.move(new Vector((float) xRoads[0], yCar));
                else car.move(new Vector((float) xRoads[1], yCar));
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


