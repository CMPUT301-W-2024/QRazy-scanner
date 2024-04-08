package com.example.projectapp.Controller;

import com.example.projectapp.Model.Event;
import com.google.firebase.firestore.DocumentChange;

/**
 * An interface defining a callback mechanism to signal the update of an attendee event.
 */
public interface AttendeeEventsListenerCallback {
    void onAttendeeEventsUpdated(DocumentChange.Type updateType, Event event);
}
