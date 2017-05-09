package com.apps.philipps.opengltest.activities;

import android.opengl.Matrix;

import com.apps.philipps.opengltest.Shapes;
import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.GameObject3D;
import com.apps.philipps.source.helper._3D.Renderer3D;
import com.apps.philipps.test.activities.Game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class MyGLRenderer extends Renderer3D {

    private GameObject3D triangle;
    private GameObject3D square;
    private GameObject3D cube;
    private GameObject3D banana;

    private float mAngle;


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        super.onSurfaceCreated(unused, config);
        triangle = new GameObject3D(new Shapes.Triangle());
        square = new GameObject3D(new Shapes.Square());
        cube = new GameObject3D(GameObject3D.loadObject("Download/cube.OBJ"));
        banana = new GameObject3D(GameObject3D.loadObject("Download/banana.obj"));
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        super.onDrawFrame(unused);

        banana.update(deltaTime);
        cube.update(deltaTime);
        triangle.update(deltaTime);
        square.getRotation().add(new Vector(0,1,0,2));
        square.update(deltaTime);
    }
}