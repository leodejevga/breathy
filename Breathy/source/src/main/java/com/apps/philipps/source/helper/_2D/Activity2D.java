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
    protected final float SCREEN_FACTOR = ((float) (getScreenHeight() + getScreenWidth())) / (1080 + 1920);
    protected ViewGroup game;
    protected long delta = 0;
    Random random = new Random();



    private int coins = 0;

    private static final String TAG = "Activity 2D";
    private boolean draw;
    private int frameRate = 0;
    private int frame = 0;
    private boolean initialized = false;
    public final long start = System.currentTimeMillis();
    private short ready = 2;
    private static int thread = 0;

    protected void stopDrawing() {
        draw = false;
    }


    private final Runnable drawing = new Runnable() {
        private double millis;

        @Override
        public void run() {
            runOnUiThread(() -> {
                if (!initialized) {
                    init();
                    Log.e(TAG, "Initialized");
                    millis = 1000 / AppState.framelimit.value;
                }
                initialized = true;
            });
            while (draw) {
                if (initialized) {
                    delta = System.currentTimeMillis() - delta;
                    if (delta >= millis && ready >= 2) {
                        ready = 0;
                        executeDraw(delta);
                        Log.e(TAG, "Draw");
                        delta = System.currentTimeMillis();
                    } else {
                        try {
                            long sleep = 1000 / AppState.framelimit.value - delta;
                            if(sleep>2) {
                                Log.e(TAG, "Sleep for " + sleep + " ms");
                                Thread.sleep(sleep);
                            }
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Fail to wait");
                        }
                    }
                }
            }
        }
    };

    private void starToDraw() {
        draw = true;
        Thread drawThread = new Thread(null, drawing, "Activity 2D " + thread++);
        drawThread.setPriority(Thread.MAX_PRIORITY);
        drawThread.start();
    }

    protected int getInt(int from, int to){
        int result = Math.abs(random.nextInt());
        return (result+from)%to;
    }

    protected abstract void draw();

    protected abstract void init();

    protected abstract void touch(MotionEvent event);

    protected int getScreenWidth() {
        int result = Resources.getSystem().getDisplayMetrics().widthPixels;
        return result;
    }

    protected int getScreenHeight() {
        int result = Resources.getSystem().getDisplayMetrics().heightPixels;
        return result;
    }

    private long second = System.currentTimeMillis();

    private synchronized void executeDraw(long delta) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    PlanManager.update();
                    draw();
                    ready++;
                } catch (Exception e) {
                    Log.e(TAG, "Draw not successfull", e);
                }
                frame = ++frame;
            }
        });
        if (second + 1000 <= System.currentTimeMillis()) {
            frameRate = frame;
            frame = 0;
            second = System.currentTimeMillis();
        }
        ready++;
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

    private GameObject2D initObject(@DrawableRes int content) {
        return initObject(content, new Vector(0, 0), new Vector(0, 0), 0);
    }

    protected GameObject2D initObject(@DrawableRes int content, Vector position) {
        return initObject(content, position, new Vector(0, 0), 0);
    }

    protected GameObject2D initObject(@DrawableRes int content, Vector position, Vector destination, int move) {
        ImageView view = new ImageView(this);
        view.setImageResource(content);
        game.addView(view);
        GameObject2D result = new GameObject2D(view, position, destination);
        result.move(move);
        return result;
    }


    public void addCoin() {
        this.coins++;
    }

    public void addCoin(int amount) {
        this.coins += amount;
    }

    public void subCoin() {
        if (this.coins > 0)
            this.coins--;
    }

    public void subCoin(int amount) {
        if (this.coins - amount > 0)
            this.coins -= amount;
        else this.coins = 0;
    }

    public int getCoins() {
        return coins;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public int getFrame() {
        return frame;
    }
}
