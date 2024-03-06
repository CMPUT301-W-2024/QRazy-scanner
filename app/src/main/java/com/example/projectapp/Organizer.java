package com.example.projectapp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

public class Organizer {
    private String organizerId;
    private ArrayList<String> events;

    public Organizer() {
        organizerId = UUID.randomUUID().toString();
        events = new ArrayList<>();
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    public void addEvent(String eventId){
        if (!events.contains(eventId)){
            events.add(eventId);
            DocumentReference attendeeRef = FirebaseFirestore.getInstance().collection("organizers").document(organizerId);
            attendeeRef.update("events", events);
        }
    }
}
