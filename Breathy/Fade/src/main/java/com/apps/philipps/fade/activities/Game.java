package com.apps.philipps.fade.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.apps.philipps.fade.R;
import com.apps.philipps.fade.TransparencyService;

public class Game extends Activity implements OnClickListener {

    private final static String TAG = "Game";

    public final static String BROADCAST_R_INTENT_FILTER_ACTION = "MainBroadcastReceiver";

    private Intent svc;

    private Button btnStartService;
    private Button btnStopService;
    private Button btnAutoChangeTransparency;

    private MainBroadcastReceiver mainBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fade_game);

        if (!checkOverlayPermission()){
            askForOverlayPermission();
        }

        initGUIComponents();

        svc = new Intent(this, TransparencyService.class);
        if (TransparencyService.isThreadAlive()) {
            setGUIElementsToStartedService();
        }

        mainBroadcastReceiver = new MainBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_R_INTENT_FILTER_ACTION);
        registerReceiver(mainBroadcastReceiver, intentFilter);

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)){ // Permission is denied
            return false;
        }
        return true;
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
        btnStartService = (Button) findViewById(R.id.btnStartActivity2);
        btnStartService.setOnClickListener(this);

        btnStopService = (Button) findViewById(R.id.btnStopActivity2);
        btnStopService.setEnabled(false);
        btnStopService.setOnClickListener(this);

        btnAutoChangeTransparency = (Button) findViewById(R.id.btnAutoChangeTransparency);
        btnAutoChangeTransparency.setEnabled(false);
        btnAutoChangeTransparency.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mainBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStartActivity2) {
            startTranspearencyServiceAndNotification();
        } else if (v.getId() == R.id.btnAutoChangeTransparency) {
            if (TransparencyService.isThreadRunning()) {
                TransparencyService.setThreadRunning(false);
                btnAutoChangeTransparency.setText("Transparenz automatisch Ändern");
            } else {
                TransparencyService.setThreadRunning(true);
                btnAutoChangeTransparency.setText("Transparenz nicht Ändern");
            }
            Log.i(TAG, "TransparencyService.setNewAlpha()");
        } else if (v.getId() == R.id.btnStopActivity2) {
            stopTranspearencyServiceAndNotification();
        }
    }

    private void startTranspearencyServiceAndNotification() {
        if (checkOverlayPermission()){
            startService(svc);
            setGUIElementsToStartedService();
        }else {
            // OverlayPermission fehlt. Rechte Anfragen.
            askForOverlayPermission();
            // TODO Benutzer auf Erforderlichkeit der Rechte hinweisen
        }


    }

    private void setGUIElementsToStartedService() {
        btnStartService.setEnabled(false);
        btnStopService.setEnabled(true);
        btnAutoChangeTransparency.setEnabled(true);
    }

    private void stopTranspearencyServiceAndNotification() {
        if (stopService(svc)) {
            Log.i(TAG, "TransparencyService stopped");
            resetGUIElements();
        } else {
            Log.e(TAG, "TransparencyService NOT stopped");
        }
    }

    private void resetGUIElements() {
        btnStartService.setEnabled(true);
        btnStopService.setEnabled(false);
        btnAutoChangeTransparency.setEnabled(false);
        btnAutoChangeTransparency.setText("Transparenz nicht Ändern");
    }

    public static class MainBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "MainBroadcastReceiver:  onReceive()");
            if (intent.getIntExtra(TransparencyService.EXTRA_NEXT_STATE, 0) == TransparencyService.EXTRA_VALUE_STOP) {
                ((Game)context).resetGUIElements();
            }
        }
    }
}
