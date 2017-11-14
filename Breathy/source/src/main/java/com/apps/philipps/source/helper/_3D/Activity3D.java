package com.apps.philipps.source.helper._3D;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.helper.BreathyActivity;

/**
 * Created by Jevgenij Huebert on 09.03.2017. Project Breathy
 */
public abstract class Activity3D extends BreathyActivity {
    /**
     * The Open gl.
     */
    protected SurfaceView3D openGL;

    @Override
    protected void onPause() {
        super.onPause();
        if (openGL != null)
            openGL.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        openGL.onResume();
    }
}
