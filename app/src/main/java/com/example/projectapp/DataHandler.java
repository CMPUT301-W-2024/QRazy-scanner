package com.example.projectapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class DataHandler {

    private static DataHandler instance;
    private FirebaseFirestore db;
    private AttendeePageActivity attendeePageActivity;
    private Attendee attendee;
    private Organizer organizer;
    private ListenerRegistration attendeesListener;
    private ListenerRegistration eventsListener;
    private ListenerRegistration organizersListener;

    private DataHandler(){
        db = FirebaseFirestore.getInstance();
        attendeePageActivity = AttendeePageActivity.getInstance();
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

    public void addAttendeesListener(){
        CollectionReference attendeesRef = db.collection("attendees");

        attendeesListener = attendeesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    Attendee a = dc.getDocument().toObject(Attendee.class);
                    switch (dc.getType()) {
                        case ADDED:
                            break;
                        case MODIFIED:
                            System.out.println("Modified attendee: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            System.out.println("Removed attendee: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });
    }

    public void removeAttendeesListener(){
        attendeesListener.remove();
    }

    public void addEventsListener(){
        System.out.println("added listener letsgooo");
        CollectionReference eventsRef = db.collection("events");

        eventsListener = eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            System.out.println("Added event: " + dc.getDocument().toObject(Event.class).getEventId());
                            AttendeePageActivity.getInstance().addEvent(dc.getDocument().toObject(Event.class));
                            break;
                        case MODIFIED:
                            System.out.println("Modified event: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            AttendeePageActivity.getInstance().removeEvent(dc.getDocument().toObject(Event.class));
                            break;
                    }
                }
            }
        });
    }

    public void removeEventsListener(){
        eventsListener.remove();
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

    public void removeOrganizersListener(){
        organizersListener.remove();
    }

    public void addAllListeners(){
        addAttendeesListener();
        addOrganizersListener();
        addEventsListener();
    }

/*    public Attendee getAttendee(String attendeeId){
        CollectionReference attendeesRef = db.collection("attendees");
        attendeesRef.document(attendeeId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                attendee = documentSnapshot.toObject(Attendee.class);
            }
        });;
        return attendee;
    }*/

    public void addAttendee(Attendee attendee){
        CollectionReference attendeesRef = db.collection("attendees");

        attendeesRef.document(attendee.getAttendeeId()).set(attendee);
    }

    public void addEvent(Event event){
        CollectionReference attendeesRef = db.collection("events");

        attendeesRef.document(event.getEventId()).set(event);
    }

/*    public Event getEvent(String eventId){
        CollectionReference eventsRef = db.collection("events");
        eventsRef.document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                event = documentSnapshot.toObject(Event.class);
            }
        });
        return event;
    }*/

    public void addOrganizer(Organizer organizer){
        CollectionReference organizersRef = db.collection("organizers");

        organizersRef.document(organizer.getOrganizerId()).set(organizer);
    }

/*    public Organizer getOrganizer(String organizerId){
        CollectionReference organizersRef = db.collection("organizers");
        organizersRef.document(organizerId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                organizer = documentSnapshot.toObject(Organizer.class);
            }
        });
        return organizer;
    }*/
}
