package com.example.projectapp.Controller;

import com.example.projectapp.Model.Event;
import com.google.firebase.firestore.DocumentChange;

/**
 * An interface defining a callback mechanism to signal updates related to events
 * managed within a Firestore collection.
 */
public interface EventsListenerCallback {
    void onEventsUpdated(DocumentChange.Type updateType, Event event);
}
