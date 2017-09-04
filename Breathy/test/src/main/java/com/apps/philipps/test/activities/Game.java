package com.apps.philipps.test.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.BreathInterpreter;
import com.apps.philipps.source.Coins;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.helper.Animated;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.Activity2D;
import com.apps.philipps.source.helper._2D.GameObject2D;
import com.apps.philipps.test.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends Activity2D {
    private Buffer buffer;
    private long enemySpawned;
    private TextView finisched;
    private TextView secondsLeft;
    private BreathInterpreter.BreathStatus status;
    private long cloudSpawned;
    private long start = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_game);
    }

    @Override
    protected void draw() {
        if (!PlanManager.isActive()) {
            stopDrawing();
            Coins.addCoins(getCoins());
            finisched.setVisibility(View.VISIBLE);
        }
        long seconds = PlanManager.getDuration() / 1000;
        String left = (seconds / 60 != 0 ? (seconds / 60) + ":" : "")
                + (seconds != 0 ? seconds % 60 + ":" : "")
                + PlanManager.getDuration() % 1000;
        secondsLeft.setText(left + "\n" + getCoins() + " Coins\n" + getFrameRate() + " Frame Rate");

        List<GameObject2D> toRemove = new ArrayList<>();

        for (GameObject2D object : buffer) {
            object.update(delta);
            if (object instanceof Ship && status != null && BreathData.get(0) != null) {
                double d = BreathData.get(0).data;
                double y = (d - AppState.breathyUserMin) / (AppState.breathyUserMax - AppState.breathyUserMin) * getScreenHeight();
                object.move(new Vector(object.getPosition().get(0), -y));
                object.getView().bringToFront();
            } else if (object instanceof Enemy && !object.isMoving()) {
                game.removeView(object.getView());
                subCoin(10);
                toRemove.add(object);
            } else if (object instanceof Cloud && !object.isMoving()) {
                game.removeView(object.getView());
                toRemove.add(object);
            } else if (object instanceof Laser) {
                if (!object.isMoving()) {
                    game.removeView(object.getView());
                    toRemove.add(object);
                } else {
                    for (Enemy enemy : buffer.enemies) {
                        if (object.intersect(enemy)) {
                            game.removeView(enemy.getView());
                            game.removeView(object.getView());
                            toRemove.add(object);
                            toRemove.add(enemy);
                            addCoin();
                        }
                    }
                }
            }
            if (object instanceof Cloud && !object.isMoving()) {
                game.removeView(object.getView());
                toRemove.add(object);
            }
        }
        buffer.removeAll(toRemove);
        if ((System.currentTimeMillis() - enemySpawned) > buffer.enemyCome) {
            enemySpawned = System.currentTimeMillis();
            buffer.enemies.add(new Enemy(this, new Vector(getScreenWidth(), getEnemyY()), buffer.enemySpeed, game));
        }
        if ((System.currentTimeMillis() - cloudSpawned) > buffer.cloudCome + getInt(0, 1000)) {
            cloudSpawned = System.currentTimeMillis();
            int y = getInt(0, getScreenHeight());
            buffer.clouds.add(new Cloud(this, new Vector(getScreenWidth(), y), buffer.cloudSpeed, game));
        }
    }

    @Override
    protected void init() {
        PlanManager.start();
        game = (RelativeLayout) findViewById(R.id.test_game2d);
        buffer = new Buffer(new Ship(this, new Animated(new Vector(50, getScreenHeight() / 2)), game), SCREEN_FACTOR);
        finisched = (TextView) findViewById(R.id.finished);
        secondsLeft = (TextView) findViewById(R.id.seconds_left);
    }

    @Override
    protected void touch(MotionEvent event) {
        if (finisched.getVisibility() == View.VISIBLE)
            finish();
        buffer.laser.add(new Laser(this, buffer.ship, getScreenWidth(), buffer.laserSpeed, game));
    }

    @Override
    public void call(Object... messages) {
        if (messages.length == 2)
            status = (BreathInterpreter.BreathStatus) messages[1];
    }

    private static class Buffer implements Iterable<GameObject2D> {
        private int enemyCome = 200;
        private int cloudCome = 5502;
        private int enemySpeed = 500;
        private int shipSpeed = 1000;
        private int laserSpeed = 4000;
        private int cloudSpeed = 60;

        private List<Enemy> enemies = new ArrayList<>();
        private List<Laser> laser = new ArrayList<>();
        private List<Cloud> clouds = new ArrayList<>();
        private Ship ship;

        public Buffer(Ship ship, double screenFactor) {
            this.ship = ship;
            enemySpeed *= screenFactor;
            shipSpeed *= screenFactor;
            laserSpeed *= screenFactor;
            this.ship.move(shipSpeed);
        }


        private boolean remove(GameObject2D object) {
            if (object instanceof Enemy)
                return enemies.remove(object);
            if (object instanceof Laser)
                return laser.remove(object);
            if (object instanceof Cloud)
                return clouds.remove(object);
            return false;
        }

        public void removeAll(List<GameObject2D> toRemove) {
            for (GameObject2D object : toRemove) {
                remove(object);
            }
        }

        @Override
        public Iterator<GameObject2D> iterator() {
            return new Iterator<GameObject2D>() {
                private int iE;
                private int iC;
                private int iL;
                private int iS;
                private GameObject2D next;

                @Override
                public boolean hasNext() {
                    if (enemies.size() > iE || clouds.size() > iC || laser.size() > iL || iS == 0) {
                        if (iE < enemies.size())
                            next = enemies.get(iE++);
                        else if (iC < clouds.size())
                            next = clouds.get(iC++);
                        else if (iL < laser.size())
                            next = laser.get(iL++);
                        else if (iS++ == 0)
                            next = ship;
                        else next = null;
                    } else next = null;
                    return next != null;
                }

                @Override
                public GameObject2D next() {
                    return next;
                }

                @Override
                public void remove() {
                    if (next instanceof Laser) {
                        laser.remove(next);
                        iL--;
                    }
                    if (next instanceof Cloud) {
                        clouds.remove(next);
                        iC--;
                    }
                    if (next instanceof Enemy) {
                        enemies.remove(next);
                        iE--;
                    }
                }
            };
        }
    }

    private static class Enemy extends GameObject2D {
        public Enemy(Context context, Vector position, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(position, new Vector(-100, position.get(1)), speed, true));
            ((ImageView) getView()).setImageResource(R.drawable.enemy);
            getView().bringToFront();
            game.addView(getView());
        }
    }

    private static class Laser extends GameObject2D {
        public Laser(Context context, Ship ship, int dest, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(ship.getPosition().clone().add(new Vector(50, 5)), new Vector(dest, ship.getPosition().get(1)), speed, true));
            ((ImageView) getView()).setImageResource(R.drawable.laser);
            game.addView(getView());
        }
    }

    private static class Cloud extends GameObject2D {
        public Cloud(Context context, Vector position, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(position, new Vector(-position.get(0), position.get(1)), speed, true));
            ((ImageView) getView()).setImageResource(R.drawable.cloud);
            game.addView(getView());
        }
    }

    private static class Ship extends GameObject2D {
        public Ship(Context context, Animated animated, ViewGroup game) {
            super(new ImageView(context), animated);
            ((ImageView) getView()).setImageResource(R.drawable.ship);
            game.addView(getView());
        }
    }

    private int getEnemyY() {
        double result = 0;
        if (PlanManager.getCurrentPlan() != null) {
            double frequency = PlanManager.getCurrentPlan().getFrequency() / 60f;
            double delta = System.currentTimeMillis() - start;
            double value = (delta / 1000 * Math.PI * frequency);
            int height = buffer.enemies.size() != 0 ? buffer.enemies.get(0).getView().getMeasuredHeight() : 0;
            result = (Math.sin(value) + 1) / 2 * ((double) getScreenHeight() - height);
        }
        return (int) result;
    }

}