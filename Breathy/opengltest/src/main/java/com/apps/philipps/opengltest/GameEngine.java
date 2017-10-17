package com.apps.philipps.opengltest;

import android.content.Context;
import android.media.MediaPlayer;

import com.apps.philipps.source.BreathInterpreter;
import com.apps.philipps.source.Coins;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;
import com.apps.philipps.source.helper._3D.Renderer3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * GameEngine
 * this class controls and draw objects in game 3D
 **/
public class GameEngine {
    private ArrayList<GameObject3D> street = new ArrayList<>();
    private ArrayList<GameObject3D> streetleft = new ArrayList<>();
    private ArrayList<GameObject3D> streetright = new ArrayList<>();
    private float MIN_SPEED = 0.03f;
    private float SPEED = MIN_SPEED;
    private float MAX_SPEED = 0.15f;
    private float INCR_SPEED = 0.001f;
    private float current_camAngle = Renderer3D.start_cam_Angle;
    private float max_CamAngle = 0f;
    private float min_CamAngle = Renderer3D.start_cam_Angle;
    private float zOffset = -0.2f;
    private float relativeDistanceOfEnemies = 10.0f;
    private float safeDistance = 1.0f;
    float carY_Position = -1.4f;
    private int numberOfEnemies = 2;
    private float minDistanceToMainCar = 3f;
    private boolean isRunning = true;
    private boolean failed = false;

    public CollisionDetectionThread collisionDetectionThread;
    private Context mActivityContext;

    public Car car;
    public ArrayList<Enemy> enemies;
    public static float streetSize = 0.7f;

    private MediaPlayer myMediaPlayer;
    private boolean isBackgroundMusicPlaying = false;
    /**
     * Set true to draw bounding box to debug
     */
    private boolean DEBUG_MODE = false;

    /**
     * create street, carBody and enemies
     */
    public GameEngine(Context mActivityContext) {
        this.mActivityContext = mActivityContext;
        createCar();
        createEnemies();
        createStreet();
        collisionDetectionThread = new CollisionDetectionThread();
        collisionDetectionThread.start();
        playBackgroundMusic();
    }

    /**
     * draws objects
     */
    public void runGame(long deltaTime) {
        if (isRunning()) {
            if (Backend.highscore < Backend.score) {
                Backend.highscore = Backend.score;
            }
            rotateCam();
            drawStreet(deltaTime);
            runSimulation(deltaTime);
            if (!isBackgroundMusicPlaying && !collisionDetectionThread.crashed)
                playBackgroundMusic();
            if (!PlanManager.isActive()) {
                PlanManager.stop();
                while (current_camAngle < min_CamAngle)
                    resetCamAngle();
                Backend.saveHighScore(Backend.gName, Backend.score);
                Coins.buy(Backend.score, mActivityContext);
                pause(false);
            }
        }
    }

    /**
     * simulate enemies moving and calculate collisions
     */
    public void runSimulation(long deltaTime) {
        if (!collisionDetectionThread.crashed) {
            car.runsWithSpeed(SPEED);
        } else {
            car.crashes();
            SPEED = MIN_SPEED;
            if (isBackgroundMusicPlaying) {
                playCrashMusic();
                if (Backend.score > 0) {
                    Backend.minusScore = Backend.score - (Backend.score / 2);
                    Backend.score = Backend.score / 2;
                    failed = true;
                }
            }
        }
        car.draw(deltaTime);

        if (DEBUG_MODE) {
            car.drawBoundingBoxLines();
        }
        enemiesRun(deltaTime);
    }

    private void createCar() {
        car = new Car();
        car.setCarBodyModel(mActivityContext, R.raw.carbody, getTextureID());
        car.setCarBodyPosition(new Vector(0, carY_Position, zOffset));
        car.setCarTireModel(mActivityContext, R.raw.tire1, R.drawable.tiretexture, true);
        car.setCarTireModel(mActivityContext, R.raw.tire2, R.drawable.tiretexture, true);
        car.setCarTireModel(mActivityContext, R.raw.tire3, R.drawable.tiretexture, false);
        car.setCarTireModel(mActivityContext, R.raw.tire4, R.drawable.tiretexture, false);
    }

    private int getTextureID() {
        int index = -1;
        for (int i = 0; i < Backend.options.size(); i++) {
            if (Backend.options.getOption(i).isSet())
                index = i;
        }
        switch (index) {
            case 0:
                return R.drawable.cartexture2;
            case 1:
                return R.drawable.cartexture3;
            case 2:
                return R.drawable.cartexture4;
            default:
                return R.drawable.cartexture;
        }
    }

    private void createEnemies() {
        enemies = new ArrayList<>();
        for (int i = 0; i < numberOfEnemies; i++) {
            enemies.add(createNewEnemy());
        }
    }

    private void enemiesRun(long deltaTime) {
        //generate random turn
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            //runsWithSpeed
            enemy.runsWithSpeed(SPEED / 2.0f);
            enemy.draw(deltaTime);

            if (DEBUG_MODE)
                enemy.drawBoundingBoxLines();
        }
    }

    private Enemy createNewEnemy() {
        Random random = new Random();
        Enemy newEnemy = new Enemy();
        newEnemy.setCarBodyModel(mActivityContext, R.raw.enemycar, R.drawable.enemytexture);
        newEnemy.setCarTireModel(mActivityContext, R.raw.tire1enemy, R.drawable.tiretexture, true);
        newEnemy.setCarTireModel(mActivityContext, R.raw.tire2enemy, R.drawable.tiretexture, true);
        newEnemy.setCarTireModel(mActivityContext, R.raw.tire5enemy, R.drawable.tiretexture, false);
        newEnemy.setCarTireModel(mActivityContext, R.raw.tire6enemy, R.drawable.tiretexture, false);

        newEnemy.setCarBodyPosition(new Vector(random.nextFloat() * streetSize, random.nextFloat() * relativeDistanceOfEnemies + minDistanceToMainCar, zOffset));
        while (enemiesOverlapped(newEnemy)) {
            newEnemy.setCarBodyPosition(new Vector(0, random.nextFloat() * relativeDistanceOfEnemies + minDistanceToMainCar, zOffset));
        }
        return newEnemy;
    }

    private boolean enemiesOverlapped(Enemy newEnemy) {
        for (int i = 0; i < enemies.size(); i++) {
            if (newEnemy != enemies.get(i) &&
                    Math.abs(newEnemy.getCarBodyObject3D().getPosition().get(1) - enemies.get(i).getCarBodyObject3D().getPosition().get(1)) < safeDistance) {
                return true;
            }
        }
        return false;
    }

    private void drawStreet(long deltaTime) {
        if (street.get(0).getPosition().get(1) > -(streetSize * 4)) {
            moveStreet();
        } else {
            refreshStreet();
            moveStreet();
        }
        drawSquares(deltaTime);
    }

    private void createStreet() {
        for (int i = -2; i < 4; i++) {
            GameObject3D str = new GameObject3D(new Shapes.Square(mActivityContext, streetSize, R.drawable.newstreet));
            str.setPosition(new Vector(0, i * streetSize * 2, 0));
            street.add(str);

            GameObject3D strl = new GameObject3D(new Shapes.Square(mActivityContext, streetSize, R.drawable.stone));
            strl.setPosition(new Vector(-streetSize * 2, i * streetSize * 2, 0));
            streetleft.add(strl);

            GameObject3D strr = new GameObject3D(new Shapes.Square(mActivityContext, streetSize, R.drawable.water));
            strr.setPosition(new Vector(streetSize * 2, i * streetSize * 2, 0));
            streetright.add(strr);
        }
    }

    private void refreshStreet() {
        float f = (float) street.get(street.size() - 1).getPosition().get(1);
        street.get(0).setPosition(new Vector(0, f + streetSize * 2, 0));
        streetleft.get(0).setPosition(new Vector(-2 * streetSize, f + streetSize * 2, 0));
        streetright.get(0).setPosition(new Vector(2 * streetSize, f + streetSize * 2, 0));
        Collections.rotate(street, -1);
        Collections.rotate(streetleft, -1);
        Collections.rotate(streetright, -1);
    }

    private void moveStreet() {
        for (int i = 0; i < street.size(); i++) {
            street.get(i).move(new Vector(0, -SPEED, 0));
            streetleft.get(i).move(new Vector(0, -SPEED, 0));
            streetright.get(i).move(new Vector(0, -SPEED, 0));
        }
    }

    private void drawSquares(long deltaTime) {
        for (int i = 0; i < street.size(); i++) {
            street.get(i).update(deltaTime);
            streetleft.get(i).update(deltaTime);
            streetright.get(i).update(deltaTime);
        }
    }

    private void increaseCamAngle() {
        if (current_camAngle > max_CamAngle) {
            float speed = 2f;
            current_camAngle -= speed;
            Renderer3D.camera3D.move(new Vector(), new Vector(), new Vector(), new Vector(1, 0, 0, -speed));
        }
    }

    private void decreaseCamAngle() {
        if (current_camAngle < min_CamAngle) {
            float speed = 1f;
            current_camAngle += speed;
            Renderer3D.camera3D.move(new Vector(), new Vector(), new Vector(), new Vector(1, 0, 0, speed));
        }
    }

    public void resetCamAngle() {
        decreaseCamAngle();
    }

    private void rotateCam() {
        if (collisionDetectionThread.crashed) {
            increaseCamAngle();
        } else {
            resetCamAngle();
        }
    }

    /**
     * increase game speed
     */
    public void increaseCarSpeed() {
        if (SPEED < MAX_SPEED) {
            SPEED += INCR_SPEED;
        }
    }

    /**
     * decrease game speed
     */
    public void decreaseCarSpeed() {
        if (SPEED > MIN_SPEED) {
            SPEED -= INCR_SPEED;
        }
    }

    public void pause(boolean isRunning) {
        stopBackgroundMusic();
        this.isRunning = isRunning;
    }

    private void playBackgroundMusic() {
        if (myMediaPlayer != null)
            myMediaPlayer.release();
        myMediaPlayer = MediaPlayer.create(mActivityContext, Backend.getDefault_music_resource_id());
        myMediaPlayer.setLooping(true);
        myMediaPlayer.start();
        isBackgroundMusicPlaying = true;
    }

    private void playCrashMusic() {
        if (myMediaPlayer != null)
            myMediaPlayer.release();
        myMediaPlayer = MediaPlayer.create(mActivityContext, R.raw.tiresound);
        myMediaPlayer.setLooping(false);
        myMediaPlayer.start();
        isBackgroundMusicPlaying = false;
    }

    private void stopBackgroundMusic() {
        myMediaPlayer.release();
        isBackgroundMusicPlaying = false;
    }

    private void validateBreath() {
        if (BreathInterpreter.getStatus().getError() != BreathInterpreter.BreathError.VeryGood
                && BreathInterpreter.getStatus().getError() != BreathInterpreter.BreathError.None) {
            increaseCarSpeed();
        } else {
            decreaseCarSpeed();
        }
    }


    public boolean isRunning() {
        return isRunning;
    }

    public class CollisionDetectionThread extends Thread {
        boolean crashed = false;
        Random random = new Random();


        @Override
        public void run() {
            try {
                //sleep to fix bug when the bounding boxes are created and immediately recognize that is a collision
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (isRunning) {
                validateBreath();
                collisionDetection();
                enemiesSimulation();
            }
        }

        public void enemiesSimulation() {
            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                float r = generateRandomNumber();
                if (r > 0.5f && !enemy.isTurningLeft() && !enemy.isTurningRight()) {
                    enemy.setTurningLeft(true);
                } else if (r <= 0.5f && !enemy.isTurningLeft() && !enemy.isTurningRight()) {
                    enemy.setTurningRight(true);
                }
                enemy.setCounter(enemy.getCounter() + 1);
            }
        }

        public boolean isCrashed() {
            return crashed;
        }

        private float generateRandomNumber() {
            return random.nextFloat();
        }

        private void collisionDetection() {
            for (int i = 0; i < enemies.size(); i++) {
                car.renewBoundingBoxPosition();
                Enemy enemy = enemies.get(i);
                float r = collisionDetectionThread.generateRandomNumber();
                //set turning or right for enemies
                int timeToStartTurn = 100;
                if (enemy.getCounter() > timeToStartTurn) {
                    int timeToEndTurn = 200;
                    if (enemy.isTurningLeft()) {
                        enemy.turnLeft(0.02f * r);
                        if (enemy.getCounter() > timeToEndTurn) {
                            enemy.setCounter(0);
                            enemy.setTurningLeft(false);
                        }
                    } else if (enemy.isTurningRight()) {
                        enemy.turnRight(0.02f * r);
                        if (enemy.getCounter() > timeToEndTurn) {
                            enemy.setCounter(0);
                            enemy.setTurningRight(false);
                        }
                    }
                }
                //set enemy, which is out of the screen to new position
                if (enemy.getCarBodyObject3D().getPosition().get(1) < -2.0f) {
                    enemy.setCarBodyPosition(new Vector(0, collisionDetectionThread.generateRandomNumber() * relativeDistanceOfEnemies + minDistanceToMainCar, zOffset));
                    while (enemiesOverlapped(enemy)) {
                        enemy.setCarBodyPosition(new Vector(0, collisionDetectionThread.generateRandomNumber() * relativeDistanceOfEnemies + minDistanceToMainCar, zOffset));
                    }
                    if (failed)
                        failed = false;
                    else
                        Backend.score++;
                }
                enemy.renewBoundingBoxPosition();
                if (car.getCarBodyObject3D() != null
                        && car.getCarBodyObject3D().getBoundingBox().collision(enemy.getCarBodyObject3D().getBoundingBox())) {
                    crashed = true;
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            crashed = false;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}