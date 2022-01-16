package io.github.marcocipriani01.simplesqm;

import android.app.PendingIntent;
import android.os.Build;

final class Constants {

    public static final int SERVICE_PENDING_INTENT_FLAG;
    // Values have to be globally unique
    static final String INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB";
    static final String INTENT_ACTION_DISCONNECT = BuildConfig.APPLICATION_ID + ".Disconnect";
    static final String NOTIFICATION_CHANNEL = BuildConfig.APPLICATION_ID + ".Channel";
    static final String INTENT_CLASS_MAIN_ACTIVITY = BuildConfig.APPLICATION_ID + ".MainActivity";
    // Values have to be unique within each app
    static final int NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            SERVICE_PENDING_INTENT_FLAG = PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
        else SERVICE_PENDING_INTENT_FLAG = PendingIntent.FLAG_UPDATE_CURRENT;
    }
}