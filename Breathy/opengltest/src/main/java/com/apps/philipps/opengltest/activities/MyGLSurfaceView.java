package com.apps.philipps.opengltest.activities;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.apps.philipps.source.helper.Vector;
import com.apps.philipps.source.helper._3D.Renderer3D;
import com.apps.philipps.source.helper._3D.SurfaceView3D;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class MyGLSurfaceView extends SurfaceView3D {

    private static final float SPEED = (float)Math.PI/100;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;
    private ScaleGestureDetector mScaleDetector;

    public MyGLSurfaceView(Context context, Renderer3D renderer3D) {
        super(context, renderer3D);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Renderer3D.camera3D.move(Renderer3D.camera3D.getPosition().add(new Vector(0, 0, detector.getScaleFactor())));
                Log.d("ZOOM", "zoom ongoing, scale: " + detector.getScaleFactor());
                return false;
            }
        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();
//        mScaleDetector.onTouchEvent(e);

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                if (dy > 0)
                    ((MyGLRenderer) renderer).setAngle(((MyGLRenderer) renderer).getAngle() + SPEED);
                else
                    ((MyGLRenderer) renderer).setAngle(((MyGLRenderer) renderer).getAngle() - SPEED);
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}