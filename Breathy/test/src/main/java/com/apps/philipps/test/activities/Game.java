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
        secondsLeft.setText(left + "\n" + getCoins() + " Coins\n" + getFrameRate() + " Framerate");
        for (GameObject2D object : buffer) {
            object.update(delta);
            if (object instanceof Ship && status != null && BreathData.get(0) != null) {
                Float y = BreathData.get(0).data / AppState.breathyUserMax * getScreenHeight();
                object.move(new Vector(object.getPosition().get(0), y));
            } else if (object instanceof Enemy && !object.isMoving()) {
                game.removeView(object.getView());
                subCoin(10);
                buffer.remove(object);
            } else if (object instanceof Cloud && !object.isMoving()) {
                game.removeView(object.getView());
                buffer.remove(object);
            } else if (object instanceof Laser) {
                if (!object.isMoving()) {
                    game.removeView(object.getView());
                    buffer.remove(object);
                } else {
                    for (Enemy enemy : buffer.enemies) {
                        if (object.intersect(enemy)) {
                            game.removeView(enemy.getView());
                            game.removeView(object.getView());
                            buffer.remove(enemy);
                            buffer.remove(object);
                            addCoin();
                        }
                    }
                }
            }
            if (object instanceof Cloud && !object.isMoving()) {
                game.removeView(object.getView());
                buffer.remove(object);
            }
        }
        if ((System.currentTimeMillis() - enemySpawned) > buffer.enemyCome) {
            enemySpawned = System.currentTimeMillis();
            buffer.enemies.add(new Enemy(this, new Vector(getScreenWidth(), getInt(0, getScreenHeight())), buffer.enemySpeed, game));
        }
        if ((System.currentTimeMillis() - cloudSpawned) > buffer.cloudCome + getInt(0, 1000)) {
            cloudSpawned = System.currentTimeMillis();
            int y = getInt(0, getScreenHeight());
            buffer.clouds.add(new Cloud(this, new Vector(getScreenWidth(), y), buffer.cloudSpeed, game));
        }
    }

    @Override
    protected void init() {
        PlanManager.startPlan();
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
        if (messages.length == 2) {
            status = (BreathInterpreter.BreathStatus) messages[1];
            BreathData.Element element = (BreathData.Element) messages[0];
            Float y = element.data / AppState.breathyUserMax * getScreenHeight();
            buffer.ship.move(new Vector(50, y), buffer.shipSpeed);
        }
    }

    private static class Buffer implements Iterable<GameObject2D> {
        private int enemyCome = 565;
        private int cloudCome = 3502;
        private int enemySpeed = 100;
        private int shipSpeed = 2000;
        private int laserSpeed = 3000;
        private int cloudSpeed = 50;
        private Iterator<GameObject2D> iterrator;

        private List<Enemy> enemies = new ArrayList<>();
        private List<Laser> laser = new ArrayList<>();
        private List<Cloud> clouds = new ArrayList<>();
        private Ship ship;

        public Buffer(Ship ship, double screenFactor) {
            this.ship = ship;
            enemySpeed *= screenFactor;
            shipSpeed *= screenFactor;
            laserSpeed *= screenFactor;
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

        @Override
        public Iterator<GameObject2D> iterator() {
            return new Iterator<GameObject2D>() {
                private int iE;
                private int iC;
                private int iL;
                private int iS;

                @Override
                public boolean hasNext() {
                    if (enemies.size() > iE || clouds.size() > iC || laser.size() > iL || iS == 0)
                        return true;
                    return false;

                }

                @Override
                public GameObject2D next() {
                    if (iE < enemies.size())
                        return enemies.get(iE++);
                    if (iC < clouds.size())
                        return clouds.get(iC++);
                    if (iL < laser.size())
                        return laser.get(iL++);
                    if (iS++ == 0)
                        return ship;
                    return null;
                }
            };
        }
    }

    private static class Enemy extends GameObject2D {
        public Enemy(Context context, Vector position, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(position, new Vector(-50, position.get(1)), speed, true));
            ((ImageView) getView()).setImageResource(R.drawable.enemy);
            game.addView(getView());
        }
    }

    private static class Laser extends GameObject2D {
        public Laser(Context context, Ship ship, int dest, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(ship.getPosition().clone(), new Vector(dest, ship.getPosition().get(1)), speed, true));
            ((ImageView) getView()).setImageResource(R.drawable.laser);
            game.addView(getView());
        }
    }

    private static class Cloud extends GameObject2D {
        public Cloud(Context context, Vector position, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(position, new Vector(-50, position.get(1)), speed, true));
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

}