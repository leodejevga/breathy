package com.apps.philipps.source.helper._2D;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.interfaces.IObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jevgenij Huebert on 11.03.2017. Project Breathy
 */
public abstract class Activity2D extends Activity implements IObserver {
    protected final float SCREEN_FACTOR = (float) (getScreenHeight(true) + getScreenWidth(true)) / (1080 + 1920);

    protected final String TAG = getClass().getSimpleName();
    protected boolean draw;
    protected ViewGroup game;
    protected long delta;
    private int coins;
    private boolean loadingReady;
    protected long currentTime = System.currentTimeMillis();

    private int frameRate = 0;
    private int frame = 0;
    private boolean initialized = false;
    private long frameTime = System.currentTimeMillis();
    protected final long start = System.currentTimeMillis();
    private boolean ready = true;
    private static int thread = 0;

    protected final void stopDrawing() {
        draw = false;
    }

    public final int getFrameRate() {
        return frameRate;
    }

    public final int getFrame() {
        return frame;
    }

    public final void addCoin() {
        coins++;
    }

    public final void subCoin() {
        subCoin(1);
    }

    protected final boolean isInitialized() {
        return initialized;
    }

    public final void addCoin(int coins) {
        this.coins += coins;
    }

    public final void subCoin(int coins) {
        this.coins -= coins;
        if (this.coins < 0)
            this.coins = 0;
    }

    public final int getCoins() {
        return coins;
    }

    private final Runnable drawing = new Runnable() {
        private Long millis;
        private int whiles = 0, ifs = 0, draws = 0;
        private long s = System.currentTimeMillis();

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!initialized) {
                        init();
                        millis = 1000L / 60L;
                    }
                    initialized = true;
                }
            });
            while (draw) {
                if (initialized) {
                    if (ready && System.currentTimeMillis() - frameTime > millis) {
                        draws++;
                        ready = false;
                        if (!loadingReady)
                            load(false);
                        else
                            executeDraw();
                    } else
                        ifs++;
                } else
                    whiles++;
                if (System.currentTimeMillis() - s > 1000) {
//                    Log.e(TAG, "draws " + draws + " ifs " + ifs + " whiles " + whiles);
                    whiles = ifs = draws = 0;
                    s = System.currentTimeMillis();
                }
            }
        }
    };

    private void load(final boolean firstLoad) {
        // TODO: 05.09.2017 Vernünftige Load schreiben sie muss wissen wann alles geladen wurde

        if (delta < 20 && delta != 0)
            loadingReady = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingReady)
                    onLoadingReady();
                else {
                    currentTime = System.currentTimeMillis();
                    delta = currentTime - frameTime;
                    int percent = (int) (System.currentTimeMillis() - start) / 60;
                    onLoading(firstLoad, percent > 100 ? 100 : percent, delta);
                }
                ready = true;
                frameTime = System.currentTimeMillis();
            }
        });
    }

    private long second = System.currentTimeMillis();

    private void executeDraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    PlanManager.update();
                    currentTime = System.currentTimeMillis();
                    delta = currentTime - frameTime;
                    draw();
//                    Log.e(TAG, "\nDraw time: " + (System.currentTimeMillis() - currentTime));
                } catch (Exception e) {
                    Log.e(TAG, "Draw not successfull", e);
                }
                frame++;
                if (second + 1000 <= currentTime) {
                    frameRate = frame;
                    frame = 0;
                    second = currentTime;
                }
                ready = true;
                frameTime = System.currentTimeMillis();
            }
        });
    }

    private void starToDraw() {
        draw = true;
        load(true);
        Thread drawThread = new Thread(null, drawing, TAG + " " + thread++);
        drawThread.start();
    }

    /**
     * You can remove your loading Screen here, or use the first draw() call to do this.
     */
    protected void onLoadingReady() {
    }

    /**
     * Show your loading screen here. This is kind of draw method, just for drawing Loading Screen.
     * Due to several background workings, this method might not be called 60 times every second.
     *
     * @param firstLoad true if this is the first onLoading(...) call
     * @param progress  progress of loading from 0 to 100;
     * @return true if this method called for the first time, otherwise false
     */
    protected abstract void onLoading(boolean firstLoad, int progress, long delta);

    /**
     * Draw your Frame
     */
    protected abstract void draw();

    /**
     * Initialize your game
     */
    protected abstract void init();

    /**
     * Your touch event
     *
     * @param event touch event
     */
    protected abstract void touch(MotionEvent event);

    /**
     * Returns the width of the game Layout if initialized, else the width of the screen
     *
     * @param absolute
     * @return with of game Layout or screen in the second place
     */
    protected final int getScreenWidth(boolean... absolute) {
        if (game != null && (absolute.length == 0 || !absolute[0]))
            return game.getWidth();
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * Returns the height of the game Layout if initialized, else the width of the screen
     *
     * @param absolute
     * @return height of game Layout or screen in the second place
     */
    protected final int getScreenHeight(boolean... absolute) {
        if (game != null && (absolute.length == 0 || !absolute[0]))
            return game.getHeight();
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "OnPause");
        BreathData.removeObserver(this);
        BreathData.saveRest();
        PlanManager.pause();
        AppState.recordData = AppState.inGame = false;
        draw = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "OnDestroy");
        PlanManager.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume");
        AppState.recordData = AppState.inGame = true;
        BreathData.addObserver(this);
        PlanManager.resume();
        starToDraw();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touch(event);
        return super.onTouchEvent(event);
    }

    /**
     * Use this Method to quickly initialize your GameObject2D
     *
     * @param content     id of your drawable content
     * @param position    position ov the Object
     * @param destination destination of the movement (can be null)
     * @param move        speed of the movement
     * @return your GameoObject2D
     */
    protected GameObject2D initObject(@DrawableRes int content, Vector position, Vector destination, double move) {
        ImageView view = new ImageView(this);
        view.setImageResource(content);
        game.addView(view);
        GameObject2D result = new GameObject2D(view, position, destination);
        result.move(move);
        return result;
    }

    /**
     * Get random int from to
     *
     * @param from frameTime of values
     * @param to   limit of values (from 0 to 2 the Values are [0,1,2])
     * @return random int from to
     */
    protected final int getInt(int from, int to) {
        Random r = new Random();
        return (from + Math.abs(r.nextInt())) % to + 1;
    }
}
