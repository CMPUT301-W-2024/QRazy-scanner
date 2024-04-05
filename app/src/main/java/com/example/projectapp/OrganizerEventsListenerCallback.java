package com.example.projectapp;

import com.google.firebase.firestore.DocumentChange;

public interface OrganizerEventsListenerCallback {
    void onOrganizerEventsUpdated(DocumentChange.Type updateType, Event event);
}
