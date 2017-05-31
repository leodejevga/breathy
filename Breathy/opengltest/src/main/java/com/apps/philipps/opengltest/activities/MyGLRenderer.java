package com.apps.philipps.opengltest.activities;

import android.content.Context;

import com.apps.philipps.opengltest.Shapes;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;
import com.apps.philipps.source.helper._3D.Renderer3D;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class MyGLRenderer extends Renderer3D {
    private Context mActivityContext;

    private float angle = 0f;
    private float SPEED = 0.01f;

    ArrayList<GameObject3D> street = new ArrayList<>();
    GameObject3D triangle;

    public MyGLRenderer(Context context) {
        this.mActivityContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        super.onSurfaceCreated(unused, config);
        createStreet();
//        triangle = new GameObject3D(new Shapes.Triangle(mActivityContext));
        //drawStreet();
        //cube = new GameObject3D(GameObject3D.loadObject(mActivityContext, "Download/cube.OBJ"));
        //banana = new GameObject3D(GameObject3D.loadObject(mActivityContext, "Download/banana.obj"));
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        super.onDrawFrame(unused);
        refreshCameraPosition();

        Renderer3D.light.setUpLight();
        drawStreet();
   //     triangle.update(deltaTime);
        Renderer3D.light.drawLight();
    }


    private void refreshCameraPosition() {
        //Renderer3D.camera3D.move(new Vector(), new Vector(), new Vector(), new Vector(1, 0, 0, angle));
    }

    private void drawStreet() {
        //square.rotate(new Vector(0, 1, 0, 2));
        if (street.get(street.size() / 2).getPosition().get(1) > -1.0) {
            moveStreet();
        } else {
            moveStreet();
            refreshStreet();
        }
        drawSquares();
    }

    private void createStreet() {
        for (int i = -3; i < 4; i++) {
            GameObject3D square = new GameObject3D(new Shapes.Square(mActivityContext));
            square.setPosition(new Vector(0, i * 1.0f, 0));
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

    private void drawSquares() {
        for (int i = 0; i < street.size(); i++) {
            street.get(i).update(deltaTime);
        }
    }


    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}