package com.apps.philipps.test.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.BreathInterpreter;
import com.apps.philipps.source.Coins;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.helper.Animations;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.Activity2D;
import com.apps.philipps.source.helper._2D.GameObject2D;
import com.apps.philipps.test.GOFactory;
import com.apps.philipps.test.GameStats;
import com.apps.philipps.test.R;
import com.apps.philipps.test.SoundManager;

public class Game extends Activity2D {
    private long enemySpawned;
    private TextView finished;
    private TextView secondsLeft;
    private BreathInterpreter.BreathStatus status;
    private long starSpawned;
    private ImageView loadingImage;
    private ProgressBar loadingProgress;
    private ProgressBar loadingProgress2;
    private TextView loadingProgressText;
    private double pressed = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_game);
    }

    @Override
    protected void onLoading(boolean firstLoad, int progress, long delta) {
        if (firstLoad) {
            loadingImage = (ImageView) findViewById(R.id.loadingImage);
            loadingProgress = (ProgressBar) findViewById(R.id.loadingProgress);
            loadingProgress2 = (ProgressBar) findViewById(R.id.loadingProgress2);
            loadingProgressText = (TextView) findViewById(R.id.loadingText);
            loadingImage.setImageResource(R.drawable.loadingscreen);
        }
        loadingProgress.setProgress(progress);
        loadingProgress2.setProgress(progress);
        loadingProgressText.setText("Loading... " + progress + "%");
    }

    @Override
    protected void onLoadingReady() {
        ((RelativeLayout) findViewById(R.id.fullSpaceFight)).removeView(loadingImage);
        ((RelativeLayout) findViewById(R.id.fullSpaceFight)).removeView(loadingProgress);
        ((RelativeLayout) findViewById(R.id.fullSpaceFight)).removeView(loadingProgress2);
        ((RelativeLayout) findViewById(R.id.fullSpaceFight)).removeView(loadingProgressText);
        game.setVisibility(View.VISIBLE);
    }

    @Override
    protected void draw() {
        double realTime = delta;
        double loopTime = (long) (GameStats.timeLoopAnimation.getPosition().get(0) * realTime);
        GameStats.timeLoopAnimation.update(realTime);
        Animations.updateAnimations(loopTime);
        GOFactory.ship.update(realTime);


        if (!PlanManager.isActive()) {
            stopDrawing();
            Coins.addCoins(getCoins());
            finished.setVisibility(View.VISIBLE);
        }
        long seconds = PlanManager.getDuration() / 1000;
        String left = (seconds / 60 != 0 ? (seconds / 60) + ":" : "")
                + (seconds != 0 ? seconds % 60 + ":" : "")
                + PlanManager.getDuration() % 1000;


        if (pressed == 0) {
            new GOFactory.Shoot(this, game);
            new GOFactory.Laser(this, getScreenWidth(), game);
            SoundManager.shoot();
        }
        pressed = pressed != -1 ? pressed + loopTime : -1;
        if (pressed >= GameStats.shoot.rate) {
            pressed = 0;
        }
        enemySpawned += loopTime;
        starSpawned += loopTime;
        if (enemySpawned > GameStats.enemyCome) {
            enemySpawned = 0;
            new GOFactory.Enemy(this, new Vector(getScreenWidth() - 50, getEnemyY()), game);
        }
        if (starSpawned > GameStats.starCome + getInt(0, 1000)) {
            starSpawned = 0;
            int y = getInt(0, getScreenHeight());
            new GOFactory.Star(this, new Vector(getScreenWidth(), y), game);
        }
        secondsLeft.bringToFront();
        secondsLeft.setText(left + "\n" + getCoins() + " Coins\n" + "Animations: " + Animations.count());
    }

    @Override
    protected void init() {
        SoundManager.init(this);
        AppState.recordData = false;
        PlanManager.start();
        GameStats.init(SCREEN_FACTOR);
        game = (RelativeLayout) findViewById(R.id.test_game2d);
        GOFactory.init(this, new Vector(50, getScreenHeight() / 2), game);
        finished = (TextView) findViewById(R.id.finished);
        secondsLeft = (TextView) findViewById(R.id.seconds_left);
    }

    @Override
    protected void touch(MotionEvent event) {
        if (isInitialized()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && finished.getVisibility() == View.VISIBLE)
                finish();
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                pressed = 0;
            else if (event.getAction() == MotionEvent.ACTION_UP)
                pressed = -1;
        }
    }

    @Override
    public void call(Object... messages) {
        if (messages.length == 2)
            status = (BreathInterpreter.BreathStatus) messages[1];
    }


    private int getEnemyY() {
        double result = 0;
        PlanManager.Plan plan = PlanManager.getCurrentPlan();
        if (plan != null) {
            double frequency = plan.getFrequency() / 60f;
            double absoluteDelta = currentTime - start;
            double value = (absoluteDelta / 1000 * Math.PI * frequency);
            double up = plan.getStrengthOut().value * (getScreenHeight()) / 2;
            double down = plan.getStrengthIn().value * (getScreenHeight() - 30) / 2;

            result = Math.sin(value);
            result = result < 0 ? result * up : result * down;
            result += getScreenHeight() / 2;
        }
        return (int) result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Animations.removeAll();
        Animations.updateAnimations(0);
    }
}