package com.example.notes_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes_app.databinding.ActivityMapsBinding;
import com.example.notes_app.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback,GeoCodeLocation.LocationCallback{
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    String s="";
    double latitude,longitude;
    List<String> al=new ArrayList<>();
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ActivityMapsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        GeoCodeLocation locationAddress = new GeoCodeLocation();
        Query query = Utility.getCollectionReferenceForNotes();
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("location");
                    if (title != null) {
                        al.add(title);
                        locationAddress.getAddressFromLocation(title, requireContext(), this);
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
    @Override
    public void onLocationResult(String result) {
        if (result != null) {
            String[] coordinates = result.trim().split("\\s+");
            String lt=coordinates[0];
            latitude = Double.parseDouble(coordinates[1]);
            longitude = Double.parseDouble(coordinates[2]);
            LatLng mumbai = new LatLng(latitude, longitude);
            requireActivity().runOnUiThread(() -> {
                mMap.addMarker(new MarkerOptions().position(mumbai).title(lt));
            });
        } else {
            Log.e("MapActivity", "Unable to get location");
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
        }
    }
}