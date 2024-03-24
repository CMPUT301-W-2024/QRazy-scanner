package com.example.projectapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Data handler. Handles interactions with Firebase,
 * stores attendee and organizer data, and sets up listeners for data changes.
 */
public class DataHandler {

    private static DataHandler instance;
    private FirebaseFirestore db;
    private Attendee attendee;
    private Organizer organizer;
    private ListenerRegistration attendeesListener;
    private ListenerRegistration eventsListener;
    private ListenerRegistration organizersListener;

    private DataHandler(){
        db = FirebaseFirestore.getInstance();
    }

    public static DataHandler getInstance(){
        if (instance == null){
            instance = new DataHandler();
        }
        return instance;
    }

    /**
     * Get the Attendee
     * @return the Attendee
     */
    public Attendee getAttendee() {
        return attendee;
    }

    /**
     * Set the attendee
     * @param attendee an attendee
     */
    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    /**
     * Get the organizer
     * @return the organizer
     */
    public Organizer getOrganizer() {return organizer;}

    /**
     * Set the organizer
     * @param organizer a organizer
     */
    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public void addOrganizersListener(){
        CollectionReference organizersRef = db.collection("organizers");

        organizersListener = organizersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    Organizer o = dc.getDocument().toObject(Organizer.class);
                    switch (dc.getType()) {
                        case ADDED:
                            break;
                        case MODIFIED:
                            System.out.println("Modified organizer: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            System.out.println("Removed organizer: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });
    }

    /**
     * Adds an Attendee document to the "attendees" collection in Firebase.
     * @param attendee
     *      The Attendee object representing the data to add.
     */
    public void addAttendee(Attendee attendee){
        CollectionReference attendeesRef = db.collection("attendees");

        attendeesRef.document(attendee.getAttendeeId()).set(attendee);
    }

    /**
     * Adds an Event document to the "events" collection in Firebase.
     * @param event
     *      The Event object representing the data to add.
     */
    public void addEvent(Event event){
        CollectionReference attendeesRef = db.collection("events");

        attendeesRef.document(event.getEventId()).set(event);
    }

    /**
     * Adds an Organizer document to the "organizers" collection in Firebase.
     * @param organizer
     *      The Organizer object representing the data to add.
     */
    public void addOrganizer(Organizer organizer){
        CollectionReference organizersRef = db.collection("organizers");

        organizersRef.document(organizer.getOrganizerId()).set(organizer);
    }

    public void updateEvent(String eventId, String field, String value){
        CollectionReference eventRef = FirebaseFirestore.getInstance().collection("events");
        eventRef.document(eventId).update(field, value);
    }

    public void updateAttendee(String attendeeId, String field, String value){
        CollectionReference attendeeRef = FirebaseFirestore.getInstance().collection("attendees");
        attendeeRef.document(attendeeId).update(field, value);
    }

    public void subscribeToTopic(String eventId){
        FirebaseMessaging.getInstance().subscribeToTopic(eventId)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                    }
                });
    }

}
