package com.example.doit;

//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import org.checkerframework.checker.nullness.qual.NonNull;

public class NoteDetailsActivity extends AppCompatActivity {
    EditText notesTitleText, contentText;
    ImageView saveButton;
    TextView pageTitle, deleteButton;
    String title, docID, content, docId;
    boolean isEditmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        notesTitleText = findViewById(R.id.notesTitleText);
        contentText = findViewById(R.id.contentText);
        saveButton = findViewById(R.id.saveButton);
        pageTitle = findViewById(R.id.pageTitle);
        deleteButton = findViewById(R.id.deleteButton);


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNoteFromFirebase();
            }
        });

        //Recieve Data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()){
            isEditmode = true;
        }
        notesTitleText.setText(title);
        contentText.setText(content);

        if (isEditmode){
            pageTitle.setText("Edit Notes");
            deleteButton.setVisibility(View.VISIBLE);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    private void saveNote() {
        String noteTitle = notesTitleText.getText().toString();
        String noteContent = contentText.getText().toString();

        if (noteTitle == null || noteTitle.isEmpty()){
            notesTitleText.setError("Title is required");
            return;
        }

            Note note = new Note();
            note.setTitle(noteTitle);
            note.setContent(noteContent);
            note.setTimestamp(Timestamp.now());
            saveNotetoFirebase(note);
    }

            void saveNotetoFirebase(Note note){
                DocumentReference documentReference;
                if (isEditmode){
                documentReference = Utility.getCollectionReferenceForNotes().document(docId);
                }
                else{
                documentReference = Utility.getCollectionReferenceForNotes().document();

                }

                documentReference.set(note)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(NoteDetailsActivity.this, "Note Added", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(NoteDetailsActivity.this, "Failed while adding notes", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            private void deleteNoteFromFirebase() {
                DocumentReference documentReference;
                documentReference = Utility.getCollectionReferenceForNotes().document(docId);

                documentReference.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(NoteDetailsActivity.this, "Note is deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(NoteDetailsActivity.this, "Failed to deleting notes", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

    }
}