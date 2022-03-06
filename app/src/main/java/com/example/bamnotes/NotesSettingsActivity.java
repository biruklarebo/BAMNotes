package com.example.bamnotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class NotesSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_note_settings);
        initListButton();
        initSettingsButton();
        initNewNoteButton();
        initSortByClick();
        initSortOrderClick();
        initSettings();

    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesSettingsActivity.this, NotesListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initNewNoteButton() {
        ImageButton ibNew = findViewById(R.id.imageButtonNewNote);
        ibNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesSettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setEnabled(false);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesSettingsActivity.this, NotesSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initSettings() {
        String sortBy = getSharedPreferences("BAMNotesPreferences", Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("BAMNotesPreferences", Context.MODE_PRIVATE).getString("sortorder","ASC");
        RadioButton rbSubject = findViewById(R.id.radioSubject);
        RadioButton rbPriority = findViewById(R.id.radioPriority);
        RadioButton rbDate = findViewById(R.id.radioDate);
        if (sortBy.equalsIgnoreCase("subject")) {
            rbSubject.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("priority")) {
            rbPriority.setChecked(true);
        }
        else {
            rbDate.setChecked(true);
        }
        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);
        if (sortOrder.equalsIgnoreCase("ASC")){
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);
        }
    }
    private void initSortByClick(){
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbSubject = findViewById(R.id.radioSubject);
                RadioButton rbPriority = findViewById(R.id.radioPriority);
                if (rbSubject.isChecked()) {
                    getSharedPreferences("BAMNotesPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "subject").apply();
                }
                else if (rbPriority.isChecked()){
                    getSharedPreferences("BAMNotesPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "priority").apply();
                }
                else {
                    getSharedPreferences("BAMNotesPreferences", Context.MODE_PRIVATE).edit().putString("sortfield", "date").apply();
                }
            }
        });
    }
    private void initSortOrderClick(){
        RadioGroup rgSortOrder =findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAscending= findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("BAMNotesPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").apply();
                }
                else {
                    getSharedPreferences("BAMNotesPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "DESC").apply();
                }
            }
        });
    }
}
