package com.breathy.racing.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.Activity2D;
import com.apps.philipps.source.helper._2D.GameObject2D;
import com.breathy.racing.GameUtil;
import com.breathy.racing.R;
import com.breathy.racing.RaceUtil;
import com.breathy.racing.SlowCar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * This Class provides the Game Logic of the racing Game
 */

public class Application extends Activity2D {
    // Gameobjects and Views
    GameObject2D car;
    ArrayList<SlowCar> slowCars;
    LinkedList<GameObject2D> background;
    RelativeLayout game;
    int carNumber;

    // Highscore
    double highscore = 0;
    double safedhighscore = 0;
    double gameScoreMultiplier = 1;
    TextView score;
    int bostPoints;
    ProgressBar lvlUpBar;

    // Game stats
    double deltaSpeed;
    long nextCar;
    long start;
    Random random;
    float[] xRoads;
    float yCar;
    int faster = 0;

    // Player
    int curXIndex;

    //Breathgraph
    private double breathdata;
    private double testdata;
    private LineChart myChart;
    private LineData chartData;
    private LineDataSet breathChartData;
    private LineDataSet breathPlaneChartData;
    private Integer xIndex = 0;

    final int BACKGROUNDSPEED = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.racing_gamescreen);
    }

    @Override
    protected void onLoading(boolean firstLoad, int progress) {
    }


    @Override
    protected void draw() {
        long delta = System.currentTimeMillis() - start;
        calcSpeed();

        calcEmeny(delta);

        //checkBackground
        if (!background.getFirst().isMoving()) {
            background.removeFirst();
            createBackground();
        }

        background.getFirst().update(delta);
        background.getLast().update(delta);

        car.update(delta);
        score.setText("Highscore: " + (Math.round(100 * safedhighscore) / 100.0) + " + " + (Math.round(100 * highscore) / 100));
        start = System.currentTimeMillis();

        refreshChart();

        calcBostPoints();


    }


    @Override
    protected void init() {
        //RelativeLayout game = new RelativeLayout(getBaseContext());
        game = (RelativeLayout) findViewById(R.id.gameArea);
        score = (TextView) findViewById(R.id.score);
        lvlUpBar = (ProgressBar) findViewById(R.id.progressBar);
        lvlUpBar.setProgress(0);
        lvlUpBar.setMax(10000);
        xRoads = new float[]{(int) (getScreenWidth() / 100 * 25), (int) (getScreenWidth() / 100 * 42.5), (int) (getScreenWidth() / 5 * 3)};

        yCar = getScreenHeight() - 264;
        car = initObject(new ImageView(this), R.drawable.car, 1, new Vector(xRoads[1], yCar), new Vector(xRoads[1], yCar), 1200);
        car.getView().getLayoutParams().height = (int) (getScreenWidth() * 0.15);
        car.getView().getLayoutParams().width = (int) (getScreenWidth() * 0.15);
        //Chart element
        myChart = RaceUtil.createLineChart(this);
        game.addView(myChart, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 400));
        chartData = RaceUtil.createData();
        myChart.setData(chartData);
        myChart.bringToFront();
        breathChartData = RaceUtil.createDataSet("BreathData", Color.RED);
        chartData.addDataSet(breathChartData);
        breathPlaneChartData = RaceUtil.createDataSet("PlanData", Color.GREEN);
        chartData.addDataSet(breathPlaneChartData);
        chartData.addDataSet(breathPlaneChartData);

        chartData.notifyDataChanged();
        //create Background
        background = new LinkedList<>();
        background.add(initObject(new ImageView(this), R.drawable.stadt, 3, new Vector(0, 0), new Vector(0, getScreenHeight()), (int) (BACKGROUNDSPEED + deltaSpeed)));
        background.getFirst().getView().setLayoutParams(new ViewGroup.LayoutParams((int) getScreenWidth(), (int) getScreenHeight()));
        GameUtil.sendViewToBack(background.getLast().getView());
        createBackground();

        curXIndex = 1;
        slowCars = new ArrayList<>();
        start = System.currentTimeMillis();
        nextCar = start;


        if (game == null) stopDrawing();
        deltaSpeed = 400 * SCREEN_FACTOR;
        random = new Random(5);
        if (game == null)
            stopDrawing();
    }

    /**
     * this method is called if the user touch the screen
     *
     * @param event
     */
    @Override
    protected void touch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            int dx = (int) event.getX();
            int dy = (int) event.getY();
            if (dy < getScreenHeight() / 2)
                car.setRotation(0.1);
            // get the direction
            if (dy < getScreenHeight() / 2 && bostPoints >= lvlUpBar.getMax()) {
                safedhighscore += highscore * 1.2;
                bostPoints = 0;
                highscore = 0;
            } else if (getScreenWidth() / 2 < dx) {
                movePlayer("right");
            } else {
                movePlayer("left");
            }
        }
    }

    /**
     * move the player right or left
     *
     * @param direction must be "right" or "left"
     */
    public void movePlayer(String direction) {
        switch (direction) {
            case "right":

                if (curXIndex == 0) {
                    car.move(new Vector(xRoads[1], yCar));
                    curXIndex = 1;
                } else if (curXIndex == 1) {
                    car.move(new Vector(xRoads[2], yCar));
                    curXIndex = 2;
                }
                break;
            case "left":
                if (curXIndex == 2) {
                    car.move(new Vector(xRoads[1], yCar));
                    curXIndex = 1;
                } else if (curXIndex == 1) {
                    car.move(new Vector(xRoads[0], yCar));
                    curXIndex = 0;
                }
                break;
        }
    }

    private void calcEmeny(long delta) {

        if (System.currentTimeMillis() - nextCar > 0) {
            createEmeny();
        }

        for (int i = 0; i < slowCars.size(); i++) {
            boolean removed = false;

            if (slowCars.get(i).intersect(car)) {
                game.removeView(slowCars.get(i).getView());
                slowCars.remove(slowCars.get(i));
                removed = true;
                i--;
                highscore = 0;
            }

            checkOvertake(i);

            if (!removed && !slowCars.get(i).isMoving()) {
                game.removeView(slowCars.get(i).getView());
                slowCars.remove(slowCars.get(i));
                highscore += 1 * gameScoreMultiplier;

            } else if (!removed) slowCars.get(i).update(delta);

        }
    }


    private void checkOvertake(int i) {
        int j;
        SlowCar curCar = slowCars.get(i);
        double carSpeed = curCar.getAnimated().getSpeed();
        boolean leftLane = true;
        boolean rightLane = true;
        int overtake = 0;
        int curStreet = curCar.getCurXStreet();
        for (j = i; j < slowCars.size(); j++) {
            SlowCar carInFront = slowCars.get(j);

            if (carInFront.getPosition().get(1) < curCar.getPosition().get(1) - 4 * carSpeed) {
                break;
            }

            if (overtake == 0 && (curStreet == carInFront.getCurXStreet() && carInFront.getPosition().get(1) <
                    curCar.getPosition().get(1) - 4 * (carSpeed - carInFront.getAnimated().getSpeed()) &&
                    curCar.getAnimated().getSpeed() <= carInFront.getAnimated().getSpeed())) {
                overtake = j;
            }

            if (carInFront.getCurXStreet() + 1 == curStreet) {
                rightLane = false;
            } else if (carInFront.getCurXStreet() - 1 == curStreet) {
                leftLane = false;
            }

        }
        if (overtake != 0) {
            if (rightLane) {
                curCar.move(new Vector(xRoads[curStreet + 1], curCar.getAnimated().getDestination().get(1)));
            } else if (leftLane) {
                curCar.move(new Vector(xRoads[curStreet - 1], curCar.getAnimated().getDestination().get(1)));
            } else {
                curCar.move(slowCars.get(overtake).getAnimated().getSpeed());
            }
        }
    }

    /**
     * creates a new Emeny on a pseudo random line
     */
    private void createEmeny() {

        int xIndex = GameUtil.getRandomNumber(0, 2);
        if (slowCars.size() != 0 && xIndex == slowCars.get(slowCars.size() - 1).getPosition().get(0)) {
            xIndex = GameUtil.getRandomNumber(0, 2);
        }
        double speed = GameUtil.triangularDistribution(0.1, 1.4, 1);
        slowCars.add(initSlowCar(new ImageView(this), R.drawable.slow_car, 0, new Vector(xRoads[xIndex], (float) 10),
                new Vector(xRoads[xIndex], getScreenHeight()), (int) deltaSpeed, xIndex, speed));
        slowCars.get(slowCars.size() - 1).getView().getLayoutParams().height = (int) (getScreenWidth() * 0.15);
        slowCars.get(slowCars.size() - 1).getView().getLayoutParams().width = (int) (getScreenWidth() * 0.15);
        nextCar = System.currentTimeMillis() + (long) GameUtil.triangularDistribution(1500., 2300., 2000.);
    }

    /**
     * this method calculate the bostpoints of the player depended of the breathdata
     */
    private void calcBostPoints() {

        if (testdata == breathdata) { //ToDo toleranz
            bostPoints += 10;
        } else if (testdata + 100 < breathdata && testdata - 100 > breathdata) {
            bostPoints = Math.max(bostPoints - 1, 0);
        }
        lvlUpBar.setProgress(bostPoints);
    }

    /**
     * this method calculate the deltaspeed for all emenys and backgrounds
     */
    private void calcSpeed() {
        if (faster >= (60 * 15)) {
            deltaSpeed += 10;
            faster = 0;
            gameScoreMultiplier += .1;
            for (int i = 0; i < slowCars.size(); i++) {
                slowCars.get(i).move((int) (deltaSpeed * slowCars.get(i).getGroundSpeed()));
            }
            background.getFirst().move((int) (BACKGROUNDSPEED + deltaSpeed));
            background.getLast().move((int) (BACKGROUNDSPEED + deltaSpeed));
        } else faster++;
    }

    /**
     * creates a new Background above the screen and bring him to the back
     */
    private void createBackground() {
        if (Math.random() > 0.5) {
            background.add(initObject(new ImageView(this), R.drawable.stadt, 3, new Vector(0, 0 - getScreenHeight()), new Vector(0, getScreenHeight()), (int) (BACKGROUNDSPEED + deltaSpeed)));
        } else {
            background.add(initObject(new ImageView(this), R.drawable.wald, 3, new Vector(0, 0 - getScreenHeight()), new Vector(0, getScreenHeight()), (int) (BACKGROUNDSPEED + deltaSpeed)));
        }
        background.getLast().getView().setLayoutParams(new ViewGroup.LayoutParams((int) getScreenWidth(), (int) getScreenHeight()));
        GameUtil.sendViewToBack(background.getLast().getView());

    }

    private void refreshChart() {
        testdata = BreathData.get(0).data * GameUtil.getRandomNumber(0, 1);
        breathdata = BreathData.get(0).data;

        breathChartData.addEntry(new Entry(breathChartData.getEntryCount(), (float) breathdata));
        breathPlaneChartData.addEntry(new Entry(breathPlaneChartData.getEntryCount(), (float) testdata));
        breathChartData.notifyDataSetChanged();
        chartData.notifyDataChanged();
        myChart.notifyDataSetChanged();
        myChart.refreshDrawableState();
        myChart.invalidate();
        myChart.setVisibleXRange(6, 60);
        myChart.moveViewToX(breathPlaneChartData.getEntryCount() - 60);
    }

    /**
     * creates a new 2D Gameobject
     *
     * @param view        the parent view
     * @param content     drawable ressource
     * @param id          //TODO
     * @param position    2d position of the gameobject
     * @param destination 2d destination of the gameobject
     * @param move        the movementspeed of the object in pixel per update
     * @return the gameobject
     */
    private GameObject2D initObject(ImageView view, @DrawableRes int content, int id, Vector position, Vector destination, int move) {
        view.setId(id);
        view.setImageResource(content);
        game.addView(view);
        GameObject2D result = new GameObject2D(view, position, destination);
        result.move(move);
        return result;
    }

    private SlowCar initSlowCar(ImageView view, @DrawableRes int content, int id, Vector position, Vector destination, int move, int curXStreet, double groundspeed) {
        view.setId(id);
        view.setImageResource(content);
        game.addView(view);
        SlowCar result = new SlowCar(view, position, destination, curXStreet, groundspeed);
        result.move((int) (move * groundspeed));
        return result;
    }

    @Override
    public void call(Object... messages) {

    }
}



