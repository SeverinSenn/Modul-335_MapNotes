package com.example.mapnotes.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mapnotes.MapsActivity;
import com.example.mapnotes.R;
import com.example.mapnotes.Util.EditTextTextWatcher;
import com.example.mapnotes.ViewModel.EditNoteViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class NotificationService extends Service {
    public static final String CHANNEL_ID = "NotificationServiceForegroundServiceChannel";
    private android.location.LocationManager LocationManager;
    private DataService DataService;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manger = getSystemService(NotificationManager.class);
            manger.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Start ForgroundService");
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getBaseContext());
        this.startForeground(1,builder.build());



        Intent intentDataService = new Intent(this, DataService.class);
        bindService(intentDataService, connection, Context.BIND_AUTO_CREATE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_STICKY;
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 420000, 50, locationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 420000, 50, locationListener);



        return START_NOT_STICKY;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            ArrayList<EditNoteViewModel> locations = DataService.getList();
            for (EditNoteViewModel item : locations) {
                LatLng latLngitem = item.getLatLng();
                double dis = distance(latLngitem.latitude,location.getLatitude(),latLngitem.longitude,location.getLongitude(),0,0);
                if(dis <= 20){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(item.getTitle())
                            .setContentText(item.getBeschreibung());
                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getBaseContext());

                    managerCompat.notify(locations.indexOf(item),builder.build());
                }
            }



        }
    };

    //distance in Meters
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DataService.LocalBinder binder = (DataService.LocalBinder) service;
            DataService = binder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {}
    };

    @Override
    public void onDestroy() { super.onDestroy();
        unbindService(connection);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}