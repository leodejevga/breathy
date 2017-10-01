package com.apps.philipps.source.helper;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.util.Log;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * Created by Leo on 06.09.2017.
 */

public abstract class Animations {
    private final static String TAG = "Animations";
    protected static List<Animations> animations = new ArrayList<>();
    private static List<Animations> toRemove = new ArrayList<>();
    private static boolean waitRemove = false;

    public Animations() {
        animations.add(this);
    }

    @CallSuper
    public static void updateAnimations(final double delta) {
        if (!toRemove.isEmpty()) {
            animations.removeAll(toRemove);
            toRemove.clear();
        }
        for (final Animations animation : animations) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    animation.update(delta);
                }
            });
        }

    }

    public static List<? extends Animations> get(Class clazz) {
        List result = new ArrayList<>();
        for (int i = 0; i < animations.size(); i++)
            if (animations.get(i).getClass() == clazz)
                result.add(animations.get(i));
        return result;
    }

    @MainThread
    protected abstract void update(double delta);

    public static int count() {
        return animations.size();
    }

    @CallSuper
    public void remove() {
        toRemove.add(this);
    }
    @CallSuper
    public static void removeAll() {
        toRemove.addAll(animations);
    }

    @Override
    public String toString() {
        return (getClass() != Animations.class ? getClass().getSimpleName() + " : total " : "") + count() + " Animations";
    }
}
