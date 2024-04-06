package com.example.projectapp.Controller;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public interface SpecificEventListenerCallback {
    void onSpecificEventUpdated(List<GeoPoint> geoPoints);
}
