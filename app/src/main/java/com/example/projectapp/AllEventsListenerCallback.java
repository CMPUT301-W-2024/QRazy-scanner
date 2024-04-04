package com.example.projectapp;

import com.google.firebase.firestore.DocumentChange;

public interface AllEventsListenerCallback {
    void onAllEventsUpdated(DocumentChange.Type updateType, Event event);
}
