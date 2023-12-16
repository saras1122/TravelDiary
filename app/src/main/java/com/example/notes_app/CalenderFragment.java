package com.example.notes_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class CalenderFragment extends Fragment {
    ListView l;
    public CalenderFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_calender, container, false);
        Query query = Utility.getCollectionReferenceForNotes();
        ArrayList<String> al=new ArrayList<>();
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    int i=task.getResult().size();
                    String title = document.getString("location");
                    String date = document.getString("date");
                    if (title != null) {
                        ArrayList<String> innerList = new ArrayList<>();
                        innerList.add(date);
                        innerList.add(title);
                        String s="     ";
                        s+=date;
                        s+="                             ";
                        s+="📍";
                        s+=title;
                        al.add(s);
                        if(i==al.size()){
                            l=view.findViewById(R.id.list1);
                            Log.d("Firestore", " " + al);
                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, al);
                            l.setAdapter(adapter);
                        }
                        Log.d("inner", " " + al);
                    } else {
                        Log.e("Firestore", "Title is null for document with ID: " + document.getId());
                    }
                }
            } else {
                Log.e("Firestore", "Error getting documents: ", task.getException());
            }
        });
        return view;
    }
}