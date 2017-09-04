package com.apps.philipps.test.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.philipps.test.R;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper._2D.Activity2D;
import com.apps.philipps.source.helper._2D.GameObject2D;
import com.apps.philipps.source.helper.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Application extends Activity2D {
    private GameObject2D ship;
    private List<GameObject2D> enemies;
    private List<GameObject2D> lasers;
    private double enemySpeed;
    private long enemySpawn;
    private Random random;
    private TextView rate;
    private TextView views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_application);
    }

    @Override
    protected void draw() {
        views.setText("Views on screen: " + (enemies.size() + lasers.size() + 3));
        rate.setText("Frame rate: " + getFrameRate());
        if (System.currentTimeMillis() - enemySpawn > 50) {
            int y = Math.abs(random.nextInt()) % (int) getScreenHeight(); // Über 500 bewegliche Objekte können gezeichnet werden sodass HTC M8 immer noch bei 30 frames per Seconds läuft. Über 700 bei 20 fps
            enemies.add(initObject(R.drawable.enemy, new Vector(1000f, (float) y), new Vector(50f, (float) y), (int) (320 * SCREEN_FACTOR)));
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
            if (!removed && !enemies.get(i).isMoving()) {
                enemies.get(i).setPosition(new Vector(1000f, enemies.get(i).getPosition().get(1)));
                enemies.get(i).move(new Vector(50f, enemies.get(i).getPosition().get(1)), (int) (320 * SCREEN_FACTOR));
            } else if (!removed)
                enemies.get(i).update(delta);

        }
        for (int i = 0; i < lasers.size(); i++) {
            if (!lasers.get(i).isMoving()) {
                game.removeView(lasers.get(i).getView());
                lasers.remove(lasers.get(i));
            } else
                lasers.get(i).update(delta);
        }
        if (BreathData.get(0) != null) {
            ship.move(new Vector(50f, getScreenHeight() - (BreathData.get(0).data * getScreenHeight()) / 1024f));
            ship.update(delta);
        }
    }

    @Override
    protected void init() {
        views = (TextView) findViewById(R.id.app_views);
        rate = (TextView) findViewById(R.id.app_framelimit);
        game = (RelativeLayout) findViewById(R.id.test_game_area);
        ship = initObject(R.drawable.player, new Vector(50f, getScreenHeight() / 2f), new Vector(50f, getScreenHeight()), (int) (1200 * SCREEN_FACTOR));
        enemies = new ArrayList<>();
        lasers = new ArrayList<>();
        enemySpawn = System.currentTimeMillis();
        enemySpeed = 0.05;
        random = new Random(5);
        if (game == null)
            stopDrawing();
    }

    @Override
    protected void touch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            lasers.add(initObject(R.drawable.laser, ship.getPosition().clone(), new Vector(getScreenWidth(), ship.getPosition().get(1)), (int) (10000 * SCREEN_FACTOR)));
    }

    @Override
    public void call(Object... messages) {

    }
}
