package com.example.projectapp;

import com.google.firebase.firestore.DocumentChange;

public interface AttendeeEventsCallback {
    void onAttendeeEventUpdated(DocumentChange.Type updateType, Event event);
}
