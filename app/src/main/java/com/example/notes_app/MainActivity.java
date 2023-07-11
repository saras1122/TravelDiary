package com.example.notes_app;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton plus;
    RecyclerView recyclerView;
    ImageButton menu;
    Adapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        plus=findViewById(R.id.notebuton);
        recyclerView=findViewById(R.id.recyclerview);
        menu=findViewById(R.id.menu1);
        plus.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this,Add_notes.class)));
        menu.setOnClickListener((v)->show());
        setupRecyclerView();
    }
    void show(){
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,menu);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,Login1.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

     void setupRecyclerView() {
        Query query=Utility.getCollectionReferenceForNotes();
         FirestoreRecyclerOptions<Note> options=new FirestoreRecyclerOptions.Builder<Note>()
                 .setQuery(query,Note.class).build();
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter =new Adapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }
    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}