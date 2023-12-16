package com.example.notes_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MemoryFragment extends Fragment {
    RecyclerView recyclerView;
    ImageButton menu;
    SearchView editsearch;
    Adapter noteAdapter;
    TextView textView,text;
    ImageSlider imageSlider;
    LinearLayout linearLayout;
    public MemoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_memory, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);
        textView=view.findViewById(R.id.text1);
        text=view.findViewById(R.id.text);
        menu = view.findViewById(R.id.menu1);
        linearLayout=view.findViewById(R.id.im);
        menu.setOnClickListener((v)->show());
        imageSlider = view.findViewById(R.id.image_slider);
        setupRecyclerView();
        return view;
    }
    void setupRecyclerView() {
        String s=datePick();
        Query query = Utility.getCollectionReferenceForNotes()
                .whereEqualTo("date", s);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        if(!options.getSnapshots().isEmpty()){
            textView.setVisibility(View.VISIBLE);
        }else{
            text.setVisibility(View.VISIBLE);
            ArrayList<String> al=new ArrayList<>();
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ArrayList<String> imageUrl = (ArrayList<String>) document.get("images");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        // Process or store the imagesFieldValue as needed
                        for(String s1: imageUrl)
                            al.add(s1);
                    } else {
                        Log.d(TAG, document.getId() + " => No images field or value is null");
                    }
                }
                Log.d("images",al+"");
                if(al!=null && !al.isEmpty()){
                    Log.d("images11",al+"");
                    linearLayout.setVisibility(View.VISIBLE);
                    List<SlideModel> imageList1 = new ArrayList<>();
                    for(String i:al){
                        imageList1.add(new SlideModel(i
                                , ScaleTypes.CENTER_CROP));
                    }
                    imageSlider.setImageList(imageList1);
                    imageSlider.startSliding(1000000);
                    imageSlider.stopSliding();
                }
            }).addOnFailureListener(e -> {
                Log.w(TAG, "Error getting documents.", e);
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        noteAdapter = new Adapter(options, requireContext());
        recyclerView.setAdapter(noteAdapter);
    }
    void show() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), menu);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("Logout")) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(requireContext(), Login1.class));
                    requireActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }
    String datePick(){
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        // Add one year to the current date
        LocalDate futureDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            futureDate = currentDate.plusYears(1);
        }

        // Specify the desired date format
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        }
        String currentDateString="";
        // Format the dates to strings
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateString = currentDate.format(formatter);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String futureDateString = futureDate.format(formatter);
        }
        return currentDateString;
    }

    @Override
    public void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }
    @Override
    public void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}