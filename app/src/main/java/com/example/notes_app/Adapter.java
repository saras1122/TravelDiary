package com.example.notes_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class Adapter extends FirestoreRecyclerAdapter<Note,Adapter.NoteView> {
    int index=-1;
    Context context;
    public Adapter(@NonNull FirestoreRecyclerOptions<Note> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteView holder, int position, @NonNull Note note) {
        holder.title.setText(note.title);
        holder.content.setText(note.content);
        if(note.getFlag()){
            holder.bookmark.setImageResource(R.drawable.bookmark);
        }else{
            holder.bookmark.setImageResource(R.drawable.bookmarkwhite);
        }
//        if(position%2 == 0){
//            holder.linearLayout.setBackgroundColor(R.drawable.rounded);
//        }
//        else
//        {
//            holder.itemView.setBackgroundColor(Color.parseColor("#013220"));
//        }
        holder.share.setOnClickListener((v)->{
            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Title::   " + note.title+ "\nContent::   " + note.content);
            intent.setType("text/plains");
            context.startActivity(Intent.createChooser(intent, "Send to"));
        });
        holder.itemView.setOnClickListener((v)->
        {
            Intent intent=new Intent(context,MainActivity2.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            intent.putExtra("styles",note.arr);
            intent.putExtra("start",note.start);
            intent.putExtra("end",note.end);
            intent.putExtra("color",note.color);
            intent.putExtra("flag",note.flag);
            intent.putExtra("image",note.imageUrl);
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
        ImageButton share,bookmark;
        LinearLayout linearLayout;
        public NoteView(@NonNull View itemView){
            super(itemView);
            title=itemView.findViewById(R.id.titletext);
            content=itemView.findViewById(R.id.contenttext);
            share=itemView.findViewById(R.id.share);
            bookmark=itemView.findViewById(R.id.bookmark);
            linearLayout=itemView.findViewById(R.id.recycle);
        }
    }
    public  void filter(List<Note> sl){

    }
}
