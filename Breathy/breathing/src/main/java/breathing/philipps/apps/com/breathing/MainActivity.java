package breathing.philipps.apps.com.breathing;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import utils.Bubble;
import utils.Fish;
import utils.HighScoreHelper;
import utils.SimpleAlertDialog;
import utils.SoundHelper;

public class MainActivity extends AppCompatActivity implements Fish.FishListener {

    private ViewGroup mContentView;
    private ProgressBar pBar;
    private int mScreenWidth, mScreenHight;
    private static final int MIN_ANIMATION_DELAY = 500;
    private static final int MAX_ANIMATION_DELAY = 1500;
    private static final int MIN_ANIMATION_DURATION = 1000;
    private static final int MAX_ANIMATION_DURATION = 8000;
    private Button mGoButton;
    private boolean mPlaying;
    private boolean mGameStopped = true;

    private int mlevel;
    private int mScore;
    TextView mScoreDisplay, mLevelDisplay;
    private List<Fish> mFish = new ArrayList<Fish>();
    private int mFishPopped;
    private static final int FISH_PER_LEVEL = 10;
    private static final int Bubble_PER_LEVEL = 20;
    private SoundHelper mSoundHelper;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing_game);


        textView = (TextView) findViewById(R.id.Breathing_status_display);

        pBar = (ProgressBar) findViewById(R.id.pBar);
        pBar.setVisibility(View.VISIBLE);
        pBar.setMax(100);
        pBar.setProgress(100);
        pBar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);

        getWindow().setBackgroundDrawableResource(R.drawable.b);

        mContentView = (ViewGroup) findViewById(R.id.activity_main1);
        setToFullScreen();
        ViewTreeObserver viewTreeObserver = mContentView.getViewTreeObserver();

        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mScreenWidth = mContentView.getWidth();
                    mScreenHight = mContentView.getHeight();
                }
            });
        }

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });

        mScoreDisplay = (TextView) findViewById(R.id.score_display);
        mLevelDisplay = (TextView) findViewById(R.id.level_display);

        mGoButton = (Button) findViewById(R.id.go_button);
        updateDisplay();

        mSoundHelper = new SoundHelper(this);
        mSoundHelper.prepareMusicPlayer(this);
    }

    private void setToFullScreen() {
        ViewGroup rootLayout = (ViewGroup) findViewById(R.id.activity_main1);

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onResume() {
        super.onResume();
        setToFullScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundHelper.pauseMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundHelper.stopMusic();
    }

    private void startGame() {
        setToFullScreen();
        mScore = 0;
        mlevel = 0;
        pBar.setProgress(100);
        pBar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        mGameStopped = false;
        startLevel();
        mSoundHelper.playMusic();
    }

    private void startLevel() {
        mlevel++;
        updateDisplay();
        FishLauncher Launcher = new FishLauncher();
        BubbleLauncher bubbleLauncher = new BubbleLauncher();
        Launcher.execute(mlevel);
        //bubbleLauncher.execute(mlevel);

        mPlaying = true;
        mFishPopped = 0;
        mGoButton.setText("Stop Game");
    }

    private void finishLevel() {
        Toast.makeText(this, String.format("You finished level %d", mlevel), Toast.LENGTH_SHORT).show();
        mPlaying = false;
        mGoButton.setText(String.format("Start level %d", mlevel + 1));

    }

    private int generateRandom(int a, int b) {
        Random r = new Random();
        return r.nextInt(b) + a;

    }

    public void goButtonClickHandler(View view) {

        if (mPlaying) {
            gameOver(false);
        } else if (mGameStopped) {
            startGame();
        } else {
            startLevel();
        }
    }

    @Override
    public void popFish(Fish fish, boolean userTouch) {

        mFishPopped++;
        mSoundHelper.playSound();
        mContentView.removeView(fish);
        mFish.remove(fish);

        if (userTouch) {
            mScore++;
        } else {
            pBar.setProgress(pBar.getProgress() - 20);
            if (pBar.getProgress() < 60)
                pBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            if (pBar.getProgress() <= 0) {
                gameOver(true);
            }
        }
        updateDisplay();

        if (mFishPopped == FISH_PER_LEVEL) {
            finishLevel();
        }
    }

    private void updateDisplay() {
        mScoreDisplay.setText(String.valueOf(mScore));
        mLevelDisplay.setText(String.valueOf(mlevel));

    }

    private void gameOver(boolean b) {

        Toast.makeText(this, "Game over!!", Toast.LENGTH_SHORT).show();
        mSoundHelper.pauseMusic();

        for (Fish fish : mFish) {
            mContentView.removeView(fish);
            fish.setPop(true);

        }
        mFish.clear();
        mPlaying = false;
        mGameStopped = true;
        mGoButton.setText("Start game");

        if (b) {
            if (HighScoreHelper.isTopScore(this, mScore)) {
                HighScoreHelper.setTopScore(this, mScore);
                SimpleAlertDialog dialog = SimpleAlertDialog.newInstance("New High Score!",
                        String.format("Your new High Score ist %d", mScore));
                dialog.show(getSupportFragmentManager(), null);
            }

        }

    }

    private class FishLauncher extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError(
                        "Expected 1 param for current level");
            }

            int level = params[0];
            int maxDelay = Math.max(MIN_ANIMATION_DELAY,
                    (MAX_ANIMATION_DELAY - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int fishesLaunched = 0;
            while (mPlaying && fishesLaunched < FISH_PER_LEVEL) {

//              Get a random horizontal position for the next Fish
                Random random = new Random(new Date().getTime());
                int yPosition = random.nextInt(mScreenHight - 180);
                publishProgress(yPosition);
                fishesLaunched++;

//              Wait a random number of milliseconds before looping
                int delay = random.nextInt(minDelay) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int yPosition = values[0];
            launchFish(yPosition);
        }

    }

    private class BubbleLauncher extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError(
                        "Expected 1 param for current level");
            }

            int level = params[0];
            int maxDelay = Math.max(MIN_ANIMATION_DELAY,
                    (MAX_ANIMATION_DELAY - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int BubblesLaunched = 0;
            while (BubblesLaunched < Bubble_PER_LEVEL) {

//              Get a random horizontal position for the next Fish
                Random random = new Random(new Date().getTime());
                int xPosition = random.nextInt(mScreenWidth - 100);
                publishProgress(xPosition);
                BubblesLaunched++;

//              Wait a random number of milliseconds before looping
                int delay = random.nextInt(200) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            launchBubble(xPosition);
        }

    }

    private void launchFish(int y) {

        Fish fish = new Fish(this, 100, generateRandom(1, 4));
        mFish.add(fish);

//      Set fishes horizental position and dimensions, add to container
        fish.setY(y);
        fish.setX(0);
        mContentView.addView(fish);

//      Let 'er swim
        int duration = Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (mlevel * 1000));
        fish.releaseFish(mScreenWidth, duration);


    }

    private void launchBubble(int x) {

        Bubble bubble = null;
        int rand = generateRandom(1, 2);
        if (rand == 1)
            bubble = new Bubble(this, 90, 1);
        if (rand == 2)
            bubble = new Bubble(this, 70, 2);
        if (rand == 3)
            bubble = new Bubble(this, 40, 3);


//      Set fishes horizental position and dimensions, add to container
        bubble.setX(x);
        bubble.setY(mScreenHight + bubble.getHeight());
        mContentView.addView(bubble);
//      Let 'er swim
        textView.setText("Bubble");
        int duration = Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (mlevel * 1000));
        bubble.releaseBubble(mScreenHight, duration);

    }
}
