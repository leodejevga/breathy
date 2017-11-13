package com.apps.philipps.source.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathData;
import com.apps.philipps.source.PlanManager;
import com.apps.philipps.source.R;
import com.apps.philipps.source.interfaces.IObserver;

/**
 * Created by Jevgenij Huebert on 13.11.2017. Project Breathy.
 */

public abstract class BreathyActivity extends Activity implements IObserver {
    protected final String TAG = getClass().getSimpleName();


    @Override
    public void call(Object... messages) {
        if (!AppState.simulateBreathy)
            if (messages.length != 0 && messages[0] instanceof AppState.BtState) {
                AppState.BtState state = (AppState.BtState) messages[0];
                if (state != AppState.BtState.Connected) {
                    onPause();

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.bt_error);
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            getString(R.string.exit),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    BreathyActivity.super.onBackPressed();
                                    dialog.cancel();
                                }
                            });

                    builder.setNegativeButton(
                            getString(R.string.reconnect),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    BreathyActivity.this.onResume();
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    onResume();
                }
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "OnPause");
        if (AppState.inGame) {
            PlanManager.pause();
        }
        AppState.recordData = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "OnDestroy");
        if (AppState.inGame) {
            BreathData.save(getClass());
            PlanManager.stop();
            AppState.inGame = false;
        }
        BreathData.removeObserver(this);
        AppState.removeBTPObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume");
        AppState.recordData = true;
        if (AppState.inGame) {
            PlanManager.resume();
        }
        BreathData.addObserver(this);
        AppState.addBTPObserver(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppState.inGame = true;
    }

    @Override
    public void onBackPressed() {
        onPause();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.really_exit);
        builder.setCancelable(true);

        builder.setPositiveButton(
                getString(R.string.exit),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BreathyActivity.super.onBackPressed();
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                getString(R.string.con),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BreathyActivity.this.onResume();
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
