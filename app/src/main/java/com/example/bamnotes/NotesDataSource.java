package com.example.bamnotes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotesDataSource {
    private SQLiteDatabase database;
    private NotesDBHelper dbHelper;

    public NotesDataSource (Context context) {
        dbHelper = new NotesDBHelper(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close(){
        dbHelper.close();
    }

    public boolean insertNote(Note n) {
        boolean didSucced = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("subject", n.getNoteSubject());
            initialValues.put("note", n.getNote());
            initialValues.put("priority", n.getPriority());
           //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            //String strDate= formatter.format(n.getDate());
            initialValues.put("date", String.valueOf(n.getDate().getTime()));

            didSucced = database.insert("notes", null, initialValues) > 0;
        }
        catch (Exception e) {
            //do nothing -will return false if there is an exception
        }
        return didSucced;
    }

    public boolean updateNote(Note n) {
        boolean didSucced = false;
        try {
            long rowId = (long) n.getNoteID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("subject", n.getNoteSubject());
            updateValues.put("note", n.getNote());
            updateValues.put("priority", n.getPriority());
            updateValues.put("date", String.valueOf(n.getDate().getTime()));

            didSucced = database.update("notes",  updateValues, "_id=" + rowId , null) > 0;
        }
        catch (Exception e) {
            //do nothing -will return false if there is an exception
        }
        return didSucced;
    }

    public int getLastNoteID() {
        int lastID;
        try{
            String query = "Select MAX(_id) from notes";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            lastID = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastID = -1;
        }
        return lastID;

    }
    public ArrayList<Note> getNotes(String sortField, String sortOrder) {
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            String query = "SELECT * FROM notes ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Note newNote;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newNote = new Note();
                newNote.setNoteID(cursor.getInt(0));
                newNote.setNoteSubject(cursor.getString(1));
                newNote.setNote(cursor.getString(2));
                newNote.setPriority(cursor.getString(3));
                Date date = new Date();
                date.setTime(Long.parseLong(cursor.getString(4)));
                newNote.setDate(date);
                notes.add(newNote);

                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            notes = new ArrayList<Note>();
        }
        return notes;
    }

    public Note getSpecificNote (int noteId) {
        Note note = new Note();
        String query = "SELECT * FROM notes WHERE _id =" + noteId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            note.setNoteID(cursor.getInt(0));
            note.setNoteSubject(cursor.getString(1));
            note.setNote(cursor.getString(2));
            note.setPriority(cursor.getString(3));
            Date date = new Date();
            date.setTime(Long.valueOf(cursor.getString(4)));
            note.setDate(date);

            cursor.close();
        }
        return note;
    }
    public boolean deleteNote (int noteId) {
        boolean didDelete = false;
        try{
            didDelete = database.delete("notes", "_id=" + noteId,null) >0;
        }
        catch (Exception e) {
            //Do nothing -return value is already set to false
        }
        return didDelete;
    }
}