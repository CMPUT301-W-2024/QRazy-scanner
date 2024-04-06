package com.example.projectapp;

import com.example.projectapp.Model.Event;
import com.google.firebase.firestore.DocumentChange;

public interface AttendeeEventsListenerCallback {
    void onAttendeeEventsUpdated(DocumentChange.Type updateType, Event event);
}
