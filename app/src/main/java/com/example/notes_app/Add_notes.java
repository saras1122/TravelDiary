package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class Add_notes extends AppCompatActivity {
    EditText notes2,notes1;
    ImageButton savebutton;
    TextView pagetitle;
    String title,content,docId;
    boolean isEdit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        notes1=findViewById(R.id.notes1);
        notes2=findViewById(R.id.notes2);
        pagetitle=findViewById(R.id.pagetitle);
        savebutton=findViewById(R.id.savebutton);
        savebutton.setOnClickListener((v) -> Save());
//        title=getIntent().getStringExtra("title");
//        content=getIntent().getStringExtra("content");
//        docId=getIntent().getStringExtra("docId");
//        if(docId!=null && !docId.isEmpty()){
//            isEdit=true;
//        }
//        notes1.setText(title);
//        notes2.setText(content);
//        if(isEdit){
//
//            pagetitle.setText("Edit your note");
//        }
    }
    void Save() {
        String title=notes1.getText().toString();
        String content=notes2.getText().toString();
        if(title.isEmpty() || title==null){
            notes1.setError("Please Enter Title Name :)");
            return;
        }
        Note note=new Note();
        note.setTitle(title);
        note.setContent(content);
       // note.setTimestamp(Timestamp.now());
        saveNote(note);
    }

    private void saveNote(Note note) {
        DocumentReference documentReference=Utility.getCollectionReferenceForNotes().document();
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Add_notes.this, "Note added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(Add_notes.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}