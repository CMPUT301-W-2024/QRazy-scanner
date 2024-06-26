package com.example.projectapp.View;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.projectapp.Controller.DataHandler;
import com.example.projectapp.R;
import com.example.projectapp.Controller.UpdateEventCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;

/**
 * The GeopointDialog class provides a dialog interface for an attendee to record their geolocation
 * as a check-in point for a specific event.
 */
public class GeopointDialog extends AppCompatActivity {
    private String eventId; // The variable you want to pass
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final DataHandler dataHandler = DataHandler.getInstance();
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geopoint_dialog);
        checkLocationPermission();
        eventId = getIntent().getStringExtra("eventId");

        Button okButton = findViewById(R.id.okButton);
        Button cancelButton = findViewById(R.id.noButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check location permission and proceed to set location if permission is granted
                getCurrentLocation();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle cancel button if needed
                finish(); // Close the dialog
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
                //getCurrentLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Checks if the app has location permissions. If not granted, requests permission.
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(GeopointDialog.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(GeopointDialog.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, set location
            //getCurrentLocation();
        }
    }


    /**
     *  Obtains the device's current location (if permission is granted) and  updates the Firestore event
     *  document with the attendee's geolocation data.
     */
    private void getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // You have to request permissions before calling this method
            finish();
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
                            dataHandler.updateEvent(eventId, "geopoints."+dataHandler.getLocalAttendee().getAttendeeId(), FieldValue.arrayUnion(newGeopoint), null);
                        } else {
                            Toast.makeText(GeopointDialog.this, "Fail to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}