package com.example.projectapp;

import com.example.projectapp.Model.Event;
import com.google.firebase.firestore.DocumentChange;

public interface EventsListenerCallback {
    void onEventsUpdated(DocumentChange.Type updateType, Event event);
}
