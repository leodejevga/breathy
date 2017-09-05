package com.apps.philipps.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Leo on 05.09.2017.
 */

public class GameStats {

    public static final String TAG = "Game Constants";
    private static final int[] ENEMY_COME_MIN_MAX = {100, 300};
    private static final int[] ENEMY_SPEED_MIN_MAX = {200, 500};
    private static boolean initialized = false;

    public static int enemyCome;
    public static int starCome;
    public static int enemySpeed;
    public static int shipSpeed;
    public static int cloudSpeed;
    public static int explosionTime;
    public static ShootSpeed shoot = ShootSpeed.Green;

    public static void init() {
        enemyCome = 200;
        starCome = 20;
        enemySpeed = 300;
        shipSpeed = 1000;
        cloudSpeed = 160;
        explosionTime = 50;
        shoot = ShootSpeed.Green;
        initialized = true;
    }

    public enum Effect {
        increaseShootSpeed(true, "Increase shoot_red speed", 0),
        decreaseShootSpeed(false, "Decrease shoot_red speed", 0),
        increaseEnemySpeed(false, "Increase enemy speed", 50),
        decreaseEnemySpeed(true, "Decrease enemy speed", 50),
        increaseEnemyCome(false, "Increase enemy come", -30),
        decreaseEnemyCome(true, "Decrease enemy come", -30);

        public final boolean good;
        public final String name;
        public final int value;

        Effect(boolean good, String name, int value) {
            this.good = good;
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }

        public static List<Effect> getKind(boolean good) {
            List<Effect> result = new ArrayList<>();
            for (Effect e :
                    values()) {
                if (e.good == good)
                    result.add(e);
            }
            return result;
        }

        public static Effect getEffect(boolean good) {
            Random r = new Random();
            int i = Math.abs(r.nextInt()) % 2;
            return getKind(good).get(i);
        }

        public boolean activate() {
            boolean result;
            if (initialized) {
                switch (this) {
                    case increaseShootSpeed:
                        ShootSpeed succ = shoot;
                        shoot = shoot.succ();
                        return succ != shoot;
                    case decreaseShootSpeed:
                        ShootSpeed prev = shoot;
                        shoot = shoot.prev();
                        return prev != shoot;
                    case increaseEnemySpeed:
                        enemySpeed += value;
                        enemySpeed = (result = enemySpeed > ENEMY_SPEED_MIN_MAX[1]) ? ENEMY_SPEED_MIN_MAX[1] : enemySpeed;
                        return !result;
                    case decreaseEnemySpeed:
                        enemySpeed -= value;
                        enemySpeed = (result = enemySpeed < ENEMY_SPEED_MIN_MAX[0]) ? ENEMY_SPEED_MIN_MAX[0] : enemySpeed;
                        return result;
                    case increaseEnemyCome:
                        enemyCome += value;
                        enemyCome = (result = enemyCome > ENEMY_COME_MIN_MAX[1]) ? ENEMY_COME_MIN_MAX[1] : enemyCome;
                        return result;
                    case decreaseEnemyCome:
                        enemyCome -= value;
                        enemyCome = (result = enemyCome < ENEMY_COME_MIN_MAX[0]) ? ENEMY_COME_MIN_MAX[0] : enemyCome;
                        return result;
                }
            }
            return false;
        }
    }

    public static enum ShootSpeed {
        None(-1, Integer.MAX_VALUE, 0),
        Green(0, 100, 5000),
        Blue(1, 80, 5400),
        Red(2, 60, 6000);

        public final int rate;
        public final int id;
        private int speed;

        ShootSpeed(int id, int rate, int speed) {
            this.id = id;
            this.rate = rate;
            this.speed = speed;
        }

        public void setFactor(double factor) {
            speed *= factor;
        }

        public int getSpeed() {
            return speed;
        }

        public static ShootSpeed get(int id) {
            if (id >= values().length - 1)
                return Red;
            if (id < 0)
                return Blue;
            for (ShootSpeed e : values())
                if (e.id == id)
                    return e;
            return None;
        }

        public ShootSpeed succ() {
            return get(id + 1);
        }

        public ShootSpeed prev() {
            return get(id - 1);
        }
    }
}
