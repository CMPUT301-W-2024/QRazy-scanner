package com.example.projectapp;

import com.example.projectapp.Model.Attendee;
import com.google.firebase.firestore.DocumentChange;

public interface EventAttendeesListenerCallback {
    void onEventCheckedAttendeesUpdated(DocumentChange.Type updateType, Attendee attendee);
    void onEventSignedAttendeesUpdated(DocumentChange.Type updateType, Attendee attendee);
}
