package com.apps.philipps.source.helper._2D;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.interfaces.IObserver;

import java.util.Random;

/**
 * Created by Jevgenij Huebert on 11.03.2017. Project Breathy
 */

public abstract class Activity2D extends Activity implements IObserver {
    protected final float SCREEN_FACTOR = (float) (getScreenHeight() + getScreenWidth()) / (1080 + 1920);

    protected static final String TAG = "Activity 2D";
    protected boolean draw;
    protected ViewGroup game;
    protected long delta;
    private int coins;

    private int frameRate = 0;
    private int frame = 0;
    private boolean initialized = false;
    private long start = System.currentTimeMillis();
    private boolean ready = true;
    private static int thread = 0;

    protected void stopDrawing() {
        draw = false;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public int getFrame() {
        return frame;
    }

    protected void addCoin() {
        coins++;
    }

    protected void subCoin() {
        coins--;
    }

    protected void addCoin(int coins) {
        this.coins += coins;
    }

    protected void subCoin(int coins) {
        this.coins -= coins;
        if (this.coins < 0)
            this.coins = 0;
    }

    public int getCoins() {
        return coins;
    }

    private final Runnable drawing = new Runnable() {
        private Long millis;

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
                    delta = System.currentTimeMillis() - start;
                    if (ready) {
                        ready = false;
                        executeDraw();
                        start = System.currentTimeMillis();
                    }
                }
            }
        }
    };

    private void starToDraw() {
        draw = true;
        Thread drawThread = new Thread(null, drawing, "Activity 2D " + thread++);
        drawThread.start();
    }

    protected abstract void draw();

    protected abstract void init();

    protected abstract void touch(MotionEvent event);

    /**
     *
     * Returns the width of the game Layout if initialized, else the width of the screen
     *
     * @param absolute
     * @return with of game Layout or screen in the second place
     */
    protected int getScreenWidth(boolean... absolute) {
        if (game != null && (absolute.length==0 || !absolute[0]))
            return game.getWidth();
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * Returns the height of the game Layout if initialized, else the width of the screen
     *
     * @param absolute
     * @return height of game Layout or screen in the second place
     */
    protected int getScreenHeight(boolean... absolute) {
        if (game != null && (absolute.length==0 || !absolute[0]))
            return game.getHeight();
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private long second = System.currentTimeMillis();

    private void executeDraw() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    PlanManager.update();
                    draw();
                } catch (Exception e) {
                    Log.e(TAG, "Draw not successfull", e);
                }
                frame++;
                if (second + 1000 <= System.currentTimeMillis()) {
                    frameRate = frame;
                    frame = 0;
                    second = System.currentTimeMillis();
                }
                ready = true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "OnPause");
        AppState.recordData = AppState.inGame = false;
        BreathData.removeObserver(this);
        BreathData.saveRest();
        draw = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume");
        AppState.recordData = AppState.inGame = true;
        BreathData.addObserver(this);
        starToDraw();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touch(event);
        return super.onTouchEvent(event);
    }

    protected GameObject2D initObject(@DrawableRes int content, Vector position, Vector destination, double move) {
        ImageView view = new ImageView(this);
        view.setImageResource(content);
        game.addView(view);
        GameObject2D result = new GameObject2D(view, position, destination);
        result.move(move);
        return result;
    }

    protected int getInt(int from, int to) {
        Random r = new Random();
        return (from + r.nextInt()) % to;
    }
}
