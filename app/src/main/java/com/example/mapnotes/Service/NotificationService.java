package com.example.mapnotes.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mapnotes.MapsActivity;
import com.example.mapnotes.R;

public class NotificationService extends Service {
    private static boolean threadCanRun = true;
    public static final String CHANNEL_ID = "NotificationServiceForegroundServiceChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        threadCanRun = true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manger = getSystemService(NotificationManager.class);
            manger.createNotificationChannel(channel);
        }

        progressThread.start();
        return START_NOT_STICKY;
    }

    private Thread progressThread = new Thread(new Runnable()
    {
        public void run() {
            while (threadCanRun) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("My notification")
                        .setContentText("Much longer text that cannot fit one line...");
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getBaseContext());
                managerCompat.notify(1,builder.build());

                

                try {
                    progressThread.sleep(60000);
                } catch (InterruptedException ex) {}
            }
        }
    });


    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        threadCanRun = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}