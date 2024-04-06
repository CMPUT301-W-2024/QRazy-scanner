package com.example.projectapp;

import com.example.projectapp.Model.Event;
import com.google.firebase.firestore.DocumentChange;

public interface OrganizerEventsListenerCallback {
    void onOrganizerEventsUpdated(DocumentChange.Type updateType, Event event);
}
