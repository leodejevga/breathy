package com.apps.philipps.audiosurf.activities;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.apps.philipps.audiosurf.R;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper.Activity2D;
import com.apps.philipps.source.helper.GameObject2D;
import com.apps.philipps.source.helper.Vector;

import java.security.PolicySpi;
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
        setContentView(R.layout.as_application);
    }

    @Override
    protected void draw() {
        long delta = System.currentTimeMillis() - start;
        if(System.currentTimeMillis() - enemySpawn>1000){

            float y = Math.abs(random.nextInt())%1920f;
            enemies.add(initObject(new ImageView(this), R.drawable.enemy, 0, new Vector(1000f, y), new Vector(50f, y), 320));
            enemySpawn = System.currentTimeMillis();
        }
        for(GameObject2D enemy : enemies){
            for(GameObject2D laser : lasers)
                if(enemy.intercect(laser)){
                    game.removeView(enemy.getView());
                    game.removeView(laser.getView());
                    enemies.remove(enemy);
                    lasers.remove(laser);
                }

            if(!enemy.isMoving()){
                game.removeView(enemy.getView());
                enemies.remove(enemy);
            } else
                enemy.update(delta);
        }
        for(GameObject2D laser : lasers){
            if(!laser.isMoving()){
                game.removeView(laser.getView());
                lasers.remove(laser);
            } else
                laser.update(delta);
        }
        ship.move(new Vector(50f, 1920-(BreathData.get(0)*1920)/1024f));
        ship.update(delta);

        start = System.currentTimeMillis();
    }

    @Override
    protected void init() {
        AppState.framelimit = AppState.Framelimit.Sixty;
        game = (RelativeLayout) findViewById(R.id.as_game_area);
        ship = initObject(new ImageView(this), R.drawable.ship, 1, new Vector(50f, 1920/2f), new Vector(50f, 1920f), 1200);
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
            lasers.add(initObject(new ImageView(this), R.drawable.laser, 2, ship.getPosition().clone(), new Vector(1080f, ship.getPosition().get(1)), 3000));
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
