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

import com.apps.philipps.R;
import com.apps.philipps.fade.activities.Game;

public class TransparencyService extends Service{
    private final String TAG = "TransparencyService";

    public static final String EXTRA_NEXT_STATE = "EXTRA_NEXT_STATE";
    public static final int NEW_SERVICE = 1000;
    public static final int EXTRA_VALUE_PAUSE_CONTINUE = 1001;
    public static final int EXTRA_VALUE_STOP = 1002;

    private View topLeftView;
    private View testView;

    private static boolean threadAlive;
    private static boolean threadRunning;

    private static float currentAlpha;
    private static boolean alphaRise;
    private static WindowManager wm;

    private static Intent serviceIntent;

    Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int nextState = NEW_SERVICE;
        if (intent != null) {
            TransparencyService.serviceIntent = intent;
            nextState = TransparencyService.serviceIntent.getIntExtra(EXTRA_NEXT_STATE, 0);
        } else {
            TransparencyService.serviceIntent = new Intent(this, TransparencyService.class);
        }
        if (nextState == EXTRA_VALUE_PAUSE_CONTINUE) {
            threadRunning = !threadRunning;
            Log.i(TAG, "onStartCommand() EXTRA_VALUE_PAUSE_CONTINUE - Threadrunning: " + threadRunning);
            try {
                Thread.sleep(200);
                startOrUpdateTranspearencyNotification();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (nextState == EXTRA_VALUE_STOP) {
            Log.i(TAG, "onStartCommand() EXTRA_VALUE_STOP");
            // NOTIFICATION will also be deleted in onDestroy()
            onDestroy();
        } else {
            // nextState == NEW_SERVICE
            TransparencyService.threadAlive = false;
            setThreadRunning(false);

            handler = new Handler();

            initTransparencyObjects();

            startUpdateThread();

            startOrUpdateTranspearencyNotification();
            Log.i(TAG, "onStartCommand() NO NEXT STATE VALUE!!");
        }
        return 0;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initTransparencyObjects() {
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        TransparencyService.currentAlpha = 0.0f;
        TransparencyService.alphaRise = true;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        testView = new View(this);
        testView.setAlpha(currentAlpha);
        testView.setBackgroundColor(0xDD664422);
        testView.setFocusable(false);

        wm.addView(testView, params);

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
                    while (threadRunning) {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setNewAlpha();
                                    updateView();
                                    startOrUpdateTranspearencyNotification();
                                }
                            });
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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

        removeAllViews();

        stopTranspearencyNotification();

        notifyMainActivity();

        super.onDestroy();
    }

    private void notifyMainActivity() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(Game.BROADCAST_R_INTENT_FILTER_ACTION);
        intent.putExtra(EXTRA_NEXT_STATE, EXTRA_VALUE_STOP);

        Log.i(TAG, "sendBroadcast()");
        sendBroadcast(intent);
    }
    private void removeAllViews() {
        wm.removeView(testView);
        wm.removeView(topLeftView);
        testView = null;
        topLeftView = null;
        wm = null;
    }

    private void updateView() {
        testView.setAlpha(currentAlpha / 100);
        WindowManager.LayoutParams params = (LayoutParams) testView.getLayoutParams();
        wm.updateViewLayout(testView, params);
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public void setNewAlpha() {
        if (currentAlpha >= 100.0f) {
            alphaRise = false;
        } else if (currentAlpha <= 0.0f) {
            alphaRise = true;
        }
        if (alphaRise) {
            // TODO
            currentAlpha = currentAlpha + 10;
        } else {
            currentAlpha = currentAlpha - 10;
        }
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

    private void startOrUpdateTranspearencyNotification() {
        if (isThreadRunning()) {
            // Thread laeuft > M�glichkeit den Thead zu pausieren in Notification einfuegen
            TransparencyNotification.notify(this, TransparencyNotification.NOTIFICATION_PAUSE ,getString(R.string.notification_exercise_runnung), getPractiseInformation(), 1);
        } else {
            // Thread laeuft nicht > M�glichkeit den Thead zu fortzusetzen in Notification einfuegen
            TransparencyNotification.notify(this, TransparencyNotification.NOTIFICATION_CONTINUE ,getString(R.string.notification_exercise_paused), getPractiseInformation(), 1);
        }
    }
    private void stopTranspearencyNotification() {
        TransparencyNotification.cancel(this);
    }
    private String getPractiseInformation() {
        return "Aktueller Transparenz-Grad: " + (100 - TransparencyService.getAlpha()) + "%";
    }
    public static Intent getServiceIntent() {
        return serviceIntent;
    }
}