package com.example.bamnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class NotesListActivity extends AppCompatActivity{
    ArrayList<Note> notes;
    NotesAdapter notesAdapter;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder)
                    view.getTag();
            int position = viewHolder.getAdapterPosition();
            int noteId = notes.get(position).getNoteID();
            Intent intent = new Intent(NotesListActivity.this, MainActivity.class);
            intent.putExtra("noteid", noteId);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        initListButton();
        initNewNoteButton();
        initSettingsButton();
        initDeleteSwitch();
    }
    @Override
    public void onResume() {
        super.onResume();
        String sortBy = getSharedPreferences("BAMNotesPreferences", Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("BAMNotesPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        NotesDataSource ds = new NotesDataSource(this);

        try {
            ds.open();
            notes = ds.getNotes(sortBy, sortOrder);
            ds.close();
            if (notes.size() > 0) {
                RecyclerView notesList = findViewById(R.id.rvNotes);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                notesList.setLayoutManager(layoutManager);
                notesAdapter = new NotesAdapter(notes, this);
                notesAdapter.setOnItemClickListener(onItemClickListener);
                notesList.setAdapter(notesAdapter);
            }
            else {
                Intent intent  = new Intent(NotesListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setEnabled(false);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesListActivity.this, NotesListActivity.class);
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
                Intent intent = new Intent(NotesListActivity.this, MainActivity.class);
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
                Intent intent = new Intent(NotesListActivity.this, NotesSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Boolean status = compoundButton.isChecked();
                notesAdapter.setDelete(status);
                notesAdapter.notifyDataSetChanged();
            }
        });
    }

}
