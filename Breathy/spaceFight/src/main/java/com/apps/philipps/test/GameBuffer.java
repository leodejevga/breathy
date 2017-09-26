package com.apps.philipps.test;

import android.util.Log;
import android.view.ViewGroup;

import com.apps.philipps.source.helper._2D.GameObject2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Leo on 05.09.2017.
 */

public class GameBuffer implements Iterable<GameObject2D> {


    protected final String TAG = getClass().getSimpleName();
    public List<GOFactory.Enemy> enemies = new ArrayList<>();
    public List<GOFactory.Explosion> explosions = new ArrayList<>();
    public List<GOFactory.Laser> laser = new ArrayList<>();
    public List<GOFactory.Star> stars = new ArrayList<>();
    public List<GOFactory.Goody> goodies = new ArrayList<>();
    public GOFactory.Shoot shoot;
    public GOFactory.Ship ship;
    public boolean initialized = false;

    public GameBuffer(GOFactory.Ship ship, double screenFactor) {
        this.ship = ship;
        GameStats.enemySpeed *= screenFactor;
        GameStats.shipSpeed *= screenFactor;
        GameStats.shoot.setFactor(screenFactor);
        this.ship.move(GameStats.shipSpeed);
        initialized = true;
    }


    public boolean remove(GameObject2D object) {
        if (object instanceof GOFactory.Enemy)
            return enemies.remove(object);
        if (object instanceof GOFactory.Explosion)
            return explosions.remove(object);
        if (object instanceof GOFactory.Laser)
            return laser.remove(object);
        if (object instanceof GOFactory.Shoot) {
            shoot = null;
            return true;
        }
        if (object instanceof GOFactory.Star)
            return stars.remove(object);
        if (object instanceof GOFactory.Goody)
            return goodies.remove(object);
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
            private int iE = enemies.size();
            private int iS = stars.size();
            private int iL = laser.size();
            private int iP = ship!=null?1:0;
            private int iSh = shoot!=null?1:0;
            private int iG = goodies.size();
            private int iEx = explosions.size();
            private GameObject2D next;

            @Override
            public boolean hasNext() {
                if (iE + iS + iL + iP + iG + iEx + iSh != 0) {
                    if (iP == 1) {
                        next = ship;
                        iP--;
                    } else if (iSh == 1) {
                        next = shoot;
                        iSh--;
                    }
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
        }

                ;
    }

    public void clear(ViewGroup game) {
        for (GameObject2D o : this) {
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
