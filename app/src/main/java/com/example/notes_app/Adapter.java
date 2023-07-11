package com.example.notes_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Adapter extends FirestoreRecyclerAdapter<Note,Adapter.NoteView> {

    Context context;
    public Adapter(@NonNull FirestoreRecyclerOptions<Note> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteView holder, int position, @NonNull Note note) {
        holder.title.setText(note.title);
        holder.content.setText(note.content);
        holder.itemView.setOnClickListener((v)->
        {
            Intent intent=new Intent(context,MainActivity2.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            String docId=this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler,parent,false);
        return new NoteView(view);
    }

    class NoteView extends RecyclerView.ViewHolder {
        TextView title,content;
        public NoteView(@NonNull View itemView){
            super(itemView);
            title=itemView.findViewById(R.id.titletext);
            content=itemView.findViewById(R.id.contenttext);

        }
    }
}
