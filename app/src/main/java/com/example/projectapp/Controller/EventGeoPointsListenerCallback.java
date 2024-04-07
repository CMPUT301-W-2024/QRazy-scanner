package com.example.projectapp.Controller;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public interface EventGeoPointsListenerCallback {
    void onEventGeoPointsUpdated(List<GeoPoint> geoPoints);
}
