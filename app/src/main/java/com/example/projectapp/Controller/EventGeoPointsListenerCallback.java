package com.example.projectapp.Controller;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

/**
 * An interface defining a callback mechanism to signal updates related to
 * geographic points (GeoPoints) associated with a Firestore-backed event.
 */
public interface EventGeoPointsListenerCallback {
    void onEventGeoPointsUpdated(List<GeoPoint> geoPoints);
}
