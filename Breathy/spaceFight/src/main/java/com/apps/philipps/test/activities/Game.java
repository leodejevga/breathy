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
import com.apps.philipps.source.helper.Animated;
import com.apps.philipps.source.helper.Animations;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.Activity2D;
import com.apps.philipps.source.helper._2D.GameObject2D;
import com.apps.philipps.test.GOFactory;
import com.apps.philipps.test.GameBuffer;
import com.apps.philipps.test.GameStats;
import com.apps.philipps.test.R;

import java.util.ArrayList;
import java.util.List;

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
    private String lastGoodie = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_game);
    }

    @Override
    protected void onLoading(boolean firstLoad, int progress) {
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
        double loopTime = (long) (GameStats.timeLoopAnimation.getStart().get(0) * realTime);
        if (!PlanManager.isActive()) {
            stopDrawing();
            Coins.addCoins(getCoins());
            finished.setVisibility(View.VISIBLE);
            GameBuffer.clear(game);
        }
        long seconds = PlanManager.getDuration() / 1000;
        String left = (seconds / 60 != 0 ? (seconds / 60) + ":" : "")
                + (seconds != 0 ? seconds % 60 + ":" : "")
                + PlanManager.getDuration() % 1000;

        List<GameObject2D> toRemove = new ArrayList<>();

        for (GameObject2D object : GameBuffer.iterable()) {
            GameStats.timeLoopAnimation.update(delta);
            if (object instanceof GOFactory.Ship)
                object.update(realTime);
            else object.update(loopTime);

            if (object instanceof GOFactory.Ship && status != null && BreathData.get(0) != null) {
                double d = BreathData.get(0).data;
                double y = (d - AppState.breathyUserMin) / (AppState.breathyUserMax - AppState.breathyUserMin) * getScreenHeight();
                object.move(new Vector(object.getPosition().get(0), y));
                object.getView().bringToFront();
            } else if (object instanceof GOFactory.Enemy && !object.isMoving()) {
                game.removeView(object.getView());
                subCoin();
                toRemove.add(object);
                object.getView().bringToFront();
            } else if (object instanceof GOFactory.Star && !object.isMoving()) {
                game.removeView(object.getView());
                toRemove.add(object);
            } else if (object instanceof GOFactory.Laser) {
                if (!object.isMoving()) {
                    game.removeView(object.getView());
                    toRemove.add(object);
                } else {
                    for (GOFactory.Goody goody : GameBuffer.goodies) {
                        if (object.intersect(goody)) {
                            if (goody.activate()) {
                                if (goody.effect.good)
                                    addCoin(10);
                                else
                                    subCoin(10);
                            }
                            lastGoodie = goody.effect.name;
                            game.removeView(goody.getView());
                            game.removeView(object.getView());
                            toRemove.add(object);
                            addCoin();
                        }
                    }
                    for (GOFactory.Enemy enemy : GameBuffer.enemies) {
                        if (object.intersect(enemy)) {
                            GameBuffer.explosions.add(new GOFactory.Explosion(this, enemy, game));
                            if (getInt(0, 5) == 5)
                                GameBuffer.goodies.add(new GOFactory.Goody(this, enemy, game, getInt(0, 5) != 1));
                            game.removeView(enemy.getView());
                            game.removeView(object.getView());
                            toRemove.add(object);
                            toRemove.add(enemy);
                            addCoin();
                        }
                    }
                }
            }
        }

        if (GameBuffer.initialized) {
            if (pressed == 0) {
                GameBuffer.shoot.add(new GOFactory.Shoot(this, game));
                GameBuffer.laser.add(new GOFactory.Laser(this, getScreenWidth(), GameStats.shoot.getSpeed(), game));
            }
            pressed = pressed != -1 ? pressed + loopTime : -1;
            if (pressed >= GameStats.shoot.rate) {
                pressed = 0;
            }
            GameBuffer.removeAll(toRemove);
            enemySpawned += loopTime;
            starSpawned += loopTime;
            if (enemySpawned > GameStats.enemyCome) {
                enemySpawned = 0;
                GameBuffer.enemies.add(new GOFactory.Enemy(this, new Vector(getScreenWidth() - 50, getEnemyY()), GameStats.enemySpeed, game));
            }
            if (starSpawned > GameStats.starCome + getInt(0, 1000)) {
                starSpawned = 0;
                int y = getInt(0, getScreenHeight());
                GameBuffer.stars.add(new GOFactory.Star(this, new Vector(getScreenWidth(), y), GameStats.starSpeed, game));
            }
        }
        secondsLeft.bringToFront();
        secondsLeft.setText(left + "\n" + getCoins() + " Coins\n" + status + " status\n" +
                realTime + " real Time\n" + loopTime + " loop time\n" + lastGoodie);
        Animations.update(realTime);
    }

    @Override
    protected void init() {
//        AppState.recordData = false;
        PlanManager.start();
        GameStats.init();
        game = (RelativeLayout) findViewById(R.id.test_game2d);
        GameBuffer.init(new GOFactory.Ship(this, new Animated(new Vector(50, getScreenHeight() / 2)), game), SCREEN_FACTOR);
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
            int height = GameBuffer.enemies.size() != 0 ? GameBuffer.enemies.get(0).getView().getMeasuredHeight() : 0;
            double up = plan.getStrengthOut().value * (getScreenHeight()) / 2;
            double down = plan.getStrengthIn().value * (getScreenHeight() - 30) / 2;

            result = Math.sin(value);
            result = result < 0 ? result * up : result * down;
            result += getScreenHeight() / 2;
        }
        return (int) result;
    }


}