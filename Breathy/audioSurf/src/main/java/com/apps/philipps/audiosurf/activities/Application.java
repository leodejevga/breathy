package com.apps.philipps.audiosurf.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apps.philipps.audiosurf.R;
import com.apps.philipps.source.helper.Activity2D;
import com.apps.philipps.source.helper.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Application extends Activity2D{
    ImageView ship;
    List<ImageView> enemies;
    List<ImageView> lasers;
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
        if(System.currentTimeMillis() - enemySpawn>2000){
            ImageView iv = new ImageView(this);
            iv.setImageResource(R.drawable.enemy);
            iv.setId(enemies.size());
            iv.setX(getScreenWidth());
            iv.setY(getScreenHeight()/2);
            game.addView(iv);
            enemies.add(iv);
            enemySpawn = System.currentTimeMillis();
        }

        for(ImageView enemy : enemies){
            Position position = new Position(enemy.getX(), enemy.getY());
            double delta = (System.currentTimeMillis()-start) * enemySpeed;
            position.sub(new Position(delta));
            if(position.compareTo(new Position())==-1) {
                game.removeView(enemy);
                enemies.remove(enemy);
            }
            else {
                enemy.setX((float) position.get(0));
                enemy.setY((float) position.get(1));
            }
        }

        start = System.currentTimeMillis();
    }

    @Override
    protected void init() {
        game = (RelativeLayout) findViewById(R.id.as_game_area);
        ship = (ImageView) findViewById(R.id.as_ship);
        enemies = new ArrayList<>();
        lasers = new ArrayList<>();
        start = System.currentTimeMillis();
        enemySpawn = start;
        enemySpeed = 0.05;
        random = new Random(5);
        if(game == null)
            brakeDraw();
    }

}
