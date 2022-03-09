package com.example.bamnotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter {
    private ArrayList<Note> noteData;
    private View.OnClickListener getmOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;
    private View.OnClickListener mOnItemClickListener;

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewSubject;
        public TextView textViewPriority;
        public TextView textViewDate;

        public Button deleteButton;

        public NotesViewHolder(@NonNull View itemView){
            super(itemView);
            textViewSubject = itemView.findViewById(R.id.textNoteSubject);
            textViewPriority = itemView.findViewById(R.id.textNotePriority);
            textViewDate = itemView.findViewById(R.id.textDate);
            deleteButton = itemView.findViewById(R.id.buttonDeleteNote);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
        public TextView getNoteTextView() {
            return textViewSubject;
        }

        public TextView getTextViewPriority(){
            return textViewPriority;
        }

        public TextView getTextViewDate(){
            return textViewDate;
        }

        public Button getDeleteButton(){
            return deleteButton;
        }
    }
    public NotesAdapter(ArrayList<Note> arrayList, Context context) {
        noteData = arrayList;
        parentContext = context;
    }
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note, parent, false);
        return new NotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        NotesViewHolder cvh = (NotesViewHolder) holder;
        cvh.getNoteTextView().setText(noteData.get(position).getNoteSubject());
        switch((noteData.get(position).getPriority())){
            case "3":
                cvh.getTextViewPriority().setText("High");
                holder.itemView.setBackgroundResource(R.color.red);
                break;
            case "2":
                cvh.getTextViewPriority().setText("Medium");
                holder.itemView.setBackgroundResource(R.color.yellow);
                break;
            default:
                cvh.getTextViewPriority().setText("Low");
                holder.itemView.setBackgroundResource(R.color.green);
                break;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(noteData.get(position).getDate());
        cvh.getTextViewDate().setText(strDate);

        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return noteData.size();
    }
    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Note note = noteData.get(position);
        NotesDataSource ds = new NotesDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteNote(note.getNoteID());
            ds.close();
            if (didDelete) {
                noteData.remove(position);
                notifyDataSetChanged();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(parentContext, "Delete Failed", Toast.LENGTH_LONG).show();
        }

    }


}