package com.apps.philipps.test;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apps.philipps.source.helper.Animated;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.GameObject2D;

/**
 * Created by Leo on 05.09.2017.
 */

public class GOFactory {

    public static class Enemy extends GameObject2D {
        public Enemy(Context context, Vector position, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(position, new Vector(-100, position.get(1)), speed, true));
            ((ImageView) getView()).setImageResource(R.drawable.enemy);
            getView().bringToFront();
            game.addView(getView());
        }
    }

    public static class Laser extends GameObject2D {
        public Laser(Context context, Ship ship, int dest, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(ship.getPosition().clone().add(new Vector(110, 25)), new Vector(dest, ship.getPosition().get(1) + 25), speed, true));

            if (GameStats.shoot.id == 0)
                ((ImageView) getView()).setImageResource(R.drawable.laser_green);
            if (GameStats.shoot.id == 1)
                ((ImageView) getView()).setImageResource(R.drawable.laser_blue);
            if (GameStats.shoot.id == 2)
                ((ImageView) getView()).setImageResource(R.drawable.laser_red);
            game.addView(getView());
        }
    }

    public static class Star extends GameObject2D {
        public Star(Context context, Vector position, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(position, new Vector(-10, position.get(1)), speed, true));
            ((ImageView) getView()).setImageResource(R.drawable.star);
            game.addView(getView());
        }
    }

    public static class Explosion extends GameObject2D {
        public long start = System.currentTimeMillis();
        private long past;
        private ViewGroup game;
        public boolean toRemove;

        public Explosion(Context context, Enemy enemy, final ViewGroup game) {
            super(new ImageView(context), new Animated(enemy.getPosition().clone().add(new Vector(-40, -40)), enemy.getAnimated().getEnd(), enemy.getAnimated().getSpeed(), true));
            ((ImageView) getView()).setImageResource(R.drawable.explosion);
            game.addView(getView());
            this.game = game;
        }

        @Override
        public void update(long deltaMilliseconds) {
            super.update(deltaMilliseconds);
            past += deltaMilliseconds;
            if (toRemove = past > GameStats.explosionTime)
                game.removeView(getView());
        }
    }

    public static class Ship extends GameObject2D {
        public Ship(Context context, Animated animated, ViewGroup game) {
            super(new ImageView(context), animated);
            ((ImageView) getView()).setImageResource(R.drawable.ship);
            game.addView(getView());
        }
    }

    public static class Goody extends GameObject2D {
        public GameStats.Effect effect;
        public int intersectableIn = 100;
        public long start = 0;
        public boolean toRemove = false;

        public Goody(Context context, Enemy enemy, ViewGroup game, boolean good) {
            super(new ImageView(context), enemy.getAnimated().clone());
            intercectable = false;
            if (good)
                ((ImageView) getView()).setImageResource(R.drawable.goody_good);
            else
                ((ImageView) getView()).setImageResource(R.drawable.goody_bad);
            effect = GameStats.Effect.getEffect(good);
            game.addView(getView());
        }

        public boolean activate() {
            boolean result = effect.activate();
            if (effect == GameStats.Effect.timeLoop)
                start = effect.value;
            else start = -1;
            return result;
        }

        @Override
        public void update(long deltaMilliseconds) {
            super.update(deltaMilliseconds);
            if (!intercectable) {
                intercectable = System.currentTimeMillis() - created >= intersectableIn;
            }
            long delta = System.currentTimeMillis() - start;
            if (start > 0 && delta >= GameStats.TIME_LOOP_DELAY) {
                GameStats.timeLoopAnimation.animate(new Vector(1));
                Log.e(TAG, "time loop deactivate : " + GameStats.timeLoopAnimation);
                start = -1;
            }
            if (start == -1 || !isMoving())
                toRemove = true;
        }
    }

    public static class Shoot extends GameObject2D {
        private long past;
        private ViewGroup game;
        public boolean toRemove;

        public Shoot(Context context, Ship ship, ViewGroup game) {
            super(new ImageView(context), new Animated(ship.getPosition().clone().add(90, -20),
                    ship.getAnimated().getEnd().clone().add(110, -20), ship.getAnimated().getSpeed(), true));
            if (GameStats.shoot.id == 0)
                ((ImageView) getView()).setImageResource(R.drawable.shoot_greed);
            if (GameStats.shoot.id == 1)
                ((ImageView) getView()).setImageResource(R.drawable.shoot_blue);
            if (GameStats.shoot.id == 2)
                ((ImageView) getView()).setImageResource(R.drawable.shoot_red);
            game.addView(getView());
            this.game = game;
        }

        @Override
        public void update(long deltaMilliseconds) {
            super.update(deltaMilliseconds);
            past += deltaMilliseconds;
            if (toRemove = past > GameStats.shootTime)
                game.removeView(getView());
            Log.e(TAG + ":" + getClass().getSimpleName(), toRemove + " to Remove");
        }
    }
}