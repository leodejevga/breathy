package com.apps.philipps.source.helper._3D;

import android.app.Activity;
import android.util.Log;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.PlanManager;

/**
 * Created by Jevgenij Huebert on 09.03.2017. Project Breathy
 */
public abstract class Activity3D extends Activity {
    protected final String TAG = getClass().getSimpleName();
    /**
     * The Open gl.
     */
    protected SurfaceView3D openGL;

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "OnPause");
        BreathData.saveRest();
        PlanManager.pause();
        AppState.recordData = AppState.inGame = false;
        if (openGL != null)
            openGL.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume");
        AppState.recordData = AppState.inGame = true;
        if (PlanManager.isActive())
            PlanManager.resume();
        else
            PlanManager.start();
        openGL.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "OnDestroy");
        PlanManager.stop();
    }
}
