package com.apps.philipps.test;

import android.util.Log;
import android.view.ViewGroup;

import com.apps.philipps.source.helper._2D.GameObject2D;
import com.apps.philipps.test.activities.Game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Leo on 05.09.2017.
 */

public abstract class GameBuffer {


    protected final String TAG = getClass().getSimpleName();
    public static List<GOFactory.Enemy> enemies = new ArrayList<>();
    public static List<GOFactory.Explosion> explosions = new ArrayList<>();
    public static List<GOFactory.Laser> laser = new ArrayList<>();
    public static List<GOFactory.Star> stars = new ArrayList<>();
    public static List<GOFactory.Goody> goodies = new ArrayList<>();
    public static List<GOFactory.Shoot> shoot = new ArrayList<>();
    public static GOFactory.Ship ship;
    public static boolean initialized = false;

    public static void init(GOFactory.Ship ship, double screenFactor) {
        GameBuffer.ship = ship;
        GameStats.enemySpeed *= screenFactor;
        GameStats.shipSpeed *= screenFactor;
        GameStats.shoot.setFactor(screenFactor);
        GameBuffer.ship.move(GameStats.shipSpeed);
        initialized = true;
    }


    public static boolean remove(GameObject2D object) {
        if (object instanceof GOFactory.Enemy)
            return enemies.remove(object);
        if (object instanceof GOFactory.Explosion)
            return explosions.remove(object);
        if (object instanceof GOFactory.Laser)
            return laser.remove(object);
        if (object instanceof GOFactory.Shoot)
            return shoot.remove(object);
        if (object instanceof GOFactory.Star)
            return stars.remove(object);
        if (object instanceof GOFactory.Goody)
            return goodies.remove(object);
        return false;
    }

    public static void removeAll(List<GameObject2D> toRemove) {
        for (GameObject2D object : toRemove) {
            remove(object);
        }
    }

    public static Iterable<GameObject2D> iterable() {
        return new Iterable<GameObject2D>() {
            @Override
            public Iterator<GameObject2D> iterator() {
                return new Iterator<GameObject2D>() {

                    private int iE = enemies.size();
                    private int iS = stars.size();
                    private int iL = laser.size();
                    private int iP = ship != null ? 1 : 0;
                    private int iSh = shoot.size();
                    private int iG = goodies.size();
                    private int iEx = explosions.size();
                    private GameObject2D next;

                    @Override
                    public boolean hasNext() {
                        if (iE + iS + iL + iP + iG + iEx + iSh != 0) {
                            if (iP == 1) {
                                next = ship;
                                iP--;
                            } else if (iSh >= 1)
                                next = shoot.get(--iSh);
                            else if (iEx >= 1)
                                next = explosions.get(--iEx);
                            else if (iG >= 1)
                                next = goodies.get(--iG);
                            else if (iL >= 1)
                                next = laser.get(--iL);
                            else if (iE >= 1)
                                next = enemies.get(--iE);
                            else if (iS >= 1)
                                next = stars.get(--iS);
                            else
                                next = null;
                        } else next = null;
                        return next != null;
                    }

                    @Override
                    public GameObject2D next() {
                        return next;
                    }
                };
            }
        };
    }

    public static void clear(ViewGroup game) {
        for (GameObject2D o : GameBuffer.iterable()) {
            game.removeView(o.getView());
        }
        laser.clear();
        stars.clear();
        explosions.clear();
        ship = null;
        enemies.clear();
        initialized = false;
    }
}
