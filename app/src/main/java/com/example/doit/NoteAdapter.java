package com.example.doit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;


public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.noteViewHolder> {
    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull noteViewHolder holder, final int position, @NonNull Note model) {
        holder.noteTitleTextView.setText(model.title);
        holder.noteContentTextView.setText(model.content);
        holder.noteTimeTextView.setText(timeStamptoString(model.timestamp));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteDetailsActivity.class);
                intent.putExtra("title", model.title);
                intent.putExtra("content", model.content);
                String docID = getSnapshots().getSnapshot(position).getId();
                intent.putExtra("docId", docID);

                context.startActivity(intent);


            }
        });
    }

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items, parent, false);
        return new noteViewHolder(view);
    }

    class noteViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitleTextView, noteContentTextView,noteTimeTextView;
        public noteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitleTextView = itemView.findViewById(R.id.noteTitleTextView);
            noteContentTextView = itemView.findViewById(R.id.noteContentTextView);
            noteTimeTextView = itemView.findViewById(R.id.noteTimeTextView);
        }
    }
    static String timeStamptoString(Timestamp timestamp){
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}
