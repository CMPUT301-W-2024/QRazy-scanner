package com.example.projectapp;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class DataHandler {

    private static DataHandler instance;
    private FirebaseFirestore db;

    private DataHandler(){
        db = FirebaseFirestore.getInstance();
    }

    public static DataHandler getInstance(){
        if (instance == null){
            instance = new DataHandler();
        }
        return instance;
    }

    public void addAttendeeListener(){
        CollectionReference attendeesRef = db.collection("attendees");

        attendeesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            System.out.println("New attendee: " + dc.getDocument().getData());
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

    public void addEventListener(){
        CollectionReference eventsRef = db.collection("events");

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            System.out.println("New event: " + dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            System.out.println("Modified event: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            System.out.println("Removed event: " + dc.getDocument().getData());
                            break;
                    }
                }
            }
        });
    }

    public void addOrganizerListener(){
        CollectionReference organizersRef = db.collection("organizers");

        organizersRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            System.out.println("New organizer: " + dc.getDocument().getData());
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

    public void addAllListeners(){
        addAttendeeListener();
        addOrganizerListener();
        addEventListener();
    }

}
