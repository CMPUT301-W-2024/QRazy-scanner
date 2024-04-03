package com.example.projectapp;

import com.google.firebase.firestore.DocumentChange;

public interface AllEventsCallback {
    void onAllEventsUpdated(DocumentChange.Type updateType, Event event);
}
