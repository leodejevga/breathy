package com.apps.philipps.source.helper;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 06.09.2017, project ${PROJECT}, project Breathy. Project Breathy.
 */
public abstract class Animation {
    private Integer z;
    private final static String TAG = "Animation";
    private static SparseArray<List<Animation>> animations = new SparseArray<>();
    private static List<Animation> toRemove = new ArrayList<>();
    private static List<Integer> levels = new ArrayList<>();
    private static boolean removeAll = false;

    /**
     * Initializes Animation with z = 0
     */
    public Animation() {
        this(0);
    }

    /**
     * Initializes Animation with a specific z value. Higher z value will be updated at the end, because
     * you can bring them to front and they will appear at the top of other animations.
     *
     * @param z the hierarchical value of this animation
     */
    public Animation(@NonNull Integer z) {
        this.z = z;
        List<Animation> level = animations.get(z);
        if (level == null) {
            levels.add(z);
            Collections.sort(levels, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            level = new ArrayList<>();
            animations.append(z, level);
        }
        level.add(this);
    }

    /**
     * Removes all marked Animations and than update remained Animations
     *
     * @param delta Time past after the last update
     */
    @CallSuper
    public static void updateAnimations(final double delta) {
        if (removeAll) {
            animations.clear();
            removeAll = false;
            toRemove.clear();
            levels.clear();
            return;
        }
        if (!toRemove.isEmpty()) {
            for (Animation ani : toRemove) {
                List<Animation> anis = animations.get(ani.z);
                if (anis != null) {
                    anis.remove(ani);
                    if (anis.isEmpty()) {
                        levels.remove(ani.z);
                        animations.remove(ani.z);
                    }
                }
            }
            toRemove.clear();
        }
        for (int i : levels) {
            for (final Animation animation : animations.get(i)) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        animation.update(delta);
                    }
                });
            }
        }
    }

    /**
     * Returns list of of all Animations with the given z value
     *
     * @param z the hierarchical value of this animation
     * @return List of values with the given z value
     */
    public static List<Animation> get(int z) {
        return animations.get(z);
    }

    /**
     * Returns all animations of a specific Class
     *
     * @param clazz Class of Animation
     * @return List of those Animations
     */
    public static List<Animation> get(Class clazz) {
        List<Animation> result = new ArrayList<>();
        for (int i : levels)
            for (int j = 0; j < animations.get(i).size(); j++)
                if (animations.get(i).get(j).getClass() == clazz)
                    result.add(animations.get(i).get(j));
        return result;
    }

    /**
     * Update your Animation here
     *
     * @param delta Time past after the last update
     */
    @MainThread
    protected abstract void update(double delta);

    /**
     * Animations count
     *
     * @return amount of created Animations
     */
    public static int count() {
        return animations.size();
    }

    /**
     * Marks this Animation as removed. Important! This Animation will be removed at beginning by calling {@link #updateAnimations(double)}
     */
    @CallSuper
    public void remove() {
        toRemove.add(this);
    }

    /**
     * Marks all Animations as removed. Important! The Animations will be removed at beginning by calling {@link #updateAnimations(double)}
     */
    @CallSuper
    public static void removeAll() {
        removeAll = true;
    }


    @Override
    public String toString() {
        String ls = "";
        for (Integer l : levels)
            ls += ", " + l;
        return getClass().getSimpleName() + " : z=" + z + " | " + count() +
                " Animations with keys=[" + (ls.isEmpty() ? "" : ls.substring(2)) + "]";
    }
}
