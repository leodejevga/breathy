package com.apps.philipps.test;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apps.philipps.source.helper.Animated;
import com.apps.philipps.source.helper.Animations;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.GameObject2D;
import com.apps.philipps.test.activities.Game;

import java.util.List;

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
        public Laser(Context context, int dest, int speed, ViewGroup game) {
            super(new ImageView(context), new Animated(GameBuffer.ship.getPosition().clone().add(new Vector(110, 25)),
                    new Vector(dest, GameBuffer.ship.getPosition().get(1) + 25), speed, true));

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

        public Explosion(Context context, Enemy enemy, final ViewGroup game) {
            super(new ImageView(context), new Animated(enemy.getPosition().clone().add(new Vector(-80, -80)), enemy.getAnimated().getEnd(), enemy.getAnimated().getSpeed(), true));
            ((ImageView) getView()).setImageResource(R.drawable.explosion);
            game.addView(getView());
            this.game = game;
        }

        @Override
        public void update(double deltaMilliseconds) {
            super.update(deltaMilliseconds);
            past += deltaMilliseconds;
            if (past > GameStats.explosionTime) {
                game.removeView(getView());
                GameBuffer.remove(this);
            }
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
        private Context context;
        private ViewGroup game;

        public Goody(Context context, Enemy enemy, ViewGroup game, boolean good) {
            super(new ImageView(context), enemy.getAnimated().clone());
            this.context = context;
            intercectable = false;
            if (good)
                ((ImageView) getView()).setImageResource(R.drawable.goody_good);
            else
                ((ImageView) getView()).setImageResource(R.drawable.goody_bad);
            effect = GameStats.Effect.getEffect(good);
            game.addView(getView());
            this.game = game;
        }

        public boolean activate() {
            boolean result = effect.activate();
            if (effect == GameStats.Effect.timeLoop) {
                start = System.currentTimeMillis();
                new LoopAnimationsOn(context, game);
            }
            else start = -1;
            return result;
        }

        @Override
        public void update(double deltaMilliseconds) {
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
                GameBuffer.remove(this);
        }
    }

    public static class Shoot extends GameObject2D {
        private long past;
        private ViewGroup game;

        public Shoot(Context context, ViewGroup game) {
            super(new ImageView(context), new Animated(GameBuffer.ship.getPosition().clone().add(90, -20),
                    GameBuffer.ship.getPosition().clone().add(110, -20), 30, true));
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
        public void update(double deltaMilliseconds) {
            super.update(deltaMilliseconds);
            past += deltaMilliseconds;
            if (past > GameStats.shootTime) {
                game.removeView(getView());
                GameBuffer.remove(this);
            }
        }
    }
    //Animations_____________________


    public static class LoopAnimationsOn extends Animations {
        private Animated fade = new Animated(new Vector(1), new Vector(0), 2, true);
        private RelativeLayout surfaceView;
        private ViewGroup game;

        public LoopAnimationsOn(Context context, ViewGroup game) {
            this.game = game;
            surfaceView = new RelativeLayout(context);
            surfaceView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            int color = (int) (Color.WHITE * fade.getStart().get(0));
            surfaceView.setBackgroundColor(color);
            this.game.addView(surfaceView);
        }

        @Override
        protected void updateAnimation(double delta) {
            fade.update(delta);
            int color = (int) (Color.WHITE * fade.getStart().get(0));
            surfaceView.setBackgroundColor(color);
            if(!fade.isMoving()) {
                game.removeView(surfaceView);
                remove();
            }
        }
    }
}