package com.example.projectapp.Controller;

import com.example.projectapp.Model.Attendee;
import com.google.firebase.firestore.DocumentChange;

/**
 * An interface defining callback mechanisms to signal updates related to checked and
 * signed attendees associated with a Firestore-backed event.
 */
public interface EventAttendeesListenerCallback {
    void onEventCheckedAttendeesUpdated(DocumentChange.Type updateType, Attendee attendee);
    void onEventSignedAttendeesUpdated(DocumentChange.Type updateType, Attendee attendee);
}
