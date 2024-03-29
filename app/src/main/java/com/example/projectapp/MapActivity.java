package com.example.projectapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private MapView mMapView;
    private MapController mMapController;
    private MarkerAdapter markerAdapter;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markerAdapter = new MarkerAdapter((String) getIntent().getSerializableExtra("eventId"));

        // Configure the user agent
        Configuration.getInstance().setUserAgentValue("Project app");

        setContentView(R.layout.osm_main); // Make sure you have a layout file named osm_main.xml

        // Initialize the map view
        mMapView = findViewById(R.id.mapview);
        backButton = findViewById(R.id.goBackButton);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapView.setBuiltInZoomControls(true);

        // Set an initial zoom level and center point
        mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(10);

        mMapView.setMaxZoomLevel(22.0);
        mMapView.setMinZoomLevel(5.0);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            setupMap();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Go back to MainActivity
            }
        });
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                setupMap();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Get device's location
    private void setupMap() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        double latitude, longitude;
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            // Default to a fallback location if device location is unavailable
            latitude = 21.0244;
            longitude = 105.8412;
        }

        GeoPoint gPt = new GeoPoint(latitude, longitude); // Use device's location
        mMapController.setCenter(gPt);

        // Use the default marker
        Drawable marker = getResources().getDrawable(android.R.drawable.ic_dialog_map);

        // Create an empty overlay for markers
        ItemizedIconOverlay<OverlayItem> itemizedOverlay = new ItemizedIconOverlay<>(this, new ArrayList<>(), null);

        // Retrieve markers from Firestore and add them to the overlay

        markerAdapter.retrieveMarkersFromFirestore(new MarkerAdapter.MarkerRetrievalListener() {
            @Override
            public void onMarkersRetrieved(List<OverlayItem> overlayItems) {
                // Clear existing items
                itemizedOverlay.removeAllItems();

                // Convert Firestore's OverlayItems to osmdroid's OverlayItems
                for (OverlayItem overlayItem : overlayItems) {
                    GeoPoint geoPoint = (GeoPoint) overlayItem.getPoint();
                    Log.d(TAG, "Geopoint marker: " + geoPoint.toString());
                    org.osmdroid.views.overlay.OverlayItem osmOverlayItem = new org.osmdroid.views.overlay.OverlayItem(null, null, new org.osmdroid.util.GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()));
                    mMapController.setCenter(new org.osmdroid.util.GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()));
                    itemizedOverlay.addItem(osmOverlayItem);
                }

                // Add the overlay to the map
                mMapView.getOverlays().add(itemizedOverlay);

                // Redraw the map to reflect the changes
                mMapView.invalidate();
                // Add this line before adding the overlay to the map
                Log.d(TAG, "Number of markers added: " + itemizedOverlay.size());

// Add this line after adding the overlay to the map
                Log.d(TAG, "Map overlays count: " + mMapView.getOverlays().size());

            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MapActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

        });
    }
}

