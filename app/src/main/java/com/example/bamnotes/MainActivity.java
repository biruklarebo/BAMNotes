package com.example.bamnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Note currentNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListButton();
        initNewNoteButton();
        initSettingsButton();
        initToggleButton();
        initTextChangedEvents();
        iniPriorityClick();
        initSaveButton();
        setForEditing(false);

        Bundle extras= getIntent().getExtras();
        if (extras != null) {
            initNote(extras.getInt("noteid"));
        }
        else {
            currentNote = new Note();
            Date date = new Date();
            currentNote.setDate(date);
        }
    }
    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initNewNoteButton() {
        ImageButton ibNew = findViewById(R.id.imageButtonNewNote);
        ibNew.setEnabled(false);
        ibNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initToggleButton() {
        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }
    public void initSaveButton(){
        Button saveButton = findViewById(R.id.buttonSave);
        EditText textSubj = findViewById(R.id.editSubject);
        RadioButton rbHigh = findViewById(R.id.radioHigh);
        RadioButton rbMed = findViewById(R.id.radioMed);
        RadioButton rbLow = findViewById(R.id.radioLow);
        saveButton.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                NotesDataSource ds = new NotesDataSource(MainActivity.this);
                if ((textSubj.getText().toString().isEmpty()) || (!rbHigh.isChecked() && !rbMed.isChecked() && !rbLow.isChecked())) {
                    Context context = MainActivity.this;
                    new AlertDialog.Builder(context).setTitle("Error").setMessage("Please enter a priority and subject before saving").show();
                    setForEditing(true);
                } else {
                    try {
                        ds.open();
                        if (currentNote.getNoteID() == -1) {
                            wasSuccessful = ds.insertNote(currentNote);
                            if (wasSuccessful) {
                                int newId = ds.getLastNoteID();
                                currentNote.setNoteID(newId);
                            }
                        } else {
                            wasSuccessful = ds.updateNote(currentNote);
                        }
                        ds.close();
                    } catch (Exception e) {
                        wasSuccessful = false;
                    }
                    if (wasSuccessful) {
                        ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                        editToggle.toggle();
                        setForEditing(false);
                    }
                }
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editSubject = findViewById(R.id.editSubject);
        EditText editNote = findViewById(R.id.editNote);
        Button buttonSave = findViewById(R.id.buttonSave);
        RadioButton rbHigh = findViewById(R.id.radioHigh);
        RadioButton rbMed = findViewById(R.id.radioMed);
        RadioButton rbLow = findViewById(R.id.radioLow);


        editSubject.setEnabled(enabled);
        editNote.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        rbHigh.setEnabled(enabled);
        rbMed.setEnabled(enabled);
        rbLow.setEnabled(enabled);

        if (enabled) {
            editSubject.requestFocus();

        } else {
            ScrollView s = findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    private void initTextChangedEvents() {
        final EditText etSubject = (EditText) findViewById(R.id.editSubject);
        etSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentNote.setNoteSubject(etSubject.getText().toString());
            }
        });
        final EditText etNote = (EditText) findViewById(R.id.editNote);
        etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentNote.setNote(etNote.getText().toString());
            }
        });
    }

    private void iniPriorityClick() {
        RadioGroup groupPriority = (RadioGroup) findViewById(R.id.rbGroupPriority);
        RadioButton rbHigh = findViewById(R.id.radioHigh);
        RadioButton rbMed = findViewById(R.id.radioMed);
        RadioButton rbLow = findViewById(R.id.radioLow);

        groupPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (rbHigh.isChecked())
                    currentNote.setPriority("3");
                else if (rbMed.isChecked())
                    currentNote.setPriority("2");
                else if (rbLow.isChecked())
                    currentNote.setPriority("1");
            }
        });
    }
    private void initNote(int id) {
        NotesDataSource ds = new NotesDataSource(MainActivity.this);
        try {
            ds.open();
            currentNote = ds.getSpecificNote(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Contact Failed", Toast.LENGTH_LONG).show();
        }

        EditText editSubject = findViewById(R.id.editSubject);
        EditText editNote = findViewById(R.id.editNote);
        RadioButton rbHigh = findViewById(R.id.radioHigh);
        RadioButton rbMed = findViewById(R.id.radioMed);
        RadioButton rbLow = findViewById(R.id.radioLow);

        editSubject.setText(currentNote.getNoteSubject());
        editNote.setText(currentNote.getNote());

        switch (currentNote.getPriority()){
            case "3":
                rbHigh.setChecked(true);
                break;
            case "2":
                rbMed.setChecked(true);
                break;
            default:
                rbLow.setChecked(true);
                break;
        }

        // if (currentNote.getPriority() == "High")
        //    rbHigh.setChecked(true);
        //else if (currentNote.getPriority() == "Med")
        //    rbMed.setChecked(true);
        //else if (currentNote.getPriority() == "Low")
        //    rbLow.setChecked(true);
    }
}