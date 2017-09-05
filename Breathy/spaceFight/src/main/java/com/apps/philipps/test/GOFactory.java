package com.apps.philipps.test;

import android.content.Context;
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
            super(new ImageView(context), new Animated(ship.getPosition().clone().add(new Vector(30, 30)), new Vector(dest, ship.getPosition().get(1) + 50), speed, true));
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

        public Explosion(Context context, Enemy enemy, final ViewGroup game) {
            super(new ImageView(context), enemy.getAnimated().clone());
            ((ImageView) getView()).setImageResource(R.drawable.explosion);
            game.addView(getView());

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
        public int intercectableIn = 50;

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
            return effect.activate();
        }

        @Override
        public void update(long deltaMilliseconds) {
            super.update(deltaMilliseconds);
            if (!intercectable) {
                intercectable = System.currentTimeMillis() - created >= intercectableIn;
            }
        }
    }
}