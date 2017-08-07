package com.apps.philipps.opengltest.activities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.apps.philipps.source.helper._3D.SurfaceView3D;

/**
 * Created by Jevgenij Huebert on 05.04.2017. Project Breathy
 */

public class MyGLSurfaceView extends SurfaceView3D {
    private float mPreviousX;
    private float mPreviousY;

    public MyGLSurfaceView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setWillNotDraw(false);
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

                if (dx > 0)
                    ((MyGLRenderer) renderer).gameEngine.car.turnRight(dx);
                else if (dx < 0)
                    ((MyGLRenderer) renderer).gameEngine.car.turnLeft(dx);
                break;

        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}