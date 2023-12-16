package com.example.notes_app;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.notes_app.databinding.ActivityMapsBinding;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GeoCodeLocation.LocationCallback   {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    String s="";
    double latitude,longitude;
    List<String> al=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GeoCodeLocation locationAddress = new GeoCodeLocation();
        Query query=Utility.getCollectionReferenceForNotes();
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String title = document.getString("location");
                    if (title != null) {
                        al.add(title);
                        locationAddress.getAddressFromLocation(title, getApplicationContext(), this);
                    } else {
                        Log.e("Firestore", "Title is null for document with ID: " + document.getId());
                    }
                }
            } else {
                Log.e("Firestore", "Error getting documents: ", task.getException());
            }
        });
    }
    @Override
    public void onLocationResult(String result) {
        if (result != null) {
            String[] coordinates = result.trim().split("\\s+");
            String lt=coordinates[0];
            latitude = Double.parseDouble(coordinates[1]);
            longitude = Double.parseDouble(coordinates[2]);
            LatLng mumbai = new LatLng(latitude, longitude);
            runOnUiThread(() -> {
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