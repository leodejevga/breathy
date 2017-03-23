package com.apps.philipps.test.activities;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apps.philipps.test.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper.Activity2D;
import com.apps.philipps.source.helper.GameObject2D;
import com.apps.philipps.source.helper.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Application extends Activity2D{
    GameObject2D ship;
    List<GameObject2D> enemies;
    List<GameObject2D> lasers;
    RelativeLayout game;
    double enemySpeed;
    long enemySpawn;
    long start;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_application);
    }

    @Override
    protected void draw() {
        long delta = System.currentTimeMillis() - start;
        if(System.currentTimeMillis() - enemySpawn>1000){

            int y = Math.abs(random.nextInt())%(int)getScreenHeight();
            enemies.add(initObject(new ImageView(this), R.drawable.enemy, 0, new Vector(1000f, (float)y), new Vector(50f, (float)y), 320));
            enemySpawn = System.currentTimeMillis();
        }
        for (int i = 0; i < enemies.size(); i++) {
            boolean removed = false;
            for (int j = 0; j < lasers.size(); j++) {
                if (enemies.get(i).intersect(lasers.get(j))) {
                    game.removeView(enemies.get(i).getView());
                    game.removeView(lasers.get(j).getView());
                    enemies.remove(enemies.get(i));
                    lasers.remove(lasers.get(j));
                    removed = true;
                    i--;
                }
            }
            if(!removed && !enemies.get(i).isMoving()){
                game.removeView(enemies.get(i).getView());
                enemies.remove(enemies.get(i));
            } else if(!removed)
                enemies.get(i).update(delta);

        }
        for (int i = 0; i < lasers.size(); i++) {
            if(!lasers.get(i).isMoving()){
                game.removeView(lasers.get(i).getView());
                lasers.remove(lasers.get(i));
            } else
                lasers.get(i).update(delta);
        }
        ship.move(new Vector(50f, getScreenHeight()-(BreathData.get(0)*getScreenHeight())/1024f));
        ship.update(delta);

        start = System.currentTimeMillis();
    }

    @Override
    protected void init() {
        AppState.framelimit = AppState.Framelimit.Sixty;
        game = (RelativeLayout) findViewById(R.id.test_game_area);
        ship = initObject(new ImageView(this), R.drawable.ship, 1, new Vector(50f, getScreenHeight()/2f), new Vector(50f, getScreenHeight()), 1200);
        enemies = new ArrayList<>();
        lasers = new ArrayList<>();
        start = System.currentTimeMillis();
        enemySpawn = start;
        enemySpeed = 0.05;
        random = new Random(5);
        if(game == null)
            brakeDraw();
    }

    @Override
    protected void touched(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            lasers.add(initObject(new ImageView(this), R.drawable.laser, 2, ship.getPosition().clone(), new Vector(getScreenWidth(), ship.getPosition().get(1)), 3000));
    }

    private GameObject2D initObject(ImageView view, @DrawableRes int content, int id, Vector position, Vector destination, int move){
        view.setId(id);
        view.setImageResource(content);
        game.addView(view);
        GameObject2D result = new GameObject2D(view, position, destination);
        result.move(move);
        return result;
    }
}
