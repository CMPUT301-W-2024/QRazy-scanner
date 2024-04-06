package com.example.projectapp;

import com.example.projectapp.Model.Attendee;
import com.google.firebase.firestore.DocumentChange;

public interface AttendeesListenerCallback {
    void onAttendeesUpdated(DocumentChange.Type updateType, Attendee attendee);
}
