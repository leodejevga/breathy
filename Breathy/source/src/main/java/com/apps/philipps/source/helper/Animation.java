package com.apps.philipps.source.helper;

import android.icu.text.TimeZoneFormat;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jevgenij Huebert on 06.09.2017, project ${PROJECT}, project Breathy. Project Breathy.
 */
public abstract class Animation {
    private Integer z;
    protected OnFinished finished;
    private final static String TAG = "Animation";
    public static SparseArray<List<Animation>> animations = new SparseArray<>();
    private static List<Animation> toRemove = new ArrayList<>();
    private static List<Integer> levels = new ArrayList<>();
    private static int[] removeAll;

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
        executeToRemove();

        for (int i : levels) {
            for (final Animation animation : animations.get(i)) {
                animation.update(delta);
            }
        }
    }

    private static void executeToRemove() {
        if (removeAll != null && removeAll.length == 0) {
            animations.clear();
            removeAll = null;
            toRemove.clear();
            levels.clear();
            return;
        }
        if (removeAll != null) {
            for (Integer i : removeAll)
                animations.get(i).clear();
            removeAll = null;
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
        int count = 0;
        for (int i = 0; i < levels.size(); i++)
            count += animations.get(levels.get(i)).size();
        return count;
    }

    /**
     * Marks this Animation as removed. Important! This Animation will be removed at the beginning of calling {@link #updateAnimations(double)}
     */
    @CallSuper
    public void remove() {
        if (finished != null)
            finished.run();
        toRemove.add(this);
    }

    public void setOnFinished(OnFinished finishedListener) {
        finished = finishedListener;
    }

    /**
     * Marks all Animations as removed. Important! The Animations will be removed at the beginning of calling {@link #updateAnimations(double)}
     */
    @CallSuper
    public static void removeAll() {
        removeAll = new int[0];
    }

    /**
     * Marks all Animations as removed. Important! The Animations will be removed at the beginning of calling {@link #updateAnimations(double)}
     */
    @CallSuper
    public static void removeAll(int... z) {
        removeAll = z;
    }


    /**
     * Representation of an Animation Object like this "ClassName : key=Number | Number Animations with keys=[key1, key2, ..., keyN]"
     *
     * @return String that represents this Object
     */
    @Override
    public String toString() {
        String ls = "";
        for (Integer l : levels)
            ls += ", " + l;
        return getClass().getSimpleName() + " : key=" + z + " | " + count() +
                " Animations with keys=[" + (ls.isEmpty() ? "" : ls.substring(2)) + "]";
    }


    public interface OnFinished {
        void run();
    }
}
