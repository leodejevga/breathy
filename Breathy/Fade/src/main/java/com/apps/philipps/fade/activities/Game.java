package com.apps.philipps.fade.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.philipps.fade.R;
import com.apps.philipps.fade.TransparencyService;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.PlanManager;

public class Game extends Activity implements OnClickListener {

    private final static String TAG = "fade.Game";

    public final static String FADE_BROADCAST_R_INTENT_FILTER = "FADE_BROADCAST_R_INTENT_FILTER";

    private int currentState;
    private boolean threadAlive;
    private boolean threadRunning;

    private TextView txtRemainingTime;
    private TextView txtExerciseState;

    private Button btnStartPauseService;
    private Button btnStopService;

    private BroadcastReceiver gameActivityBroadcastReceiver;

    private SharedPreferences sharedPref;

    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fade_game);

        if (!checkOverlayPermission()){
            askForOverlayPermission();
        }

        sharedPref = getSharedPreferences("Fade.SharedPreferences", Context.MODE_PRIVATE);

        initGUIComponents();

        startUpdateThread();

        this.currentState = getServiceState();
        updateState(currentState);

        registerBroadCastReceiver();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkOverlayPermission() {
        if (Settings.canDrawOverlays(this)){
            return true;
        } else { // Permission is denied
            return false;
        }
    }

    private Intent getTransparencyServiceIntent(int serviceState, int fogColor){
        Intent i = new Intent(this, TransparencyService.class);
        i.putExtra(TransparencyService.KEY_NEXT_STATE, serviceState);
        i.putExtra(TransparencyService.KEY_FOG_COLOR, fogColor);

        return i;
    }

    private void askForOverlayPermission(){
        // Check if Android M or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(myIntent);
        }
    }

    private void initGUIComponents() {
        txtRemainingTime = (TextView) findViewById(R.id.txtRemainingTime);
        txtExerciseState = (TextView) findViewById(R.id.txtExerciseState);

        btnStartPauseService = (Button) findViewById(R.id.btnStartPauseService);
        btnStartPauseService.setOnClickListener(this);

        btnStopService = (Button) findViewById(R.id.btnStopService);
        btnStopService.setOnClickListener(this);
    }

    private int getServiceState(){
        int serviceState;
        if (TransparencyService.isThreadAlive()) {
            if (TransparencyService.isThreadRunning()){
                serviceState = TransparencyService.EXTRA_STATE_RUNNING;
            } else {
                serviceState = TransparencyService.EXTRA_STATE_PAUSED;
            }
        } else {
            serviceState = TransparencyService.EXTRA_STATE_STOPPED;
        }
        return serviceState;
    }

    @Override
    protected void onDestroy() {
        threadRunning = false;
        threadAlive = false;

        unregisterReceiver(gameActivityBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnStartPauseService.getId()) {
            Log.i(TAG, "onClick() -> btnStartPauseService currentState: " + currentState);
            boolean runFogServiceThread;
            switch(this.currentState){
                case TransparencyService.EXTRA_STATE_RUNNING:
                    runFogServiceThread = false;
                    pauseContinueFogService(runFogServiceThread);
                    break;
                case TransparencyService.EXTRA_STATE_PAUSED:
                    runFogServiceThread = true;
                    pauseContinueFogService(runFogServiceThread);
                    break;
                case TransparencyService.EXTRA_STATE_STOPPED:
                    startNewFogService();
                    break;
            }
            updateState(this.currentState);
        } else if (v.getId() == btnStopService.getId()) {
            stopNewFogService();
            updateState(this.currentState);
        }
    }

    private void startUpdateThread() {
        threadAlive = true;
        threadRunning = false;

        new Thread() {
            public void run() {
                try {
                    while (threadAlive) {
                        Thread.sleep(250);
                        while (threadRunning) {
                            Thread.sleep(100);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtRemainingTime.setText(getRemainingTimeString());
                                }
                            });
                        }
                    }
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }.start();
    }

    private String getRemainingTimeString(){
        PlanManager.update();
        long seconds = PlanManager.getDuration() / 1000;
        return (seconds / 60 != 0 ? (seconds / 60) + "min " : "")
                + (seconds != 0 ? seconds % 60 + "s" : "");
        //        + PlanManager.getDuration() % 1000;
    }



    private void updateState(int newState){
        switch(newState){
            case TransparencyService.EXTRA_STATE_RUNNING:
                threadRunning = true;

                txtExerciseState.setText(getString(R.string.running));

                btnStartPauseService.setText(getString(R.string.main_pause_exercise));
                btnStopService.setVisibility(View.VISIBLE);
                break;
            case TransparencyService.EXTRA_STATE_PAUSED:
                threadRunning = false;

                txtExerciseState.setText(getString(R.string.paused));

                btnStartPauseService.setText(getString(R.string.main_continue_exercise));
                btnStopService.setVisibility(View.VISIBLE);
                break;
            case TransparencyService.EXTRA_STATE_STOPPED:
                threadRunning = false;

                txtExerciseState.setText(getString(R.string.stopped));

                btnStartPauseService.setText(getString(R.string.main_start_exercise));
                txtRemainingTime.setText("");
                btnStopService.setVisibility(View.GONE);
                break;
        }
    }

    private void startNewFogService() {
        if(PlanManager.getCurrentPlan() != null) {
            if (checkOverlayPermission()){
                int fogColor = loadColor();
                Intent svc = getTransparencyServiceIntent(TransparencyService.EXTRA_STATE_NEW_SERVICE, fogColor);
                startService(svc);
                this.currentState = TransparencyService.EXTRA_STATE_RUNNING;
            } else {
                // OverlayPermission fehlt. Rechte Anfragen.
                askForOverlayPermission();
            }
        }
        else {
            Toast.makeText(context, "Bitte einen Plan auswählen!", Toast.LENGTH_SHORT);
        }
    }

    private void pauseContinueFogService(boolean runServiceThread) {
        // TransparencyService.setThreadRunning(runServiceThread);
        Log.i(TAG, "pauseContinueFogService() runServiceThread: " + runServiceThread);

        if (runServiceThread) {
            this.currentState = TransparencyService.EXTRA_STATE_CONTINUE;
            int fogColor = loadColor();
            Intent svc = getTransparencyServiceIntent(this.currentState, fogColor);
            startService(svc);
            this.currentState = TransparencyService.EXTRA_STATE_RUNNING;
        } else {
            this.currentState = TransparencyService.EXTRA_STATE_PAUSED;
            int fogColor = loadColor();
            Intent svc = getTransparencyServiceIntent(this.currentState, fogColor);
            startService(svc);
        }

    }

    private void stopNewFogService() {
        this.currentState = TransparencyService.EXTRA_STATE_STOPPED;

        int fogColor = loadColor();
        Intent svc = getTransparencyServiceIntent(TransparencyService.EXTRA_STATE_RUNNING, fogColor);
        if (!stopService(svc)){
            Log.e(TAG, "stopNewFogService() no Service found !!");
        }
    }

    private int loadColor() {
        int red = sharedPref.getInt(getString(R.string.com_apps_philipps_fade_preference_key_red), 127);
        int green = sharedPref.getInt(getString(R.string.com_apps_philipps_fade_preference_key_green), 127);
        int blue = sharedPref.getInt(getString(R.string.com_apps_philipps_fade_preference_key_blue), 127);
        if (red == 127 || blue == 127 || blue == 127) {
            Log.w(TAG, "FogColor not changed!?");
        }
        return Color.argb(221, red, green, blue);
    }

    private void registerBroadCastReceiver(){
        this.gameActivityBroadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                int nextState = intent.getIntExtra(TransparencyService.KEY_NEXT_STATE, -1);
                Log.i(TAG, "MainBroadcastReceiver:  onReceive() - nextState = " + nextState);
                if (nextState == TransparencyService.EXTRA_STATE_CONTINUE) {
                    ((Game) context).currentState = TransparencyService.EXTRA_STATE_RUNNING;
                    ((Game) context).updateState(currentState);
                } else if (nextState != -1) {
                    ((Game) context).updateState(nextState);
                    ((Game) context).currentState = nextState;
                } else {
                    Log.e(TAG, "onReceive() currentState = -1 !!");
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FADE_BROADCAST_R_INTENT_FILTER);
        registerReceiver(this.gameActivityBroadcastReceiver, intentFilter);
    }
}
