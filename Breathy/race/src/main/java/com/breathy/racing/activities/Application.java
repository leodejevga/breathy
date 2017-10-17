package com.breathy.racing.activities;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.SaveData;
import com.apps.philipps.source.cachemanager.UserData;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._2D.Activity2D;
import com.apps.philipps.source.helper._2D.GameObject2D;
import com.breathy.racing.GameUtil;
import com.breathy.racing.R;
import com.breathy.racing.RaceBreathInterpreter;
import com.breathy.racing.RaceTimer;
import com.breathy.racing.RaceUtil;
import com.breathy.racing.SlowCar;
import com.breathy.racing.SlowerUpdate;
import com.breathy.racing.stats.RaceDifficult;
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
//TODO screenfactor
public class Application extends Activity2D {
    GameObject2D car;
    ArrayList<SlowCar> slowCars;
    LinkedList<GameObject2D> background;
    RelativeLayout game;

    double highscore = 0;
    double safedhighscore = 0;
    double gameScoreMultiplier = 1;
    TextView score;
    ProgressBar lvlUpBar;

    MediaPlayer myMediaPlayer;

    private ImageView loadingImage;
    private ProgressBar loadingProgress;
    private ProgressBar loadingProgress2;
    private TextView loadingProgressText;

    // Game stats
    long nextCar;
    long start;
    Random random;
    float[] xRoads;
    float yCar;
    RaceBreathInterpreter raceBreathInterpreter;
    ArrayList<RaceTimer> timers = new ArrayList<RaceTimer>();

    // Player
    int curXIndex;

    //Breathgraph
    private double breathdata;
    private double testdata;
    private LineChart myChart;
    private LineData chartData;
    private LineDataSet breathChartData;
    private LineDataSet breathPlaneChartData;

    final int BACKGROUNDSPEED = 20;
    private SlowerUpdate oneCallPerMin;
    private ImageView black;
    private RaceDifficult raceDifficult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.racing_gamescreen);
        myMediaPlayer = MediaPlayer.create(this, R.raw.car_music);

        int maxVolume = 50;
        float log1=(float)(Math.log(maxVolume-25)/Math.log(maxVolume));
        myMediaPlayer.setVolume(1-log1, 1-log1);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppState.inGame = AppState.recordData = false;
        myMediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myMediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myMediaPlayer.start();
    }

    @Override
    protected void onLoading(boolean firstLoad, int progress, long delta) {
        if (firstLoad) {
            loadingImage = (ImageView) findViewById(R.id.loadingImage);
            loadingProgress = (ProgressBar) findViewById(R.id.loadingProgress);
            loadingProgress2 = (ProgressBar) findViewById(R.id.loadingProgress2);
            loadingProgressText = (TextView) findViewById(R.id.loadingText);
            loadingImage.setImageResource(R.drawable.loading_image);
        }
        loadingProgress.setProgress(progress);
        loadingProgress2.setProgress(progress);
        loadingProgressText.setText("Loading... " + progress + "%");
    }

    @Override
    protected void onLoadingReady() {
        ((RelativeLayout) findViewById(R.id.loadingArea)).removeView(loadingImage);
        ((RelativeLayout) findViewById(R.id.loadingArea)).removeView(loadingProgress);
        ((RelativeLayout) findViewById(R.id.loadingArea)).removeView(loadingProgress2);
        ((RelativeLayout) findViewById(R.id.loadingArea)).removeView(loadingProgressText);
        game.setVisibility( View.VISIBLE);

    }


    @Override
    protected void draw() {
        long delta = System.currentTimeMillis() - start;

        if (!PlanManager.isActive()) {
            stopDrawing();
        }

        if(oneCallPerMin.update()){

            int target = (int) car.getDestination().get( 0 );
            yCar = raceBreathInterpreter.getCurY();
            car.getView().setY( yCar );
            car.move(new Vector( target, yCar ) );
        }
        updateBreathStatus();
        calcEmeny( delta );
        Log.i("Draw",background.getFirst().isMoving() + "");
        //checkBackground
        if ( background.getLast().getPosition().get( 1 )>= 0) { // !background.getFirst().isMoving() ||

            background.removeFirst();
            createBackground();
            Log.i("Draw1","1.");
        }

        background.getFirst().update(delta);
        background.getLast().update(delta);

        car.update(delta);
        score.setText("Highscore: " + (Math.round(100 * safedhighscore) / 100.0) + " + " + (Math.round(100 * highscore) / 100));
        start = System.currentTimeMillis();
        refreshChart();



    }

    public void loadOptions( ){
        SaveData<UserData> saveUserdata =new SaveData<>(this);
        UserData userData = saveUserdata.readObject("userData");
        setExpStats(userData.getExp());

    }

    private void setExpStats( UserData.Experience exp ) {
        if (exp == UserData.Experience.BEGINNER || exp == null){
            double deltaSpeed = 200 * SCREEN_FACTOR;
            raceDifficult = new RaceDifficult( deltaSpeed, 1200., 1600., 2000.);
        } else {
            double deltaSpeed = 500 * SCREEN_FACTOR;
            raceDifficult = new RaceDifficult( deltaSpeed, 1000, 1400., 1800.);
        }
    }

    @Override
    protected void init() {
        PlanManager.getPlan( 0 );
        PlanManager.start();

        loadOptions();
        iniViews();
        this.oneCallPerMin = new SlowerUpdate( this.getFrameRate(), 1 );
        raceBreathInterpreter = new RaceBreathInterpreter( new Pair<>( (int) getScreenHeight(true) / 2, (int) getScreenHeight(true) - 264) );

        iniPlayerCar();
        iniChartElement();
        iniBackground();

        curXIndex = 1;
        slowCars = new ArrayList<>();
        start = System.currentTimeMillis();
        nextCar = start;

        random = new Random(5);
        if (game == null)
            stopDrawing();
    }

    private void iniBackground() {
        background = new LinkedList<>();
        background.add(initObject(new ImageView(this), R.drawable.stadt, 3, new Vector(0, 0), new Vector(0, getScreenHeight(true)), (int) (BACKGROUNDSPEED + raceDifficult.getDeltaSpeed())));
        background.getFirst().getView().setLayoutParams(new ViewGroup.LayoutParams((int) getScreenWidth(true), (int) getScreenHeight(true)));
        GameUtil.sendViewToBack(background.getLast().getView());
        createBackground();
    }

    private void iniPlayerCar() {
        xRoads = new float[]{(int) (getScreenWidth(true) /100 * 25), (int) (getScreenWidth(true) / 100 * 42.5), (int) (getScreenWidth(true) / 5 * 3)};
        yCar = (int) getScreenHeight(true) - 600;

        car = initObject( new ImageView( this ), R.drawable.car, 1, new Vector( xRoads[1], yCar ), new Vector( xRoads[1], yCar ), 1200 );
        car.getView().getLayoutParams().height = (int) (getScreenWidth(true)*0.15);
        car.getView().getLayoutParams().width = (int) (getScreenWidth(true)*0.15);
    }

    private void iniChartElement() {
        myChart = RaceUtil.createLineChart(this);
        game.addView(myChart, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 600));
        chartData = RaceUtil.createData();
        myChart.setData(chartData);
        myChart.bringToFront();
        breathChartData = RaceUtil.createDataSet("BreathData", Color.RED);
        chartData.addDataSet(breathChartData);
        breathPlaneChartData = RaceUtil.createDataSet("PlanData", Color.YELLOW);
        chartData.addDataSet(breathPlaneChartData);
        //chartData.addDataSet(breathPlaneChartData);
        chartData.notifyDataChanged();
    }

    private void iniViews() {
        game = (RelativeLayout) findViewById( R.id.gameArea );
        score = (TextView) findViewById( R.id.score );
        lvlUpBar = (ProgressBar) findViewById( R.id.progressBar );
        black = (ImageView) findViewById( R.id.black );
        lvlUpBar.setProgress( 0 );
        lvlUpBar.setMax( 5000 );
    }

    /**
     * this method is called if the user touch the screen
     *
     * @param event
     */
    @Override
    protected void touch( MotionEvent event ) {
        if ( event.getAction() == MotionEvent.ACTION_DOWN){ // || event.getAction() == MotionEvent.ACTION_MOVE) { //TODO hier muss wieder andere Touch event hin
            int dx = (int) event.getX();
            int dy = (int) event.getY();

            // get the direction
            if ( dy < getScreenHeight(true) / 2 && raceBreathInterpreter.getCurBostPoints() >= lvlUpBar.getMax() ) {
                lvlUp();
            } else if (getScreenWidth(true) / 2 < dx) {
                movePlayer("right");
            } else {
                movePlayer("left");
            }
        }
    }

    private void lvlUp() {
        safedhighscore += highscore * 1.2;
        raceBreathInterpreter.setCurBostPoints( 0 );
        highscore = 0;
        calcSpeed();
    }

    /**
     * move the player right or left
     *
     * @param direction must be "right" or "left"
     */
    public void movePlayer(String direction) {
        switch (direction) {
            case "right":
                Log.i("Move", "Right move");
                if ( curXIndex == 0 ) {
                    car.move( new Vector( xRoads[1], yCar ) );

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

            if (slowCars.get(i).intersect(car)) {
                game.removeView(slowCars.get(i).getView());
                slowCars.remove(slowCars.get(i));
                i--;
                highscore = 0;
                continue;
            }



            if (!slowCars.get(i).isMoving() || slowCars.get( i ).getPosition().get( 0 ) > getScreenHeight( true )){
                game.removeView(slowCars.get(i).getView());
                slowCars.remove(slowCars.get(i));
                highscore += 1 * gameScoreMultiplier;

                i--;

            } else {
                checkOvertake( i );
                slowCars.get( i ).update( delta );

            }



        }
    }


    private void checkOvertake(int i) {
        int j;
        SlowCar curCar = slowCars.get( i );
        int carSpeed =(int) curCar.getSpeed();

        int curStreet = curCar.getCurXStreet();
        boolean leftLane = (curStreet == 0 ? false : true);
        boolean rightLane = (curStreet == 2 ? false : true);
        int overtake = -1;

        for(j = i; j<slowCars.size(); j++){
            SlowCar carInFront = slowCars.get( j );

//            if(carInFront.getPosition().get( 1 ) < curCar.getPosition().get( 1 ) - 2*carSpeed){
//                break;
//            }

            if(overtake == -1 && (curStreet == carInFront.getCurXStreet() && carInFront.getPosition().get( 1 ) <
                    curCar.getPosition().get( 1 ) + 2*(carSpeed-carInFront.getSpeed()) &&
                    curCar.getSpeed() <= carInFront.getSpeed())){

                overtake = j;
            }

            if (carInFront.getCurXStreet() + 1 == curStreet) {
                rightLane = false;
            } else if (carInFront.getCurXStreet() - 1 == curStreet) {
                leftLane = false;
            }

        }

        if (overtake > 0) {
            if (rightLane) {
                curCar.move(new Vector(xRoads[curStreet + 1], curCar.getDestination().get(1)));
            } else if (leftLane) {
                curCar.move(new Vector(xRoads[curStreet - 1], curCar.getDestination().get(1)));
            } else {
                curCar.move(slowCars.get(overtake).getSpeed());
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

        double speed = GameUtil.triangularDistribution( 0.5, 1.4, 1 );
        slowCars.add( initSlowCar( new ImageView( this ), GameUtil.getRandomCar(), 0, new Vector( xRoads[xIndex], (float) 10 ),
                new Vector( xRoads[xIndex], getScreenHeight(true) ), (int) raceDifficult.getDeltaSpeed() , xIndex, speed) );
        slowCars.get( slowCars.size()-1).getView().getLayoutParams().height = (int) (getScreenWidth(true)*0.15);
        slowCars.get( slowCars.size()-1).getView().getLayoutParams().width = (int) (getScreenWidth(true)*0.15);
        nextCar = raceDifficult.getIntervalTime();

    }

    /**
     * this method calculate the bostpoints of the player depended of the breathdata
     */
    private void updateBreathStatus() {

        raceBreathInterpreter.update();
        lvlUpBar.setProgress( raceBreathInterpreter.getCurBostPoints() );

    }

    /**
     * this method calculate the deltaspeed for all emenys and backgrounds
     */
    private void calcSpeed() {

            raceDifficult.increaseSpeed();
            gameScoreMultiplier += .01;
            for ( int i = 0; i < slowCars.size(); i++ ) {
                slowCars.get( i ).move( (int) (raceDifficult.getDeltaSpeed()*slowCars.get( i ).getGroundSpeed()) );
            }
            background.getFirst().move( (int) (BACKGROUNDSPEED + raceDifficult.getDeltaSpeed()) );
            background.getLast().move( (int) (BACKGROUNDSPEED + raceDifficult.getDeltaSpeed()) );


    }

    /**
     * creates a new Background above the screen and bring him to the back
     */
    private void createBackground() {
        int start = 0 - background.getFirst().getView().getLayoutParams().height; //(int)getScreenHeight(true);
        if ( Math.random() > 0.5 ) {
            background.add( initObject( new ImageView( this ), R.drawable.stadt22, 3, new Vector( 0, start ), new Vector( 0, getScreenHeight(true) ), (int) (BACKGROUNDSPEED + raceDifficult.getDeltaSpeed()) ) );
        } else {
            background.add( initObject( new ImageView( this ), R.drawable.wald, 3, new Vector( 0, start ), new Vector( 0, getScreenHeight(true) ), (int) (BACKGROUNDSPEED + raceDifficult.getDeltaSpeed()) ) );
        }
        background.getLast().getView().setLayoutParams(new ViewGroup.LayoutParams((int) getScreenWidth(true), (int) getScreenHeight(true)));
        background.getLast().getView().getLayoutParams().height =  getScreenHeight();
        GameUtil.sendViewToBack( background.getLast().getView() );
        background.getLast().getView().requestLayout();

    }

    private void refreshChart() {

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
     * @param move        the movementspeed of the object in pixel per updateAnimations
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



