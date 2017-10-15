package com.apps.philipps.test;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper.Animated;
import com.apps.philipps.source.helper.Animation;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.GameObject2D;
import com.apps.philipps.test.activities.Game;

import java.util.Random;


/**
 * Created by Jevgenij Huebert on 05.09.2017. Project Breathy.
 */
public abstract class GOFactory {
    private static final String TAG = "Game Object Factory";
    public static Ship ship;

    public static void init(Context context, Vector position, ViewGroup game) {
        ship = new Ship(context, position, game);
        ship.remove();
    }


    public static class Ship extends Animation {
        private GameObject2D engine;
        private ViewGroup game;
        private GameObject2D o;

        public Ship(Context context, Vector position, ViewGroup game) {
            o = new GameObject2D(new ImageView(context), position);
            engine = new GameObject2D(new ImageView(context), position);
            ((ImageView) o.getView()).setImageResource(R.drawable.ship);
            game.addView(o.getView());
            ((ImageView) engine.getView()).setImageResource(R.drawable.engine);
            game.addView(engine.getView());
            engine.setPosition(o.getPosition().add(new Vector(-80, 4)));
            this.game = game;
        }

        @Override
        public void update(double delta) {
            o.update(delta);
            engine.getView().bringToFront();
            o.getView().bringToFront();
            engine.setPosition(o.getPosition().add(new Vector(-80, 2)));
            float value = 0.2f + (Math.abs(new Random().nextInt()) % 800f) / 1000f;
            engine.getView().setScaleY(value * 1.5f);


            double d = BreathData.get(0).data;
            double y = (d - AppState.breathyUserMin) / (AppState.breathyUserMax - AppState.breathyUserMin) * game.getHeight();
            double speed = Math.abs(o.getPosition().get(1) - y) * AppState.breathyDataFrequency;
            o.move(new Vector(o.getPosition().get(0), y), speed);
            o.getView().bringToFront();
        }
    }

    public static class Enemy extends Animation {
        private GameObject2D o;
        private GameObject2D engine;
        private ViewGroup game;
        private Game context;

        public Enemy(Game context, Vector position, ViewGroup game) {
            super(2);
            o = new GameObject2D(new ImageView(context), position, new Vector(-100, position.get(1)), GameStats.enemySpeed, true);
            this.game = game;
            this.context = context;
            game.addView(o.getView());
            o.getView().bringToFront();
            if (GameStats.aliens) {
                ((ImageView) o.getView()).setImageResource(R.drawable.alien);
            } else {
                ((ImageView) o.getView()).setImageResource(R.drawable.enemy);

                engine = new GameObject2D(context);
                ((ImageView) engine.getView()).setImageResource(R.drawable.engine);
                game.addView(engine.getView());
                engine.setPosition(o.getPosition().add(new Vector(-60, -2)));
            }
        }

        @Override
        protected void update(double delta) {
            o.update(delta);
            o.getView().bringToFront();
            if (!GameStats.aliens) {
                engine.getView().bringToFront();
                engine.setPosition(o.getPosition().add(new Vector(-60, -2)));
                float value = 0.2f + (Math.abs(new Random().nextInt()) % 800f) / 1000f;
                engine.getView().setScaleY(value * 1.5f);
            }
            if (!o.isMoving()) {
                remove();
            }
        }

        @Override
        public void remove() {
            super.remove();
            game.removeView(o.getView());
            if (!GameStats.aliens)
                game.removeView(engine.getView());
        }
    }

    public static class Laser extends Animation {
        private GameObject2D o;
        private ViewGroup game;
        private Game context;

        public Laser(Game context, int dest, ViewGroup game) {
            super(3);
            o = new GameObject2D(new ImageView(context), ship.o.getPosition().add(new Vector(110, 25)),
                    new Vector(dest, ship.o.getPosition().get(1) + 25), GameStats.shoot.getSpeed(), true);

            if (GameStats.shoot.id == 0)
                ((ImageView) o.getView()).setImageResource(R.drawable.laser_green);
            if (GameStats.shoot.id == 1)
                ((ImageView) o.getView()).setImageResource(R.drawable.laser_blue);
            if (GameStats.shoot.id == 2)
                ((ImageView) o.getView()).setImageResource(R.drawable.laser_red);
            game.addView(o.getView());
            this.game = game;
            this.context = context;

        }

        @Override
        protected void update(double delta) {
            o.update(delta);
            o.getView().bringToFront();
            if (!o.isMoving()) {
                game.removeView(o.getView());
                remove();
            } else {
                for (Animation ani : get(Goody.class)) {
                    Goody goody = (Goody) ani;
                    if (o.intersect(goody.o)) {
                        goody.activate();
                        game.removeView(goody.o.getView());
                        game.removeView(o.getView());
                        remove();
                        return;
                    }
                }
                for (Animation ani : get(Enemy.class)) {
                    Enemy enemy = (Enemy) ani;
                    if (o.intersect(enemy.o)) {
                        context.addCoin();
                        SoundManager.bang();
                        new GOFactory.Explosion(context, enemy, game);
                        if (new Random().nextInt() % 5 == 3)
                            new GOFactory.Goody(context, enemy, game, (new Random().nextInt() % 5 != 1));
                        game.removeView(enemy.o.getView());
                        game.removeView(o.getView());
                        remove();
                        enemy.remove();
                        break;
                    }
                }
            }
        }
    }

    public static class Star extends Animation {
        private GameObject2D o;
        private ViewGroup game;
        private Context context;

        public Star(Context context, Vector position, ViewGroup game) {
            super(0);
            o = new GameObject2D(new ImageView(context), position, new Vector(-100, position.get(1)), GameStats.starSpeed, true);
            if (GameStats.day)
                ((ImageView) o.getView()).setImageResource(R.drawable.cloud);
            else
                ((ImageView) o.getView()).setImageResource(R.drawable.star);

            game.addView(o.getView());
            this.game = game;
            this.context = context;
        }

        @Override
        protected void update(double delta) {
            o.update(delta);
            o.getView().bringToFront();
            if (!o.isMoving()) {
                remove();
                game.removeView(o.getView());
            }
        }
    }

    public static class Explosion extends Animation {
        private GameObject2D o;
        public long start = System.currentTimeMillis();
        private long past;
        private ViewGroup game;
        private Context context;

        public Explosion(Context context, Enemy enemy, final ViewGroup game) {
            super(4);
            o = new GameObject2D(new ImageView(context), enemy.o.getPosition().add(new Vector(-80, -80)), enemy.o.getDestination(), enemy.o.getSpeed(), true);
            ((ImageView) o.getView()).setImageResource(R.drawable.explosion);
            game.addView(o.getView());
            this.game = game;
            this.context = context;
        }

        @Override
        public void update(double delta) {
            o.update(delta);
            past += delta;
            o.getView().bringToFront();
            if (past > GameStats.explosionTime) {
                game.removeView(o.getView());
                remove();
                new Cloud(context, this, game);
            }
        }
    }

    public static class Cloud extends Animation {
        private GameObject2D o;
        private ViewGroup game;

        public Cloud(Context context, Explosion explosion, ViewGroup game) {
            super(5);
            o = new GameObject2D(new ImageView(context), explosion.o);
            ((ImageView) o.getView()).setImageResource(R.drawable.cloud);
            o.setPosition(o.getPosition().add(new Vector(20, 30)));
            o.setDestination(new Vector(-150, o.getPosition().Y));
            o.setSpeed(1000);
            game.addView(o.getView());
            this.game = game;
        }

        @Override
        protected void update(double delta) {
            o.update(delta);
            o.getView().bringToFront();
            if (!o.isMoving()) {
                remove();
                game.removeView(o.getView());
            }
        }
    }


    public static class Goody extends Animation {
        public GameStats.Effect effect;
        public int intersectableIn = 100;
        private long start = 0;
        private boolean good;
        private ViewGroup game;
        private Context context;
        private GameObject2D o;

        public Goody(Context context, Enemy enemy, ViewGroup game, boolean good) {
            super(6);
            o = new GameObject2D(new ImageView(context), enemy.o.clone());
            this.good = good;
            this.context = context;
            o.intercectable = false;
            effect = GameStats.Effect.getEffect(good);
            if (effect == GameStats.Effect.timeLoop)
                ((ImageView) o.getView()).setImageResource(R.drawable.g_time);
            else if (effect == GameStats.Effect.increaseShootSpeed)
                ((ImageView) o.getView()).setImageResource(R.drawable.g_laser_good);
            else if (effect == GameStats.Effect.decreaseEnemySpeed)
                ((ImageView) o.getView()).setImageResource(R.drawable.g_speed_good);
            else if (effect == GameStats.Effect.decreaseShootSpeed)
                ((ImageView) o.getView()).setImageResource(R.drawable.g_laser_bad);
            else if (effect == GameStats.Effect.increaseEnemySpeed)
                ((ImageView) o.getView()).setImageResource(R.drawable.g_speed_bad);
            else if (good)
                ((ImageView) o.getView()).setImageResource(R.drawable.goody_good);
            else
                ((ImageView) o.getView()).setImageResource(R.drawable.goody_bad);

            game.addView(o.getView());
            this.game = game;
        }

        public boolean activate() {
            o.getView().bringToFront();
            if (GameStats.timeLoopAnimation.getPosition().get(0) != 1 && effect == GameStats.Effect.timeLoop)
                effect = GameStats.Effect.increaseShootSpeed;
            boolean result = effect.activate();
            if (effect == GameStats.Effect.timeLoop) {
                start = System.currentTimeMillis();
                o.setPosition(new Vector(-100, -100));
                game.removeView(o.getView());
                o.stop();
                new LoopAnimationOn(context, game);
            } else remove();

            return result;
        }

        @Override
        public void update(double delta) {
            o.update(delta);
            o.getView().bringToFront();
            if (!o.intercectable) {
                o.intercectable = System.currentTimeMillis() - o.created >= intersectableIn;
            }
            if (start > 0) {
                long delay = System.currentTimeMillis() - start;
                if (delay >= GameStats.TIME_LOOP_DELAY) {
                    GameStats.timeLoopAnimation.move(new Vector(1));
                    new LoopAnimationOff(context, game);
                    remove();
                    return;
                }
            }
            if (!o.isMoving() && effect != GameStats.Effect.timeLoop)
                remove();
        }

        @Override
        public String toString() {
            return effect.name + " : " + super.toString();
        }
    }

    public static class Shoot extends Animation {
        private long past;
        private ViewGroup game;
        private Context context;
        private GameObject2D o;

        public Shoot(Context context, ViewGroup game) {
            super(4);
            o = new GameObject2D(new ImageView(context), ship.o.getPosition().add(90, -20));
            if (GameStats.shoot.id == 0)
                ((ImageView) o.getView()).setImageResource(R.drawable.shoot_greed);
            if (GameStats.shoot.id == 1)
                ((ImageView) o.getView()).setImageResource(R.drawable.shoot_blue);
            if (GameStats.shoot.id == 2)
                ((ImageView) o.getView()).setImageResource(R.drawable.shoot_red);
            game.addView(o.getView());
            this.game = game;
        }

        @Override
        public void update(double delta) {
            o.update(delta);
            o.getView().bringToFront();
            o.setPosition(ship.o.getPosition().add(90, -20));
            past += delta;
            if (past > GameStats.shootTime) {
                game.removeView(o.getView());
                remove();
            }
        }
    }
    //Animations_____________________


    public static class LoopAnimationOn extends Animation {
        private Animated fade = new Animated(new Vector(1), new Vector(0), 2, true);
        private RelativeLayout o;
        private ViewGroup game;

        public LoopAnimationOn(Context context, ViewGroup game) {
            super(7);
            this.game = game;
            o = new RelativeLayout(context);
            o.setLayoutParams(new ViewGroup.LayoutParams(game.getWidth(), game.getHeight()));
            this.game.addView(o);
        }

        @Override
        protected void update(double delta) {
            fade.update(delta);
            o.bringToFront();
            int c = (int) (fade.getPosition().get(0) * 255);
            int color = Color.argb(c, c, c, c);
            o.setBackgroundColor(color);
            if (!fade.isMoving()) {
                game.removeView(o);
                remove();
            }
        }
    }

    public static class LoopAnimationOff extends Animation {
        private Animated fade = new Animated(new Vector(0), new Vector(1), 2, true);
        private RelativeLayout o;
        private ViewGroup game;

        public LoopAnimationOff(Context context, ViewGroup game) {
            super(7);
            this.game = game;
            o = new RelativeLayout(context);
            o.setLayoutParams(new ViewGroup.LayoutParams(game.getWidth(), game.getHeight()));
            this.game.addView(o);
        }

        @Override
        protected void update(double delta) {
            fade.update(delta);
            o.bringToFront();
            int c = (int) (fade.getPosition().get(0) * 128);
            int color = Color.argb(c, c * 2, c * 2, 128 + c);
            o.setBackgroundColor(color);
            if (!fade.isMoving()) {
                game.removeView(o);
                remove();
            }
        }
    }
}