package com.example.projectapp.Controller;

import com.example.projectapp.Model.Attendee;
import com.google.firebase.firestore.DocumentChange;

/**
 * An interface defining a callback mechanism to signal the update of an attendee.
 */
public interface AttendeesListenerCallback {
    void onAttendeesUpdated(DocumentChange.Type updateType, Attendee attendee);
}
