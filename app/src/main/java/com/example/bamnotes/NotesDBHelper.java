package com.example.bamnotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class NotesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bamnotes.db";
    private static final int DATABASE_VERSION = 1;
    // database creation

    private static final String CREATE_TABLE_NOTES = "create table notes (_id integer primary key autoincrement, "
            + "subject text not null, note text, " +  "priority text, date text);";

    public NotesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(NotesDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}
