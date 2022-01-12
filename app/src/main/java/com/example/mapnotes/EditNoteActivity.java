package com.example.mapnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mapnotes.Service.DataService;
import com.example.mapnotes.Util.EditTextTextWatcher;
import com.example.mapnotes.ViewModel.EditNoteViewModel;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class EditNoteActivity<connection> extends AppCompatActivity {

    private DataService DataService;
    private EditNoteViewModel EditNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        EditNoteViewModel = new ViewModelProvider(this).get(EditNoteViewModel.class);

        Intent intent = getIntent();
        EditNoteViewModel.setLatLng((LatLng)intent.getExtras().get(MapsActivity.LatLongKey));
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intentDataService = new Intent(this, DataService.class);
        bindService(intentDataService, connection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    public void Save(View view){
        DataService.AddItem(EditNoteViewModel);
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void Delete(View view){
        DataService.deleteItem(EditNoteViewModel.getLatLng());
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DataService.LocalBinder binder = (DataService.LocalBinder) service;
            DataService = binder.getService();
            LatLng latLng = EditNoteViewModel.getLatLng();
            EditNoteViewModel = DataService.getItem(latLng);

            if(EditNoteViewModel == null){
                EditNoteViewModel = new EditNoteViewModel();
                EditNoteViewModel.setLatLng(latLng);
                Button deletebutton = (Button) findViewById(R.id.delete);
                deletebutton.setVisibility(View.GONE);
            }

            EditText beschreibungEditText = (EditText) findViewById(R.id.beschreibung);
            beschreibungEditText.addTextChangedListener(new EditTextTextWatcher(EditNoteViewModel,"Beschreibung"));
            beschreibungEditText.setText(EditNoteViewModel.getBeschreibung());

            EditText titleEditText = (EditText) findViewById(R.id.title);
            titleEditText.addTextChangedListener(new EditTextTextWatcher(EditNoteViewModel,"title"));




        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {}
    };

}