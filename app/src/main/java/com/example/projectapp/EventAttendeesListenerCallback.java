package com.example.projectapp;

import com.google.firebase.firestore.DocumentChange;

public interface EventAttendeesListenerCallback {
    void onCheckedInUpdated(DocumentChange.Type updateType, Attendee attendee);
    void onSignedUpUpdated(DocumentChange.Type updateType, Attendee attendee);
}
