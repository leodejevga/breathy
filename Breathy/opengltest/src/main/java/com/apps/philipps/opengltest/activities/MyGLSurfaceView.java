package com.apps.philipps.opengltest.activities;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.apps.philipps.source.helper._3D.SurfaceView3D;

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
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                if (dx > 0)
                    ((MyGLRenderer) renderer).gameEngine.car.turnRight(0.01f);
                else if (dx < 0)
                    ((MyGLRenderer) renderer).gameEngine.car.turnLeft(0.01f);
                break;
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}