package com.example.mapnotes.ViewModel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

public class EditNoteViewModel extends ViewModel {
    private String Title;
    private String Beschreibung;
    private com.google.android.gms.maps.model.LatLng LatLng;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBeschreibung() {
        return Beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        Beschreibung = beschreibung;
    }

    public com.google.android.gms.maps.model.LatLng getLatLng() {
        return LatLng;
    }

    public void setLatLng(com.google.android.gms.maps.model.LatLng latLng) {
        LatLng = latLng;
    }
}
