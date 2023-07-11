package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class MainActivity2 extends AppCompatActivity {
    EditText notes2,notes1;
    ImageButton savebutton;
    TextView pagetitle;
    TextView delete;
    String title,content,docId;
    boolean isEdit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        notes1=findViewById(R.id.notes1);
        notes2=findViewById(R.id.notes2);
        pagetitle=findViewById(R.id.pagetitle);
        savebutton=findViewById(R.id.savebutton);
        delete=findViewById(R.id.delete);
                title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");
        if(docId!=null && !docId.isEmpty()){
            isEdit=true;
        }
        notes1.setText(title);
        notes2.setText(content);
        if(isEdit){

            pagetitle.setText("Edit your note");
            delete.setVisibility(View.VISIBLE);
        }
        savebutton.setOnClickListener((v) -> Save());
        delete.setOnClickListener((v)-> deleteNotefromFirebase());
    }



    private void Save() {
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
        DocumentReference documentReference;
        if(isEdit){
            documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        }
        else{
            documentReference=Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity2.this, "Note added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void deleteNotefromFirebase() {
        DocumentReference documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity2.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}