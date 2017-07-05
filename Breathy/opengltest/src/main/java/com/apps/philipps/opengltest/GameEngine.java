package com.apps.philipps.opengltest;

import android.content.Context;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;
import com.apps.philipps.source.helper._3D.Renderer3D;

import java.util.ArrayList;
import java.util.Random;

/**
 * GameEngine
 * this class controls and draw objects in game 3D
 **/
public class GameEngine {
    private ArrayList<GameObject3D> street = new ArrayList<>();
    private float SPEED = 0.01f;
    private float MIN_SPEED = 0.01f;
    private float MAX_SPEED = 0.1f;
    private float INCR_SPEED = 0.001f;
    private float current_camAngle = Renderer3D.start_cam_Angle;
    private float max_CamAngle = 0f;
    private float min_CamAngle = Renderer3D.start_cam_Angle;
    private float zOffset = -0.2f;
    private float relativeDistanceOfEnemies = 20.0f;
    private float safeDistance = 1.0f;
    private int numberOfEnemies = 5;

    private CollisionDetectionThread collisionDetectionThread;
    private Context mActivityContext;

    public Car car;
    public ArrayList<EnemyBody> enemies;
    public static float streetSize = 0.7f;

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
    }

    /**
     * draws objects
     */
    public void runGame(long deltaTime) {
        rotateCam();
        Renderer3D.light.setUpLight();
        drawStreet(deltaTime);
        runSimulation(deltaTime);
        Renderer3D.light.drawLight();
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
        }
        car.draw(deltaTime);

        if (DEBUG_MODE) {
            car.drawBoundingBoxLines();
        } else
            car.renewBoundingBoxPosition();

        enemiesRun(deltaTime);
    }

    private void createCar() {
        car = new Car();
        car.setCarBodyModel(mActivityContext, R.raw.newcar, R.drawable.cartexture);
        car.setCarBodyPosition(new Vector(0, -1.4f, zOffset));

        car.setCarTireModel(mActivityContext, R.raw.tire1, R.drawable.tiretexture, true);
        car.setCarTireModel(mActivityContext, R.raw.tire2, R.drawable.tiretexture, true);
        car.setCarTireModel(mActivityContext, R.raw.tire3, R.drawable.tiretexture, false);
        car.setCarTireModel(mActivityContext, R.raw.tire4, R.drawable.tiretexture, false);
        car.setTirePosition(new Vector(0, -1.4f, zOffset));
    }

    private void createEnemies() {
        enemies = new ArrayList<>();
        for (int i = 0; i < numberOfEnemies; i++) {
            enemies.add(createNewEnemy());
        }
    }

    private void enemiesRun(long deltaTime) {
        //generate random turn
        Random random = new Random();
        for (int i = 0; i < enemies.size(); i++) {
            float r = random.nextFloat();
            EnemyBody enemyBody = enemies.get(i);
            if (enemyBody.getCounter() > 120) {
                if (r > 0.5f && !enemyBody.isTurningLeft() && !enemyBody.isTurningRight()) {
                    enemyBody.setTurningLeft(true);
                } else if (r <= 0.5f && !enemyBody.isTurningLeft() && !enemyBody.isTurningRight()) {
                    enemyBody.setTurningRight(true);
                } else if (enemyBody.isTurningLeft()) {
                    enemyBody.turnRight(30f * r);
                    if (enemyBody.getCounter() > 180) {
                        enemyBody.setCounter(0);
                        enemyBody.setTurningLeft(false);
                    }
                } else if (enemyBody.isTurningRight()) {
                    enemyBody.turnLeft(30f * r);
                    if (enemyBody.getCounter() > 180) {
                        enemyBody.setCounter(0);
                        enemyBody.setTurningRight(false);
                    }
                }
            }
            //runsWithSpeed
            enemyBody.runsWithSpeed(SPEED / 2.0f);
            enemyBody.runs();
            enemyBody.draw(deltaTime);

            //set enemyBody, which is out of the screen to new position
            if (enemyBody.getObject3D().getPosition().get(1) < -2.0f) {
                enemyBody.setPosition(new Vector(0, random.nextFloat() * relativeDistanceOfEnemies + 1f, zOffset));
                while (enemiesOverlapped(enemyBody)) {
                    enemyBody.setPosition(new Vector(0, random.nextFloat() * relativeDistanceOfEnemies + 1f, zOffset));
                }
            }

            if (DEBUG_MODE)
                enemyBody.getObject3D().getBoundingBox().drawLines();
            else
                enemyBody.getObject3D().getBoundingBox().renewLinesPosition();
        }
    }

    private EnemyBody createNewEnemy() {
        Random random = new Random();
        EnemyBody newEnemyBody = new EnemyBody(mActivityContext, R.raw.enemycar, R.drawable.enemytexture);
        newEnemyBody.setPosition(new Vector(0, random.nextFloat() * relativeDistanceOfEnemies + 1f, zOffset));
        while (enemiesOverlapped(newEnemyBody)) {
            newEnemyBody = createNewEnemy();
        }
        return newEnemyBody;
    }

    private boolean enemiesOverlapped(EnemyBody newEnemyBody) {
        for (int i = 0; i < enemies.size(); i++) {
            if (newEnemyBody != enemies.get(i) &&
                    Math.abs(newEnemyBody.getObject3D().getPosition().get(1) - enemies.get(i).getObject3D().getPosition().get(1)) < safeDistance) {
                return true;
            }
        }
        return false;
    }

    private void drawStreet(long deltaTime) {
        if (street.get(street.size() / 2).getPosition().get(1) > -(streetSize * 2)) {
            moveStreet();
        } else {
            moveStreet();
            refreshStreet();
        }
        drawSquares(deltaTime);
    }

    private void createStreet() {
        for (int i = -9; i < 10; i++) {
            GameObject3D square = new GameObject3D(new Shapes.Square(mActivityContext, streetSize));
            square.setPosition(new Vector(0, i * streetSize * 2, 0));
            street.add(square);
        }
    }

    private void refreshStreet() {
        GameObject3D square = street.get(0);
        float f = street.get(street.size() - 1).getPosition().get(1);
        square.setPosition(new Vector(0, f + 1.0f, 0));
        street.add(square);
        street.remove(0);
    }

    private void moveStreet() {
        for (int i = 0; i < street.size(); i++) {
            street.get(i).move(new Vector(0, -SPEED, 0));
        }
    }

    private void drawSquares(long deltaTime) {
        for (int i = 0; i < street.size(); i++) {
            street.get(i).update(deltaTime);
        }
    }

    private void increaseCamAngle() {
        if (current_camAngle > max_CamAngle) {
            current_camAngle -= 2;
            Renderer3D.camera3D.move(new Vector(), new Vector(), new Vector(), new Vector(1, 0, 0, -2));
        }
    }

    private void decreaseCamAngle() {
        if (current_camAngle < min_CamAngle) {
            current_camAngle += 1;
            Renderer3D.camera3D.move(new Vector(), new Vector(), new Vector(), new Vector(1, 0, 0, 1));
        }
    }

    private void resetCamAngle() {
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

    class CollisionDetectionThread extends Thread {
        boolean crashed = false;

        @Override
        public void run() {
            while (true) {
                collisionDetection();
            }
        }

        private void collisionDetection() {
            for (int i = 0; i < enemies.size(); i++) {
                if (car.getCarBodyObject3D() != null
                        && car.getCarBodyObject3D().getBoundingBox().collision(enemies.get(i).getObject3D().getBoundingBox())) {
                    crashed = true;
                    try {
                        sleep(1000 / 40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            crashed = false;
            try {
                sleep(1000 / 40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}