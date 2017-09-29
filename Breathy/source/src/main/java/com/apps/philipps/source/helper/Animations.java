package com.apps.philipps.source.helper;

import android.support.annotation.CallSuper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 06.09.2017.
 */

public abstract class Animations {
    public static List<Animations> animations = new ArrayList<>();


    public Animations() {
        animations.add(this);
    }

    public static void update(double delta) {
        synchronized (animations) {
            for (Animations animation : animations) {
                animation.updateAnimation(delta);
            }
        }
    }

    protected abstract void updateAnimation(double delta);

    @CallSuper
    protected boolean remove() {
        return animations.remove(this);
    }
}
