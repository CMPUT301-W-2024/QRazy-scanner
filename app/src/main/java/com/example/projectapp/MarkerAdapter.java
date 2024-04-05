package com.example.projectapp;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MarkerAdapter {
    private static final String TAG = "MarkerAdapter";
    private static final String COLLECTION_NAME = "markers";

    private FirebaseFirestore db;
    private String eventId;

    public MarkerAdapter(String aeventId) {
        db = FirebaseFirestore.getInstance();
        eventId = aeventId;
    }

    public void retrieveMarkersFromFirestore(final MarkerRetrievalListener listener) {
        DocumentReference eventRef = db.collection("events").document(eventId);

        // Add a real-time snapshot listener to the document
        eventRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot snapshot,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle errors (e.g., permission issues, network errors)
                    Log.e(TAG, "Error listening to event document", e);
                    listener.onError("Error listening to event document");
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    // Document exists, access the geopoints field
                    List<OverlayItem> overlayItems = mapToOverlayItems(snapshot);
                    listener.onMarkersRetrieved(overlayItems);
                } else {
                    // Document does not exist
                    Log.e(TAG, "Event document not found");
                    listener.onError("Event document not found");
                }
            }
        });
    }

    private List<OverlayItem> mapToOverlayItems(DocumentSnapshot eventDocument) {
        List<OverlayItem> overlayItems = new ArrayList<>();

        List<com.google.firebase.firestore.GeoPoint> geoPointsList = (List<com.google.firebase.firestore.GeoPoint>) eventDocument.get("geopoints");
        Log.d(TAG, "Number of geopoints retrieved: " + geoPointsList.size());

        if (geoPointsList != null && !geoPointsList.isEmpty()) {
            for (com.google.firebase.firestore.GeoPoint geoPoint : geoPointsList) {
                double latitude = geoPoint.getLatitude();
                double longitude = geoPoint.getLongitude();
                org.osmdroid.util.GeoPoint point = new org.osmdroid.util.GeoPoint(latitude, longitude);
                OverlayItem overlayItem = new OverlayItem(null, null, point); // Passing null for title and description
                overlayItems.add(overlayItem);
            }
        }
        Log.d(TAG, "Number of overlayitems retrieved: " + overlayItems.size());

        return overlayItems;
    }


    public interface MarkerRetrievalListener {
        void onMarkersRetrieved(List<OverlayItem> overlayItems);

        void onError(String errorMessage);
    }
}

