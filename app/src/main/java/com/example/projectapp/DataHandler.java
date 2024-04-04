package com.example.projectapp;

import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
        FirebaseMessaging.getInstance().subscribeToTopic(eventId);
    }
    public void unSubscribeToTopic(String eventId){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(eventId);
    }


    public void getEvent(String eventId, GetEventCallback callback){
        CollectionReference eventsRef = db.collection("events");
        eventsRef.document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Event event = documentSnapshot.toObject(Event.class);
                    if (event != null) {
                        callback.onGetEvent(event, false);
                    }
                } else {
                    callback.onGetEvent(null, true);
                }
            }
        }).addOnFailureListener(e -> {
            callback.onGetEvent(null, true);
        });
    }


    public void addProfileDeletedListener(ProfileDeletedCallback callback){
        CollectionReference attendeesRef = db.collection("attendees");
        attendeesRef.whereEqualTo("attendeeId", attendee.getAttendeeId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.REMOVED){
                        callback.onProfileDeleted();
                    }
                }
            }
        });
    }

    public void addAttendeeEventsListener(AttendeeEventsCallback callback) {
        CollectionReference eventsRef = db.collection("events");
        eventsRef.whereArrayContains("signedAttendees", getAttendee().getAttendeeId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null) {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        callback.onAttendeeEventUpdated(dc.getType(), event);
                    }
                }
            }
        });
    }

    public void addAllEventsListener(AllEventsCallback callback){
        CollectionReference eventsRef = db.collection("events");
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>(){
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (snapshots != null){
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        Event event = dc.getDocument().toObject(Event.class);
                        callback.onAllEventsUpdated(dc.getType(), event);
                    }
                }
            }
        });
    }

}
