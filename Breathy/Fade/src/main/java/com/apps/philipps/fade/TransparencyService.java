package com.apps.philipps.fade;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.apps.philipps.fade.activities.Game;
import com.apps.philipps.source.AppState;
import com.apps.philipps.source.BreathInterpreter;
import com.apps.philipps.source.PlanManager;

public class TransparencyService extends Service{
    private final String TAG = "TransparencyService";

    public static final String KEY_NEXT_STATE = "KEY_NEXT_STATE";
    public static final String KEY_FOG_COLOR = "KEY_FOG_COLOR";

    public static final int EXTRA_STATE_NEW_SERVICE = 1000;
    public static final int EXTRA_STATE_CONTINUE = 1001;
    public static final int EXTRA_STATE_RUNNING = 1002;
    public static final int EXTRA_STATE_PAUSED = 1003;
    public static final int EXTRA_STATE_STOPPED = 1004;

    private View topLeftView;
    private View fullscreenFogView;

    private static boolean threadAlive;
    private static boolean threadRunning;
    private boolean setPausedNotification = false;


    private static float currentAlpha;
    private static WindowManager wm;

    // Used by the Fade-Notifier
    private static Intent serviceIntent;

    Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand() - Threadalive: " + threadAlive +  " - Threadrunning: " + threadRunning);

        int nextState = EXTRA_STATE_NEW_SERVICE;
        if (intent != null) {
            TransparencyService.serviceIntent = intent;
            nextState = TransparencyService.serviceIntent.getIntExtra(TransparencyService.KEY_NEXT_STATE, -1);
        } else {
            Log.e(TAG, "onStartCommand() - intent == null !!!");
            TransparencyService.serviceIntent = new Intent(this, TransparencyService.class);
        }

        switch(nextState){
            case TransparencyService.EXTRA_STATE_NEW_SERVICE:
                Log.i(TAG, "onStartCommand() EXTRA_STATE_NEW_SERVICE");
                TransparencyService.threadAlive = false;
                setThreadRunning(false);

                handler = new Handler();

                // Start Breathplan
                AppState.recordData = false;

                PlanManager.start();
                Log.i(TAG, "PlanManager.start() - isActive(): " + PlanManager.isActive());

                int fogColor = intent.getIntExtra(TransparencyService.KEY_FOG_COLOR, getResources().getColor(R.color.standardColor3));
                if (fogColor == getResources().getColor(R.color.standardColor1)) {
                    Log.i(TAG, "FogColor not changed!?");
                }
                initTransparencyObjects(fogColor);

                startUpdateThread();

                startOrUpdateTransparencyNotification();
                break;
            case TransparencyService.EXTRA_STATE_CONTINUE:
                setThreadRunning(true);
                fullscreenFogView.setVisibility(View.VISIBLE);

                PlanManager.resume();
                Log.i(TAG, "PlanManager.resume() - isActive(): " + PlanManager.isActive());

                notifyFadeGameActivity(TransparencyService.EXTRA_STATE_CONTINUE);
                Log.i(TAG, "onStartCommand() EXTRA_STATE_CONTINUE - Threadrunning: " + threadRunning);
                break;
            case TransparencyService.EXTRA_STATE_PAUSED:
                setThreadRunning(false);
                fullscreenFogView.setVisibility(View.INVISIBLE);

                PlanManager.pause();
                Log.i(TAG, "PlanManager.pause() - isActive(): " + PlanManager.isActive());

                notifyFadeGameActivity(TransparencyService.EXTRA_STATE_PAUSED);
                Log.i(TAG, "onStartCommand() EXTRA_STATE_PAUSED - Threadrunning: " + threadRunning);
                break;
            case TransparencyService.EXTRA_STATE_STOPPED:
                Log.i(TAG, "onStartCommand() EXTRA_STATE_STOPPED");
                // NOTIFICATION will be deleted in onDestroy()
                // PlanManager.stop() will be called in onDestroy()
                onDestroy();
                break;
            default:
                Log.e(TAG, "onStartCommand() NO NEXT STATE VALUE - KEY_NEXT_STATE = -1!!");
        }
        return Service.START_STICKY_COMPATIBILITY;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initTransparencyObjects(int fogColor) {
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        TransparencyService.currentAlpha = 0.0f; // No Fog

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        fullscreenFogView = new View(this);
        fullscreenFogView.setFocusable(false);
        fullscreenFogView.setAlpha(currentAlpha / 100);
        fullscreenFogView.setBackgroundColor(fogColor);

        wm.addView(fullscreenFogView, params);

        // ToDo topLeftParams???
        topLeftView = new View(this);
        WindowManager.LayoutParams topLeftParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);
    }
    private void startUpdateThread() {
        TransparencyService.threadAlive = true;
        setThreadRunning(true);
        new Thread() {
            public void run() {
                while (threadAlive) {
                    // ToDo
                    //fullscreenFogView.setVisibility(View.INVISIBLE);
                    while (threadRunning) {
                        setPausedNotification = false;
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setNewAlpha();
                                    updateView();
                                    startOrUpdateTransparencyNotification();
                                }
                            });
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (threadAlive && !setPausedNotification){
                        setPausedNotification = true;
                        startOrUpdateTransparencyNotification();
                    }
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        TransparencyService.threadAlive = false;
        setThreadRunning(false);

        // Stop Breathplan
        PlanManager.stop();

        removeAllViews();

        stopTransparencyNotification();

        notifyFadeGameActivity(EXTRA_STATE_STOPPED);

        super.onDestroy();
    }

    private void notifyFadeGameActivity(int nextState) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(Game.FADE_BROADCAST_R_INTENT_FILTER);
        intent.putExtra(KEY_NEXT_STATE, nextState);

        Log.i(TAG, "sendBroadcast(): " + nextState);
        sendBroadcast(intent);
    }

    private void removeAllViews() {
        wm.removeView(fullscreenFogView);
//        wm.removeView(topLeftView);
        fullscreenFogView = null;
        topLeftView = null;
        wm = null;
    }
    private void startOrUpdateTransparencyNotification() {
        Log.v(TAG, "startOrUpdateTransparencyNotification()");
        if (isThreadRunning()) {
            // Thread laeuft > M�glichkeit den Thead zu pausieren in Notification einfuegen
            TransparencyNotification.notify(this, TransparencyNotification.NOTIFICATION_PAUSE ,getString(R.string.notification_exercise_runnung), getPractiseInformation(), 1);
        } else {
            // Thread laeuft nicht > M�glichkeit den Thead zu fortzusetzen in Notification einfuegen
            TransparencyNotification.notify(this, TransparencyNotification.NOTIFICATION_CONTINUE ,getString(R.string.notification_exercise_paused), getPractiseInformation(), 1);
        }
    }
    private void stopTransparencyNotification() {
        Log.i(TAG, "stopTransparencyNotification()");
        TransparencyNotification.cancel(this);
    }

    private String getRemainingTimeString(){
        long seconds = PlanManager.getDuration() / 1000;
        return (seconds / 60 != 0 ? (seconds / 60) + ":" : "")
                + (seconds != 0 ? seconds % 60 + ":" : "")
                + PlanManager.getDuration() % 1000;
    }

    private String getPractiseInformation() {
        return (100 - TransparencyService.getAlpha()) + "% - " + BreathInterpreter.getStatus().getError() + " - " + getRemainingTimeString();
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public void setNewAlpha() {
        switch(BreathInterpreter.getStatus().getError()){
            case VeryGood:
                currentAlpha = 0.0f;
                break;
            case Good:
                currentAlpha = 16.6f;
                break;
            case Ok:
                currentAlpha = 33.3f;
                break;
            case NotOk:
                currentAlpha = 50.0f;
                break;
            case NotGood:
                currentAlpha = 66.6f;
                break;
            case Bad:
                currentAlpha = 83.3f;
                break;
            case VeryBad:
                currentAlpha = 100.0f;
                break;
            default:
                Log.e(TAG, "Kein Status!");
                currentAlpha = 100.0f;
        }

        fullscreenFogView.setAlpha(currentAlpha / 100);
    }
    private void updateView() {
        fullscreenFogView.setVisibility(View.VISIBLE);

        WindowManager.LayoutParams params = (LayoutParams) fullscreenFogView.getLayoutParams();
        wm.updateViewLayout(fullscreenFogView, params);
    }

    public static boolean isThreadRunning() {
        return threadRunning;
    }
    public static void setThreadRunning(boolean t) {
        TransparencyService.threadRunning = t;
    }
    public static boolean isThreadAlive() {
        return threadAlive;
    }
    public static void setThreadAlive(boolean threadAlive) {
        TransparencyService.threadAlive = threadAlive;
    }
    public static float getAlpha() {
        return currentAlpha;
    }


    public static Intent getServiceIntent() {
        return serviceIntent;
    }
}