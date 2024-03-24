package com.example.projectapp;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class GeopointDialog extends AppCompatActivity {
    private String eventId; // The variable you want to pass
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FirebaseFirestore db;
    private DocumentReference eventRef;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geopoint_dialog);

        eventId = (String) getIntent().getSerializableExtra("eventId");
        db = FirebaseFirestore.getInstance();
        eventRef = db.collection("events").document(eventId);

        Button okButton = findViewById(R.id.okButton);
        Button cancelButton = findViewById(R.id.noButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check location permission and proceed to set location if permission is granted
                checkLocationPermission();
                onBackPressed();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cancel button if needed
                onBackPressed(); // Close the dialog
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Thank you", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(GeopointDialog.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(GeopointDialog.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, set location
            getCurrentLocation();
        }
    }

    public void updateGeopoints(GeoPoint geoPoint){
        eventRef.update("geopoints", FieldValue.arrayUnion(geoPoint))
                .addOnSuccessListener(aVoid -> {
                    // Successfully added the GeoPoint
                    Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure (e.g., network error, permission denied, etc.)
                    Toast.makeText(this, "Fail to add", Toast.LENGTH_SHORT).show();
                });
    }

    public void setLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);

        // Check if at least one provider is enabled
        if (provider == null || !locationManager.isProviderEnabled(provider)) {
            Toast.makeText(this, "No enabled location provider", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted (this should not happen as we check permission before calling this method)
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location!= null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
            GeoPoint newGeopoint = new GeoPoint(latitude, longitude);
            updateGeopoints(newGeopoint);
        } else {
            Toast.makeText(this, "Fail to add", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // You have to request permissions before calling this method
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                            GeoPoint newGeopoint = new GeoPoint(latitude, longitude);
                            updateGeopoints(newGeopoint);
                        } else {
                            Toast.makeText(GeopointDialog.this, "Fail to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}