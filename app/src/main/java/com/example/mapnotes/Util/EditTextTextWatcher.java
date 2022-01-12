package com.example.mapnotes.Util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.mapnotes.ViewModel.EditNoteViewModel;

public class EditTextTextWatcher implements TextWatcher {

    private EditNoteViewModel EditNoteViewModel;
    private String Key;

    public EditTextTextWatcher(EditNoteViewModel e,String key) {
        EditNoteViewModel = e;
        Key = key;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        switch (Key){
            case "Beschreibung":
                EditNoteViewModel.setBeschreibung(s.toString());
                break;
            case "title":
                EditNoteViewModel.setTitle(s.toString());
                break;
        }
    }

    public void afterTextChanged(Editable s) {
    }

}
