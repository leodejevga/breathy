package com.apps.philipps.fade;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.apps.philipps.fade.activities.Game;

/**
 * Helper class for showing and canceling transparency notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class TransparencyNotification {

    private static final String TAG = "TranspNotification";

    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "Transparency";

    public static final int NOTIFICATION_PAUSE = 2001;
    public static final int NOTIFICATION_CONTINUE = 2002;
    public static final int PANDING_INTENT_RQ_OPEN_MAIN = 2003;
    public static final int PANDING_INTENT_RQ_PAUSE_CONTINUE = 2004;
    public static final int PANDING_INTENT_RQ_STOP = 2005;


    private static int pauseContinueDrawableID;
    private static String pauseContinueString;

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of transparency notifications. Make sure to follow the
     * <a href=
     * "https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static void notify(final Context context, final int notificationState, final String notificationTitle, final String notificationText, final int number) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
        // TODO: Remove this if your notification has no relevant thumbnail.
        // final Bitmap picture = BitmapFactory.decodeResource(res,
        // R.drawable.example_picture);

        // ###### Start der Game bei Ber�hren der Notification
        Intent intentOpenMainActivity = new Intent(context, Game.class);
        intentOpenMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntentOpenMainActivity = PendingIntent.getActivity(context, PANDING_INTENT_RQ_OPEN_MAIN, intentOpenMainActivity, 0);

        if (notificationState == NOTIFICATION_PAUSE) {
            // ###### Pausieren des Services
            pauseContinueDrawableID = R.drawable.ic_pause;
            pauseContinueString = res.getString(R.string.notification_pause);
        } else if (notificationState == NOTIFICATION_CONTINUE) {
            // ###### Service fortsetzen
            pauseContinueDrawableID = R.drawable.ic_play;
            pauseContinueString = res.getString(R.string.notification_play);
        } else {
            // TODO
            Log.e(TAG, "notificationState not Pause or Continue!");
        }

        Intent serviceIntent = TransparencyService.getServiceIntent();
        serviceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntentPauseContinueService = PendingIntent.getService(context, PANDING_INTENT_RQ_PAUSE_CONTINUE, serviceIntent.putExtra(TransparencyService.EXTRA_NEXT_STATE, TransparencyService.EXTRA_VALUE_PAUSE_CONTINUE), PendingIntent.FLAG_UPDATE_CURRENT);

        // ###### Service stoppen
        final PendingIntent pendingIntentStopService = PendingIntent.getService(context, PANDING_INTENT_RQ_STOP, serviceIntent.putExtra(TransparencyService.EXTRA_NEXT_STATE, TransparencyService.EXTRA_VALUE_STOP), PendingIntent.FLAG_UPDATE_CURRENT);

        // TODO Icon für Notification in der Statusleiste erstellen
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)

                // Set appropriate defaults for the notification light, sound,
                // and vibration.
                .setDefaults(0)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.ic_play).setContentTitle(notificationTitle).setContentText(notificationText)

                // All fields below this line are optional.

                // Use a default priority (recognized on devices running Android
                // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_MAX)

                // Provide a large icon, shown with the notification in the
                // notification drawer on devices running Android 3.0 or later.
                // .setLargeIcon(picture)

                // Set ticker text (preview) information for this notification.
                .setTicker(notificationText)

                // Show a number. This is useful when stacking notifications of
                // a single type.
                // .setNumber(number)

                // If this notification relates to a past or upcoming event, you
                // should set the relevant time information using the setWhen
                // method below. If this call is omitted, the notification's
                // timestamp will by set to the time at which it was shown.
                // TODO: Call setWhen if this notification relates to a past or
                // upcoming event. The sole argument to this method should be
                // the notification timestamp in milliseconds.
                // .setWhen(...)

                // Set the pending intent to be initiated when the user touches
                // the notification.
                .setContentIntent(pendingIntentOpenMainActivity)

                // Example additional actions for this notification. These will
                // only show on devices running Android 4.1 or later, so you
                // should ensure that the activity in this notification's
                // content intent provides access to the same actions in
                // another way.
                .addAction(pauseContinueDrawableID, pauseContinueString, pendingIntentPauseContinueService)
                .addAction(R.drawable.ic_stop, res.getString(R.string.notification_stop), pendingIntentStopService)

                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(false);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}