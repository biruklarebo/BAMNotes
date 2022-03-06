package com.example.bamnotes;

import java.util.Calendar;
import java.util.Date;

public class Note {
    private int noteID;
    private String noteSubject;
    private String note;
    private String priority;
    private Date date;

    public Note() {
        noteID = -1;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int i) {
        noteID = i;
    }

    public String getNoteSubject() {
        return noteSubject;
    }

    public void setNoteSubject(String s) {
        noteSubject = s;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String s) {
        note = s;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String s) {
        priority = s;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date1) {
        date = date1;
    }
}
