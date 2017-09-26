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
    private GameBuffer gameBuffer;
    private long enemySpawned;
    private TextView finisched;
    private TextView secondsLeft;
    private BreathInterpreter.BreathStatus status;
    private long starSpawned;
    private ImageView loadingImage;
    private ProgressBar loadingProgress;
    private ProgressBar loadingProgress2;
    private TextView loadingProgressText;
    private long pressed = -1;
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
        long realTime = delta;
        long loopTime = (long) (GameStats.timeLoopAnimation.getStart().get(0) * realTime);
        if (!PlanManager.isActive()) {
            stopDrawing();
            Coins.addCoins(getCoins());
            finisched.setVisibility(View.VISIBLE);
            gameBuffer.clear(game);
        }
        long seconds = PlanManager.getDuration() / 1000;
        String left = (seconds / 60 != 0 ? (seconds / 60) + ":" : "")
                + (seconds != 0 ? seconds % 60 + ":" : "")
                + PlanManager.getDuration() % 1000;

        List<GameObject2D> toRemove = new ArrayList<>();

        for (GameObject2D object : gameBuffer) {
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
            } else if (object instanceof GOFactory.Goody && ((GOFactory.Goody) object).toRemove) {
                game.removeView(object.getView());
                toRemove.add(object);
            } else if (object instanceof GOFactory.Explosion && ((GOFactory.Explosion) object).toRemove) {
                toRemove.add(object);
            } else if (object instanceof GOFactory.Shoot && ((GOFactory.Shoot) object).toRemove) {
                toRemove.add(object);
            } else if (object instanceof GOFactory.Laser) {
                if (!object.isMoving()) {
                    game.removeView(object.getView());
                    toRemove.add(object);
                } else {
                    for (GOFactory.Goody goody : gameBuffer.goodies) {
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
                    for (GOFactory.Enemy enemy : gameBuffer.enemies) {
                        if (object.intersect(enemy)) {
                            gameBuffer.explosions.add(new GOFactory.Explosion(this, enemy, game));
                            if (getInt(0, 5) == 5)
                                gameBuffer.goodies.add(new GOFactory.Goody(this, enemy, game, getInt(0, 5) != 1));
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

        if (gameBuffer.initialized) {
            if (pressed == 0) {
                gameBuffer.shoot = new GOFactory.Shoot(this, gameBuffer.ship, game);
                gameBuffer.laser.add(new GOFactory.Laser(this, gameBuffer.ship, getScreenWidth(), GameStats.shoot.getSpeed(), game));
            }
            pressed = pressed != -1 ? pressed + delta : -1;
            if (pressed >= GameStats.shoot.rate) {
                pressed = 0;
            }
            gameBuffer.removeAll(toRemove);
            if ((currentTime - enemySpawned) > GameStats.enemyCome) {
                enemySpawned = currentTime;
                gameBuffer.enemies.add(new GOFactory.Enemy(this, new Vector(getScreenWidth() - 50, getEnemyY()), GameStats.enemySpeed, game));
            }
            if ((currentTime - starSpawned) > GameStats.starCome + getInt(0, 1000)) {
                starSpawned = currentTime;
                int y = getInt(0, getScreenHeight());
                gameBuffer.stars.add(new GOFactory.Star(this, new Vector(getScreenWidth(), y), GameStats.starSpeed, game));
            }
            secondsLeft.setText(left + "\n" + getCoins() + " Coins\n" + status + " status");
            secondsLeft.bringToFront();
        }
    }

    @Override
    protected void init() {
//        AppState.recordData = false;
        PlanManager.start();
        GameStats.init();
        game = (RelativeLayout) findViewById(R.id.test_game2d);
        gameBuffer = new GameBuffer(new GOFactory.Ship(this, new Animated(new Vector(50, getScreenHeight() / 2)), game), SCREEN_FACTOR);
        finisched = (TextView) findViewById(R.id.finished);
        secondsLeft = (TextView) findViewById(R.id.seconds_left);
    }

    @Override
    protected void touch(MotionEvent event) {
        if (isInitialized()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && finisched.getVisibility() == View.VISIBLE)
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
            int height = gameBuffer.enemies.size() != 0 ? gameBuffer.enemies.get(0).getView().getMeasuredHeight() : 0;
            double up = plan.getStrengthOut().value * (getScreenHeight()) / 2;
            double down = plan.getStrengthIn().value * (getScreenHeight()-30) / 2;

            result = Math.sin(value);
            result = result < 0 ? result * up : result * down;
            result += getScreenHeight() / 2;
        }
        return (int) result;
    }


}