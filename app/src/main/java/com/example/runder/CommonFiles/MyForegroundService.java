package com.example.runder.CommonFiles;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.runder.R;

import java.util.List;

public class MyForegroundService extends Service {
    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }
    private Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
       @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Runder:MyWakeLockTag");
       wl.setReferenceCounted(false);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.logowithname)
                .setContentTitle("My App is running")
                .setContentText("Tap to open")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(1, builder.build());
        wl.acquire(600000 );


    }

}
