package com.example.mapnotes.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import com.example.mapnotes.MapsActivity;
import com.example.mapnotes.ViewModel.EditNoteViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataService extends Service {

    public ArrayList<EditNoteViewModel> list;
    private final IBinder binder = new LocalBinder();
    private SharedPreferences Prefs;


    public DataService() {
        list = new ArrayList<>();
    }

    public ArrayList<EditNoteViewModel> getList(){
        return list;
    }

    public void AddItem(EditNoteViewModel editNoteViewModel){
        list.add(editNoteViewModel);
    }

    public void deleteItem(LatLng latlng){
        EditNoteViewModel res = getItem(latlng);
        if(res != null){
            list.remove(res);
        }
        savedata();
    }

    public EditNoteViewModel getItem(LatLng latlng){
        EditNoteViewModel res = null;
        for (EditNoteViewModel item: list) {
            if(item.getLatLng().latitude == latlng.latitude && item.getLatLng().longitude == latlng.longitude){
                res = item;
            }
        }
        return res;
    }

    private void savedata(){
        String json = new Gson().toJson(list);
        Prefs.edit().putString(MapsActivity.PrefsEditNoteListKey,json).apply();
    }
    @Override
    public boolean onUnbind(Intent intent) {
       savedata();
        return true;
    }
    @Override
    public IBinder onBind(Intent intent) {
        Prefs = this.getSharedPreferences(MapsActivity.SharedPrefs, Context.MODE_PRIVATE);
        String json = Prefs.getString(MapsActivity.PrefsEditNoteListKey,"");
        list = new Gson().fromJson(json, new TypeToken<ArrayList<EditNoteViewModel>>(){}.getType());
        if(list == null) list = new ArrayList<EditNoteViewModel>();
        return binder;
    }

    public class LocalBinder extends Binder {
        public DataService getService() {
            return DataService.this;
        }
    }

}