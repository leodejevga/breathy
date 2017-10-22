package com.apps.philipps.source.helper._3D;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
        PlanManager.pause();
        AppState.recordData = false;
        if (openGL != null)
            openGL.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume");
        AppState.inGame = true;
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
        BreathData.saveRest();
        AppState.inGame = false;
        PlanManager.stop();
        BreathData.save(getClass());
    }

    @Override
    public void onBackPressed() {
        onPause();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you really want to exit? Your coins will be lost and its not a good idea to interrupt the session.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Exit the game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Activity3D.super.onBackPressed();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Continue the game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Activity3D.this.onResume();
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
