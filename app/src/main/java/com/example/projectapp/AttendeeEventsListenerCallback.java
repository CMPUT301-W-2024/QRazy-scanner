package com.example.projectapp;

import com.google.firebase.firestore.DocumentChange;

public interface AttendeeEventsListenerCallback {
    void onAttendeeEventsUpdated(DocumentChange.Type updateType, Event event);
}
