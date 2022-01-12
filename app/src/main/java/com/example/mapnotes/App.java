package com.example.mapnotes;

import android.app.Application;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.example.mapnotes.Service.NotificationService;

public class App extends Application {

    @Override
    public void onCreate(){
        super.onCreate();

        Intent serviceIntent = new Intent(this, NotificationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);

    }
}
